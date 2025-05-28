package io.github.swampus.aspect;

import io.github.swampus.access.AccessControlled;
import io.github.swampus.access.AccessLedger;
import io.github.swampus.exception.ShelduerAccessDeniedException;
import io.github.swampus.policy.AccessPolicy;
import io.github.swampus.resolver.SpELKeyResolver;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AccessControlAspect {

    private final AccessLedger accessLedger;
    private final SpELKeyResolver keyResolver;

    @Autowired
    public AccessControlAspect(AccessLedger accessLedger, SpELKeyResolver keyResolver) {
        this.accessLedger = accessLedger;
        this.keyResolver = keyResolver;
    }

    @Around("@annotation(controlled)")
    public Object checkAccess(ProceedingJoinPoint pjp, AccessControlled controlled)
            throws Throwable {
        String key = keyResolver.resolveKey(pjp, controlled.key());
        AccessPolicy policy = controlled.policy();
        long timeout = controlled.timeoutMs();

        boolean acquired = accessLedger.tryAcquire(key, policy, timeout);
        if (!acquired) {
            throw new ShelduerAccessDeniedException("Access denied for key: " + key);
        }

        try {
            return pjp.proceed();
        } finally {
            accessLedger.release(key);
        }
    }
}

package io.github.swampus.aspect;

import io.github.swampus.KeyProvider;
import io.github.swampus.access.AccessControlled;
import io.github.swampus.core.access.LedgerTable;
import io.github.swampus.exception.KeyProviderInstantiationException;
import io.github.swampus.policy.AccessPolicy;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Aspect that intercepts methods annotated with @AccessControlled and applies access scheduling.
 */
@Aspect
@Slf4j
@Component
public class AccessInterceptor {

    private final LedgerTable ledgerTable;

    public AccessInterceptor(LedgerTable ledgerTable) {
        this.ledgerTable = ledgerTable;
    }

    @Around("@annotation(accessControlled)")
    public Object intercept(ProceedingJoinPoint joinPoint, AccessControlled accessControlled) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();

        String key = resolveKey(accessControlled, target, method, args);
        AccessPolicy policy = accessControlled.policy();
        long timeout = accessControlled.timeoutMs();

        boolean acquired = ledgerTable.tryAcquire(key, policy, timeout);
        if (!acquired) {
            log.debug("🚫 Access skipped for key: " + key + " [policy=" + policy + "]");
            return null; // or throw, or just exit silently
        }

        try {
            return joinPoint.proceed();
        } finally {
            ledgerTable.release(key);
        }
    }

    private String resolveKey(AccessControlled annotation, Object target, Method method, Object[] args) {
        String key = annotation.value();
        if (!key.isBlank()) {
            return key;
        }

        try {
            KeyProvider provider = annotation.keyProvider().getDeclaredConstructor().newInstance();
            return provider.resolveKey(target, method, args);
        } catch (Exception e) {
            throw new KeyProviderInstantiationException("Failed to instantiate KeyProvider", e);
        }
    }
}


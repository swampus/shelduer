package io.github.swampus.config.aspect;

import io.github.swampus.AccessControlled;
import io.github.swampus.access.IAccessManager;
import io.github.swampus.exception.ShelduerAccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Aspect that intercepts methods annotated with @AccessControlled.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AccessControlAspect {

    private final IAccessManager accessManager;

    @Around("@annotation(io.github.swampus.shelduer.core.AccessControlled)")
    public Object enforceAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AccessControlled annotation = method.getAnnotation(AccessControlled.class);
        String key = annotation.value();

        if (!accessManager.isAccessAllowed(key)) {
            log.warn("❌ Shelduer denied access to: {}", key);
            throw new ShelduerAccessDeniedException(key);
        }

        return joinPoint.proceed();
    }
}

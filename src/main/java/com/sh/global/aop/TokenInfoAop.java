package com.sh.global.aop;

import com.sh.global.util.jwt.JwtProvider;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class TokenInfoAop {

    private final JwtProvider jwtProvider;

    @Around("@annotation(com.sh.global.aop.TokenInfo)")
    public Object getToken(ProceedingJoinPoint joinPoint) throws Throwable {

        // Request Header 에 담긴 token 가져옴
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String accessToken = jwtProvider.resolveAccessToken(request);
        String refreshToken = jwtProvider.resolveRefreshToken(request);

        // Access Token 검증
        jwtProvider.validateToken(accessToken);

        // 타겟 메소드 인자에 accessToken, refreshToken 을 파라미터에 넣어줌.
        Object[] modifiedArgs = modifyArgsWithToken(accessToken, refreshToken, joinPoint);

        // 변경된 파라미터로 타깃 메서드 실행
        return joinPoint.proceed(modifiedArgs);
    }

    private Object[] modifyArgsWithToken(
            String accessToken, String refreshToken, ProceedingJoinPoint joinPoint) {
        Object[] parameters = joinPoint.getArgs();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        for (int i = 0; i < method.getParameters().length; i++) {
            String parameterName = method.getParameters()[i].getName();
            if (parameterName.equals("accessToken")) {
                parameters[i] = accessToken;
            }

            if (parameterName.equals("refreshToken")) {
                parameters[i] = refreshToken;
            }
        }

        return parameters;
    }
}

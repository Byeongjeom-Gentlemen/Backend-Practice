package com.sh.global.aop;

import com.sh.global.util.jwt.JwtProvider;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;

import com.sh.global.util.jwt.TokenDto;
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
public class TokenValueRequiredAop {

    private static final String TOKEN_PARAMETER_NAME = "token";
    private final JwtProvider jwtProvider;

    @Around("@annotation(com.sh.global.aop.TokenValueRequired)")
    public Object getToken(ProceedingJoinPoint joinPoint) throws Throwable {

        // Request Header 에 담긴 token 가져옴
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String accessToken = jwtProvider.resolveAccessToken(request);
        String refreshToken = jwtProvider.resolveRefreshToken(request);

        // 타겟 메소드 인자에 accessToken, refreshToken 을 파라미터에 넣어줌.
        Object[] modifiedArgs = modifyArgsWithToken(accessToken, refreshToken, joinPoint);

        // 변경된 파라미터로 타깃 메서드 실행
        return joinPoint.proceed(modifiedArgs);
    }

    // 타겟 메소드 파라미터 값을 확인해, 조건에 맞는 파라미터 값 존재 시 값 수정/세팅
    private Object[] modifyArgsWithToken(
            String accessToken, String refreshToken, ProceedingJoinPoint joinPoint) {
        Object[] parameters = joinPoint.getArgs();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        TokenDto token = TokenDto.of(accessToken, refreshToken);
        for (int i = 0; i < method.getParameters().length; i++) {
            String parameterName = method.getParameters()[i].getName();

            if(parameterName.equals(TOKEN_PARAMETER_NAME)) {
                parameters[i] = token;
            }
        }

        return parameters;
    }
}

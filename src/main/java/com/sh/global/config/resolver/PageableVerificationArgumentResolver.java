package com.sh.global.config.resolver;

import com.sh.global.exception.customexcpetion.page.PageRangeOverException;
import com.sh.global.exception.customexcpetion.page.SizeRangeOverException;
import com.sh.global.exception.customexcpetion.page.ValueIsNotIntegerException;
import com.sh.global.exception.errorcode.PageErrorCode;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

// 페이지, 사이즈 검증 Resolver
@Component
public class PageableVerificationArgumentResolver extends PageableHandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return super.supportsParameter(parameter);
    }

    @Override
    public Pageable resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        final String pageText = webRequest.getParameter("page");
        final String sizeText = webRequest.getParameter("size");

        // page값이 숫자가 아니라면
        if (pageText != null && !pageText.equals("") && !isInteger(pageText)) {
            throw new ValueIsNotIntegerException(PageErrorCode.IS_NOT_INTEGER);
        }

        // size값이 숫자가 아니라면
        if (sizeText != null && !sizeText.equals("") && !isInteger(sizeText)) {
            throw new ValueIsNotIntegerException(PageErrorCode.IS_NOT_INTEGER);
        }

        // page가 음수라면
        if (pageText != null && !pageText.equals("") && Integer.parseInt(pageText) < 0) {
            throw new PageRangeOverException(PageErrorCode.PAGE_VALUE_OVER_RANGE);
        }

        // size가 1보다 작거나 10보다 크면
        if (sizeText != null
                && !sizeText.equals("")
                && (Integer.parseInt(sizeText) < 1 || Integer.parseInt(sizeText) > 10)) {
            throw new SizeRangeOverException(PageErrorCode.SIZE_VALUE_OVER_RANGE);
        }

        return super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }

    public boolean isInteger(String strValue) {
        try {
            Integer.parseInt(strValue);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
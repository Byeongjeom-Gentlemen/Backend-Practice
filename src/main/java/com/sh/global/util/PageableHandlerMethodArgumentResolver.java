package com.sh.global.util;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.data.web.PageableHandlerMethodArgumentResolverSupport;
import org.springframework.data.web.SortArgumentResolver;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageableHandlerMethodArgumentResolver
        extends PageableHandlerMethodArgumentResolverSupport implements PageableArgumentResolver {

    private SortArgumentResolver sortResolver;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.equals(parameter.getParameterType());
    }

    @Override
    public Pageable resolveArgument(
            MethodParameter methodParameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory) {
        String page =
                webRequest.getParameter(
                        getParameterNameToUse(getPageParameterName(), methodParameter));
        String pageSize =
                webRequest.getParameter(
                        getParameterNameToUse(getSizeParameterName(), methodParameter));
        Sort sort =
                sortResolver.resolveArgument(
                        methodParameter, mavContainer, webRequest, binderFactory);
        Pageable pageable = getPageable(methodParameter, page, pageSize);

        if (sort.isSorted()) {
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        return pageable;
    }
}

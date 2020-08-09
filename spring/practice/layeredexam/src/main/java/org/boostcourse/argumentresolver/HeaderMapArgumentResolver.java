package org.boostcourse.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Iterator;

public class HeaderMapArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HeaderInfo headerInfo = new HeaderInfo();

        Iterator<String> headerNames = nativeWebRequest.getHeaderNames();
        while(headerNames.hasNext()){
            String headerName = headerNames.next();
            String headerValue  = nativeWebRequest.getHeader(headerName);
            System.out.println(headerName + ", " + headerValue);
            headerInfo.put(headerName, headerValue);
        }

        return headerInfo;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) { // 컨트롤러 메서드의 인자가 4개일 경우, supportsParameter() 메서드가 4번 호출된다.
                                                                        // 원하는 타입의 인자가 있는지 검사한 후, 있다면 true를 리턴
        return methodParameter.getParameterType() == HeaderInfo.class;
    }
 }

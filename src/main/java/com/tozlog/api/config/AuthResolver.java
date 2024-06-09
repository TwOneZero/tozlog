package com.tozlog.api.config;


import com.tozlog.api.domain.Session;
import com.tozlog.api.exception.post.Unauthorized;
import com.tozlog.api.repository.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {
    private final SessionRepository sessionRepository;

    //paramter 체크하는 함수
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    //파라미터를 set 해주는 함수
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        String accessToken = webRequest.getHeader("Authorization");
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            log.error(">>> HttpServlet request 가 null 임");
            throw new Unauthorized();
        }
        Cookie[] cookies =  request.getCookies();
        if (cookies == null || cookies.length == 0) {
            log.error(">>> Cookie 리스트가 null 임");
            throw new Unauthorized();
        }
        String accessToken = Arrays.stream(cookies).map(Cookie::getValue).findAny().get();
        log.info(">>> 쿠키에서 추출한  accessToken => {}", accessToken);
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(Unauthorized::new);

        return new UserSession(session.getUserAccount().getId());
    }
}

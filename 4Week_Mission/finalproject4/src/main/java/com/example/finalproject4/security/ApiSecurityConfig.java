package com.example.finalproject4.security;


//@EnableWebSecurity
//@RequiredArgsConstructor
//public class ApiSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        http
//                .cors().disable() // 타 도메인에서 API 호출 가능
//                .csrf().disable() // CSRF 토큰 끄기
//                .httpBasic().disable() // httpBaic 로그인 방식 끄기
//                .formLogin().disable() // 폼 로그인 방식 끄기
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(STATELESS)
//                ); // 세션 사용안함
//
//        return http.build();
//    }
//
//
//}

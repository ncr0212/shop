package com.human.shop.security;
// 1. 시큐리티에서 제일 먼저 설정하는 것을 추천.
//  ㄴ 필터 설정 후 객체생성해 달라고 어노테이션
//  ㄴ 필터를 통과하지 못한 경우 로그인 처리
//  ㄴ 로그아웃 처리도 필터가 잡아서 한다. = <컨트롤러 진입까지 가지 않는다>

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // 환경설정 파일이니 스프링이 가동될 때 확인해서 객체 만들어
@EnableWebSecurity // 스프링 시큐리티로 동작할 거야.
public class SecurityConfig {

    @Bean //객체를 만들어달라.. BCryptPasswordEncoder는 스프링에서 제공되는 암호화메서드
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 1. SecurityFilterChain 이라는 스프링 필터 객체가 필요하다.
    @Bean   //bean 어노테이션으로 설정된 메서드로 객체 만들어라.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // 설정
        http.csrf(csrf -> csrf.disable())
        // 인증과 인가 설정
            .authorizeHttpRequests(auth ->
                auth
                .requestMatchers("/", "/login", "/members", "/css/**", "/js/**", "/access-denied"
                ) //request가 /라면
                .permitAll()                        //허가해라 
                .requestMatchers("/boards/**").hasRole("USER")
                .requestMatchers("/boards/**").hasRole("ADMIN")
                .requestMatchers("/managers/**").hasRole("ADMIN")
                .anyRequest().authenticated()       //위에서 허가되지 않은 요청은 인가와 인증을 받아라
            ).formLogin(form -> form
                //인증 받아야하는데 인증받도록 요청하는 코드
                .loginPage("/login") //인증이 필요한 경우 get 방식으로 컨트롤러에게 요청
                //클라이언트 요청이 post방식에 login이라면 다음과 같이 처리 
                //시큐리티가 인증받도록 돌려라 
                //인증은 데이터베이스로부터 조회를 한 후에 비밀번호 일치 판단 
                //처리하는 흐름 <코드에서만 생략되고, 실제로는 시큐리티가 알아서 해준다.>
                // UsernamePasswordAuthenticationFilter
                // 클라이언트가 입력한 파라미터에서 username과 password를 가져온다.

                //AuthenticationManager
                //인증총괄관리자.. 실제 인증을 하는 provider에게 작업 위임

                //DaoAuthentication Provider
                //데이터베이스에 저장된 회원 정보를 이용하여 아이디와 비번을 검증 
                //userDetailService에게 정보 가져와.
                
                //UserDetailsService
                //매퍼를 통해서 정보를 가져옴.

                //UserDetails
                //인증된 사용자 정보를 스프링이 인식할 수 있도록 랩핑

                //인증이 성공되면 Authentiation객체를 생성하고
                //Authentiation 객체는 principal(사용자이름)
                //Credentials(인증정보-비번), Authorities(권한-role)
                //정보를 저장합니다.

                //이 객체를 SecurityContext에 저장
                //SecurityContext를 세션(JSESSIONID)에 저장을 합니다.

                //한줄요약: /post /login 요청이 오면 UserDetailService가 동작하고
                // 데이터베이스로부터 사용자 정보 가져오고 비번 확인하고 
                // UserDetail 객체로 랩핑해서 로그인 인증한다. 


                .loginProcessingUrl("/login")  
                .defaultSuccessUrl("/", true) //성공했을 경우
                .failureUrl("/login?error") //인증 과정에서
                                                                        //아이디 또는 비번이 틀린 경우
                                                                        //요청하는 url
                .permitAll())
            //  로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/logout") // Spring Security가 가로채서 로그아웃 처리
                .invalidateHttpSession(true) // 서버에 저장된 HttpSession 제거
                .deleteCookies("JSESSIONID") // 클라이언트의 세션 식별 쿠키 삭제
                .logoutSuccessUrl("/login?logout")) // 로그아웃 완료 후 로그인 페이지로 리다이렉트
            .exceptionHandling(exception -> exception
                                                // 로그인 안 된 경우 authenticationEntryPoint
                .authenticationEntryPoint((request, response, authException) -> {
                                                        response.sendRedirect("/login?message=needLogin");
                                                })

                                                // 권한 없는 경우 accessDeniedHandler
                                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                                        response.sendRedirect("/access-denied?error=noPermission");
                                                }));
        
        return http.build();
    }
}

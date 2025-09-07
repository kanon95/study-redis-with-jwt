# study-redis-with-jwt

## 프로젝트 개요

이 프로젝트는 Spring Security와 Redis를 활용한 JWT 인증 시스템 구현에 대한 학습 프로젝트입니다.

## Spring Security 인증 흐름

### 1. 인증 프로세스

1. **로그인 요청**: 사용자가 `/login` 페이지에서 username과 password를 입력
2. **인증 처리**: 
   - UsernamePasswordAuthenticationFilter가 요청 인터셉트
   - AuthenticationManager가 인증 로직 실행
   - CustomUserDetailsService의 `loadUserByUsername()` 메서드 호출
3. **사용자 정보 조회**: 
   - UserRepository를 통해 DB에서 사용자 정보 조회
   - UserDetails 객체 생성 및 반환
4. **비밀번호 검증**:
   - PasswordEncoder.matches()로 입력 비밀번호와 저장된 해시 비교
   - 인증 성공 시 Authentication 객체 생성 및 SecurityContext에 저장
5. **세션 관리**:
   - 성공 시 세션에 인증 정보 저장 (Redis 사용 시 분산 세션)
   - 사용자 리디렉션 (기본값: `/`)

### 2. 주요 컴포넌트

- **SecurityConfig**: 보안 설정 클래스
  - `@EnableWebSecurity` 어노테이션으로 Spring Security 활성화
  - `SecurityFilterChain`을 @Bean으로 등록 (메서드명은 자유롭게 지정 가능)
  - HTTP 요청 인가 규칙 설정

- **CustomUserDetailsService**: 사용자 조회 및 인증 로직
  - `loadUserByUsername(String username)` 메서드 구현
  - 반환된 UserDetails 객체가 인증 검증에 사용됨

- **PasswordEncoder**: 비밀번호 해싱 및 검증
  - BCryptPasswordEncoder가 일반적으로 사용됨
  - `encode()`: 비밀번호 해싱
  - `matches()`: 평문과 해시 비교

## 해결된 이슈들

### 1. 비밀번호 인증 실패

**문제**: 올바른 username과 password 입력해도 로그인 실패

**원인**: 데이터베이스에 저장된 비밀번호 해시가 실제 비밀번호와 일치하지 않음

**해결**:
- `PasswordTestService`를 생성하여 비밀번호 해시 검증
- `data.sql`의 비밀번호 해시를 올바른 값으로 수정
- 평문 비밀번호를 직접 `passwordEncoder.encode()`로 해싱하여 저장

### 2. SecurityFilterChain 설정

**문제**: `SecurityFilterChain` Bean이 적용되지 않는다는 의심

**확인**: 
- `@EnableWebSecurity` 어노테이션이 있으면 `SecurityFilterChain` Bean은 자동 활성화됨
- Bean 메서드 이름은 중요하지 않음 (`filterChain`, `filterChain2`, `securityConfiguration` 등 사용 가능)
- 로그를 통해 Spring Security 필터 체인이 활성화됨을 확인

### 3. 세션 관리

**문제**: 세션이 예상대로 만료되지 않음

**해결**:
- `application.yml`에서 세션 설정 조정
- `sessionManagement()` 메서드에서 최대 세션 수 및 만료 URL 설정
- 로그아웃 설정에서 세션 무효화 및 쿠키 삭제 설정

## 리소스 접근 제어

### URL 패턴별 접근 권한

```java
http.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/users/**").authenticated() // 인증 필요
    .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한 필요
    .requestMatchers("/h2-console/**").permitAll() // 모두 허용
    .requestMatchers("/css/**", "/js/**").permitAll() // 정적 리소스 허용
    .requestMatchers("/login", "/register").permitAll() // 로그인/가입 허용
    .anyRequest().authenticated() // 나머지는 인증 필요
);
```

### 메서드 수준 보안

```java
@PreAuthorize("hasRole('ADMIN')") // 메서드 호출 시 ADMIN 권한 검증
public void adminOnlyMethod() { /* ... */ }

@PreAuthorize("#username == authentication.principal.username") // SpEL 표현식 사용
public User getUserProfile(String username) { /* ... */ }
```

## 테스트 방법

### 비밀번호 검증 테스트

`testPassword` 메서드를 실행하여 비밀번호 검증 과정 확인:

1. **Controller에 엔드포인트 추가**:
   ```java
   @GetMapping("/test-password")
   @ResponseBody
   public String testPassword() {
       passwordTestService.testPassword();
       return "비밀번호 테스트 완료 - 콘솔 로그를 확인하세요";
   }
   ```

2. **@PostConstruct로 서버 시작 시 자동 실행**:
   ```java
   @PostConstruct
   public void testPassword() {
       // 비밀번호 검증 로직
   }
   ```

## 참고 사항

- Spring Security 6.0+ 버전에서는 람다 기반 설정이 권장됨
- BCrypt 해시는 매번 다른 해시 값을 생성하지만, `matches()` 메서드로 검증 가능
- SecurityFilterChain은 여러 개 등록 가능하며 @Order로 우선순위 지정
- URI 패턴 접근 제어 외에도 메서드 레벨 보안과 도메인 객체 보안(ACL) 기능 활용 가능
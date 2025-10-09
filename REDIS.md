제공된 커밋 정보를 바탕으로 프로젝트에서 Redis 사용 설정에 대해 정리해드리겠습니다.

## Redis 설정 개요

이 프로젝트에서는 Redis를 **캐싱**과 **세션 관리** 두 가지 용도로 사용하도록 설정되었습니다.

## 1. 의존성 추가

**pom.xml에 추가된 의존성:**
- `spring-boot-starter-data-redis`: Redis 데이터 액세스를 위한 기본 스타터
- `spring-boot-starter-cache`: Spring Cache 추상화 기능 활성화
- `spring-session-data-redis`: Redis를 이용한 세션 저장소 구현

## 2. 애플리케이션 레벨 설정

**StudyRedisWithJwtApplication.java:**
- `@EnableCaching` 어노테이션 추가로 캐싱 기능 활성화

## 3. 캐시 구현 (UserService)

**캐시 어노테이션 적용:**

- **`@Cacheable(value = "user", key = "#id")`** - `getUserById()` 메서드
    - 사용자 조회 시 결과를 "user" 캐시에 저장
    - 동일한 ID로 재조회 시 DB 대신 캐시에서 반환
    - 디버깅용 `System.out.println()` 추가로 캐시 동작 확인 가능

- **`@CachePut(value = "user", key = "#userDto.id")`** - `updateUser()` 메서드
    - 사용자 업데이트 시 캐시도 함께 갱신
    - 메서드가 실행되고 결과를 캐시에 저장

- **`@CacheEvict(value = "user", key = "#id")`** - `deleteUser()` 메서드
    - 사용자 삭제 시 캐시에서도 해당 데이터 제거

## 4. DTO 직렬화 설정

**UserDto.java:**
- `implements java.io.Serializable` 추가
- Redis에 객체를 저장하기 위한 직렬화 지원

## 5. 설정파일 (application.yml)

**Redis 연결 설정:**
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      repositories:
        enabled: true  # Redis 리포지토리 기능 활성화
```


**캐시 설정:**
```yaml
cache:
  type: redis           # 캐시 구현체로 Redis 사용
  redis:
    time-to-live: 60s   # 캐시 데이터 60초 후 자동 만료
```


**세션 설정:**
```yaml
session:
  store-type: redis     # 세션 저장소로 Redis 사용
```


## 주요 기능별 정리

### 캐싱 기능
- 사용자 조회 성능 향상을 위해 Redis 캐시 적용
- 60초 TTL로 데이터 자동 만료 설정
- CRUD 작업에 맞는 캐시 전략 적용

### 세션 관리
- 기존 메모리 기반 세션에서 Redis 기반 세션으로 변경
- 분산 환경에서 세션 공유 가능
- 서버 재시작 시에도 세션 유지

이러한 설정을 통해 애플리케이션의 성능 향상과 확장성을 확보할 수 있습니다.
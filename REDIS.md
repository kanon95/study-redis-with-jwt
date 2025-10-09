README 파일에 정리할 수 있는 Redis 기능 설명을 작성해드리겠습니다.

# Redis 기능 구현

본 프로젝트는 Spring Boot와 Redis를 활용하여 캐싱 및 세션 관리 기능을 구현한 JWT 인증 시스템입니다.

## 📋 목차

- [Redis 구성 요소](#redis-구성-요소)
- [주요 기능](#주요-기능)
- [API 엔드포인트](#api-엔드포인트)
- [설치 및 실행](#설치-및-실행)
- [테스트 방법](#테스트-방법)

## 🔧 Redis 구성 요소

### 의존성
```xml
<!-- Redis 기본 스타터 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- 캐싱 기능 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

<!-- Redis 세션 관리 -->
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
```


### 설정
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
  cache:
    type: redis
    redis:
      time-to-live: 60s  # 캐시 TTL 60초
  session:
    store-type: redis    # Redis 세션 저장소
```


## 🚀 주요 기능

### 1. 캐싱 시스템
- **사용자 조회 캐싱**: `@Cacheable`로 사용자 정보 조회 성능 최적화
- **캐시 업데이트**: `@CachePut`로 사용자 정보 변경 시 캐시 동기화
- **캐시 무효화**: `@CacheEvict`로 사용자 삭제 시 캐시 제거
- **TTL 관리**: 60초 자동 만료로 데이터 일관성 보장

### 2. 세션 관리
- Redis 기반 분산 세션 저장소
- 서버 재시작 시에도 세션 유지
- 다중 인스턴스 환경에서 세션 공유 지원

### 3. Redis 직접 조작
- Key-Value 저장/조회/삭제 기능
- REST API를 통한 Redis 데이터 관리
- 개발 및 테스트용 유틸리티 제공

## 📡 API 엔드포인트

### Redis 관리 API
```
# 데이터 저장
POST /api/redis/set?key={key}&value={value}

# 데이터 조회
GET /api/redis/get?key={key}

# 데이터 삭제
DELETE /api/redis/delete?key={key}
```


### 사용자 API (캐싱 적용)
```
# 사용자 조회 (캐싱됨)
GET /api/users/{id}

# 사용자 목록
GET /api/users

# 사용자 생성
POST /api/users

# 사용자 수정 (캐시 업데이트)
PUT /api/users/{id}

# 사용자 삭제 (캐시 무효화)
DELETE /api/users/{id}
```


## 🔧 설치 및 실행

### 1. Redis 서버 설치
```shell script
# Docker를 이용한 Redis 실행
docker run -d -p 6379:6379 --name redis redis:latest

# 또는 직접 설치
brew install redis  # macOS
sudo apt install redis-server  # Ubuntu
```


### 2. 애플리케이션 실행
```shell script
# Maven 빌드 및 실행
./mvnw spring-boot:run

# 또는 JAR 파일 실행
./mvnw clean package
java -jar target/study-redis-with-jwt-0.0.1-SNAPSHOT.jar
```


## 🧪 테스트 방법

### 1. Redis 연결 확인
```shell script
# Redis CLI 접속
redis-cli

# 연결 테스트
127.0.0.1:6379> ping
PONG
```


### 2. 캐싱 동작 확인
```shell script
# 사용자 조회 (첫 번째 - DB에서 조회)
curl http://localhost:8080/api/users/1

# 사용자 조회 (두 번째 - 캐시에서 조회)
curl http://localhost:8080/api/users/1

# 콘솔에서 "UserService.getUserById: 1" 메시지 확인
# 첫 번째 호출에서만 출력되어야 함
```


### 3. Redis API 테스트
```shell script
# 데이터 저장
curl -X POST "http://localhost:8080/api/redis/set?key=test&value=hello"

# 데이터 조회
curl "http://localhost:8080/api/redis/get?key=test"

# 데이터 삭제
curl -X DELETE "http://localhost:8080/api/redis/delete?key=test"
```


### 4. Redis 캐시 모니터링
```shell script
# Redis CLI에서 캐시 확인
redis-cli
127.0.0.1:6379> keys *
127.0.0.1:6379> get "user::1"  # 캐시된 사용자 데이터 확인
```


## 📊 성능 향상 효과

- **조회 성능**: DB 액세스 없이 메모리에서 빠른 데이터 반환
- **서버 부하 감소**: 반복적인 DB 쿼리 최소화
- **확장성**: 분산 캐시로 다중 서버 환경 지원
- **세션 안정성**: Redis 기반 세션으로 서버 장애 시에도 세션 유지

## 🔍 주요 특징

- **자동 캐시 관리**: Spring Cache 추상화를 통한 선언적 캐싱
- **TTL 설정**: 60초 자동 만료로 데이터 일관성 확보
- **직렬화 지원**: UserDto에 Serializable 구현으로 Redis 저장 최적화
- **보안 설정**: Redis API는 인증 없이 접근 가능하도록 설정 (개발용)
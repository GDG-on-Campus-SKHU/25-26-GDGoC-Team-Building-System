# 🚀 GDGoC Team Building System - 프로젝트 개요

## 📋 프로젝트 소개

이 프로젝트는 **GDGoC (Google Developer Groups on Campus) 성공회대학교 4기** 멤버들을 위한 **팀빌딩 시스템**의 백엔드 레포지토리입니다.

GDGoC 멤버들이 프로젝트 아이디어를 공유하고, 팀을 구성하며, 협업할 수 있도록 돕는 플랫폼입니다.

## 🎯 주요 기능

### 1. 👥 사용자 관리 (User Management)
- 사용자 회원가입 및 인증
- 이메일 인증 시스템
- JWT 기반 인증 (Access Token & Refresh Token)
- 사용자 권한 관리 (일반 사용자, 관리자)
- 사용자 승인 시스템
- 마이페이지 및 프로필 관리

### 2. 💡 아이디어 관리 (Idea Management)
- 프로젝트 아이디어 등록
- 아이디어 상세 정보 관리
- 아이디어 상태 추적 (모집 중, 진행 중, 완료 등)
- 아이디어 검색 및 필터링

### 3. 👨‍👩‍👧‍👦 팀 빌딩 (Team Building)
- 아이디어 기반 팀 구성
- 팀원 모집 및 지원
- 팀 멤버 역할 관리
- 팀 구성원 현황 관리
- 파트별 팀원 구성 (프론트엔드, 백엔드, 디자인 등)

### 4. 📁 프로젝트 갤러리 (Project Gallery)
- 완성된 프로젝트 전시
- 프로젝트 상세 정보 및 파일 관리
- 프로젝트 멤버 정보 표시

### 5. 🏢 커뮤니티 (Community)
- 프로젝트 모집 게시판
- 파일 첨부 기능

### 6. 🔐 관리자 기능 (Admin)
- 사용자 관리 및 승인
- 아이디어 관리
- 프로젝트 갤러리 관리
- 활동 관리
- 사용자 프로필 관리

## 🛠 기술 스택

### Backend Framework
- **Spring Boot 3.3.4**
- **Java 21**
- **Gradle** (빌드 도구)

### 데이터베이스 & ORM
- **MySQL** (메인 데이터베이스)
- **Spring Data JPA** (ORM)
- **Redis** (세션/캐시 관리)

### 보안 & 인증
- **Spring Security**
- **JWT (JSON Web Token)** - 인증 토큰 관리
- **OAuth2** (Authorization Server, Client, Resource Server)
- **io.jsonwebtoken (JJWT 0.11.5)** - JWT 구현

### 이메일 서비스
- **Spring Mail**
- **Gmail SMTP** 연동

### API 문서화
- **SpringDoc OpenAPI (Swagger) 2.2.0**
- Swagger UI를 통한 API 문서 자동 생성

### 개발 도구
- **Lombok** - 보일러플레이트 코드 제거
- **Spring Boot DevTools** - 개발 생산성 향상

### 배포
- **Docker** - 컨테이너화
- **Eclipse Temurin 21** (JDK/JRE)

## 📁 프로젝트 구조

```
src/main/java/com/skhu/gdgocteambuildingproject/
├── admin/              # 관리자 기능
│   ├── api/           # API 인터페이스
│   ├── controller/    # 컨트롤러
│   ├── dto/           # 데이터 전송 객체
│   ├── service/       # 비즈니스 로직
│   └── model/         # 매퍼 및 모델
├── auth/              # 인증 및 권한
│   ├── controller/
│   ├── dto/
│   ├── service/
│   └── domain/
├── Idea/              # 아이디어 관리
│   ├── domain/
│   └── repository/
├── teambuilding/      # 팀 빌딩
│   ├── controller/
│   ├── domain/
│   ├── dto/
│   ├── repository/
│   └── service/
├── projectgallery/    # 프로젝트 갤러리
│   ├── controller/
│   ├── domain/
│   ├── dto/
│   ├── repository/
│   └── service/
├── community/         # 커뮤니티
│   ├── domain/
│   └── repository/
├── user/              # 사용자 관리
│   ├── domain/
│   └── repository/
├── mypage/            # 마이페이지
│   ├── controller/
│   ├── dto/
│   └── service/
└── global/            # 공통 기능
    ├── config/        # 설정
    ├── entity/        # 공통 엔티티
    ├── exception/     # 예외 처리
    ├── jwt/           # JWT 처리
    ├── email/         # 이메일 서비스
    └── swagger/       # API 문서
```

## 🔧 환경 설정

### 필수 환경 변수 (.env)

프로젝트 루트에 `.env` 파일을 생성하고 다음 내용을 설정해야 합니다:

```properties
# 데이터베이스 설정
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/{본인DB주소}?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
SPRING_DATASOURCE_USERNAME={본인DB유저이름}
SPRING_DATASOURCE_PASSWORD={본인DB비밀번호}

# 이메일 설정
SPRING_MAIL_USERNAME={본인구글이메일}
SPRING_MAIL_PASSWORD={본인앱비밀번호}

# JWT 설정
JWT_SECRET={JWT시크릿키}
JWT_ACCESS_EXPIRATION={JWTaccesstoken만료시간(ms단위)}
JWT_REFRESH_EXPIRATION={JWTrefreshtoken만료시간(ms단위)}

# Swagger 설정
SWAGGER_SERVER_LOCAL={로컬서버주소}
SWAGGER_SERVER_PROD={배포서버주소}
```

### 로컬 실행 방법

1. **저장소 클론**
   ```bash
   git clone https://github.com/GDG-on-Campus-SKHU/25-26-GDGoC-Team-Building-System.git
   cd 25-26-GDGoC-Team-Building-System
   ```

2. **환경 변수 설정**
   - `.env.example` 파일을 참고하여 `.env` 파일 생성
   - 필요한 환경 변수 값 입력

3. **MySQL 데이터베이스 준비**
   - MySQL 서버 실행
   - 데이터베이스 생성

4. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```
   또는
   ```bash
   ./gradlew build
   java -jar build/libs/*.jar
   ```

5. **API 문서 확인**
   - 브라우저에서 `http://localhost:8080/swagger-ui.html` 접속

### Docker로 실행

```bash
# Docker 이미지 빌드
docker build -t gdgoc-teambuilding .

# 컨테이너 실행
docker run -p 8080:8080 --env-file .env gdgoc-teambuilding
```

## 🌐 API 엔드포인트

애플리케이션 실행 후 Swagger UI를 통해 모든 API 엔드포인트를 확인할 수 있습니다:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/v3/api-docs`

주요 API 카테고리:
- `/api/auth` - 인증 관련
- `/api/users` - 사용자 관리
- `/api/ideas` - 아이디어 관리
- `/api/teams` - 팀 빌딩
- `/api/projects` - 프로젝트 갤러리
- `/api/community` - 커뮤니티
- `/api/admin` - 관리자 기능

## 🔒 보안 기능

- **JWT 기반 인증**: Access Token과 Refresh Token을 사용한 안전한 인증
- **OAuth2 통합**: 소셜 로그인 지원 가능
- **Spring Security**: 엔드포인트 보안 및 권한 관리
- **이메일 인증**: 회원가입 시 이메일 인증 필수
- **역할 기반 접근 제어**: 사용자 권한에 따른 기능 제한

## 📊 데이터베이스 구조

주요 엔티티:
- **User**: 사용자 정보 (이메일, 이름, 학번, 역할, 파트 등)
- **Idea**: 프로젝트 아이디어 (제목, 주제, 설명, 상태 등)
- **IdeaMember**: 아이디어 팀원 정보
- **IdeaEnrollment**: 아이디어 지원 정보
- **TeamBuildingProject**: 팀 프로젝트 정보
- **GalleryProject**: 프로젝트 갤러리 항목
- **RecruitmentProject**: 프로젝트 모집 게시글

## 🎨 주요 특징

1. **계층형 아키텍처**: Controller - Service - Repository 패턴
2. **RESTful API 설계**: 표준 HTTP 메서드 활용
3. **JPA를 통한 객체 관계 매핑**: 복잡한 도메인 모델 관리
4. **소프트 삭제 지원**: 논리적 삭제를 통한 데이터 보존
5. **페이지네이션**: 대량 데이터 효율적 처리
6. **예외 처리**: 전역 예외 처리기를 통한 일관된 에러 응답
7. **스케줄링**: @EnableScheduling을 통한 배치 작업 지원

## 👥 대상 사용자

- **GDGoC 성공회대학교 멤버**: 프로젝트 아이디어 공유 및 팀 구성
- **관리자**: 멤버 관리, 프로젝트 승인, 활동 관리

## 📝 개발 팀

GDGoC 성공회대학교 4기 멤버들

## 📄 라이선스

프로젝트 라이선스 정보는 별도로 명시되지 않았습니다.

---

더 자세한 정보나 문의사항이 있으시면 프로젝트 관리자에게 연락해주세요.

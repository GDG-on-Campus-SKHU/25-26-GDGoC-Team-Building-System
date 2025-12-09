# 👨‍👩‍👧‍👦 TeamBuildingSystem 

GDGoC (Google Developer Groups on Campus) 성공회대학교 4기 멤버들이 진행하는 **팀빌딩 프로젝트**의 백엔드 레포지토리입니다.

이 시스템은 GDGoC 멤버들이 프로젝트 아이디어를 공유하고, 팀을 구성하며, 협업할 수 있도록 돕는 플랫폼입니다.

## 🎯 주요 기능

- **사용자 관리**: 회원가입, 인증, 권한 관리
- **아이디어 관리**: 프로젝트 아이디어 등록 및 공유
- **팀 빌딩**: 팀 구성 및 멤버 모집
- **프로젝트 갤러리**: 완성된 프로젝트 전시
- **커뮤니티**: 프로젝트 모집 게시판
- **관리자 기능**: 사용자 및 프로젝트 관리

## 🛠 기술 스택

- **Backend**: `Spring Boot 3.3.4`, `Java 21`
- **Database**: `MySQL`, `Redis`
- **Security**: `Spring Security`, `JWT`, `OAuth2`
- **API Documentation**: `Swagger/OpenAPI`
- **Depoly**: `Docker`

## 🔧 개발 환경

- **`Java`**: 21   이상
- **`MySQL`**: 8.0 이상

## 🚀 로컬 환경에서 실행하기

### 1. 레포지토리 클론
- `HTTPS`
```bash
git clone https://github.com/GDG-on-Campus-SKHU/25-26-GDGoC-Team-Building-System.git
```
- `SSH`
``` bash
git@github.com:GDG-on-Campus-SKHU/25-26-GDGoC-Team-Building-System.git
```

### 2. 환경 변수 설정
`.env.example` 파일을 참고하여 루트 디렉토리에 `.env` 파일을 생성하고 필요한 값을 입력합니다.

### 3. MySQL 데이터베이스 준비
MySQL 서버를 실행하고 프로젝트에서 사용할 데이터베이스를 생성합니다.

### 4. 애플리케이션 실행

### 5. API 문서 확인
애플리케이션이 실행되면 브라우저에서 Swagger UI에 접속할 수 있습니다:
```
http://localhost:8080/swagger-ui.html
```

## 📋 필수 환경 변수

프로젝트 실행을 위해 다음 환경 변수들이 필요합니다:

| 변수명 (Variable Name) | 설명 (Description) | 예시 (Example) |
|--------|------|------|
| `SPRING_DATASOURCE_URL` | MySQL 데이터베이스 URL<br>MySQL database URL | `jdbc:mysql://localhost:3306/teambuilding` |
| `SPRING_DATASOURCE_USERNAME` | 데이터베이스 사용자명<br>Database username | `root` |
| `SPRING_DATASOURCE_PASSWORD` | 데이터베이스 비밀번호<br>Database password | `password` |
| `SPRING_MAIL_USERNAME` | Gmail 이메일 주소<br>Gmail email address | `your-email@gmail.com` |
| `SPRING_MAIL_PASSWORD` | Gmail 앱 비밀번호<br>Gmail app password | `your-app-password` |
| `JWT_SECRET` | JWT 시크릿 키<br>JWT secret key | `your-secret-key` |
| `JWT_ACCESS_EXPIRATION` | Access Token 만료 시간 (ms)<br>Access token expiration time (ms) | `3600000` (1시간 / 1 hour) |
| `JWT_REFRESH_EXPIRATION` | Refresh Token 만료 시간 (ms)<br>Refresh token expiration time (ms) | `604800000` (7일 / 7 days) |
| `SWAGGER_SERVER_LOCAL` | 로컬 서버 주소<br>Local server URL | `http://localhost:8080` |
| `SWAGGER_SERVER_PROD` | 배포 서버 주소<br>Production server URL | `https://your-domain.com` |



## 📁 프로젝트 구조

```
├── src/
│   ├── main/
│   │   ├── java/com/skhu/gdgocteambuildingproject/
│   │   │   ├── admin/          # 관리자 기능
│   │   │   ├── auth/           # 인증 및 권한
│   │   │   ├── Idea/           # 아이디어 관리
│   │   │   ├── teambuilding/   # 팀 빌딩
│   │   │   ├── projectgallery/ # 프로젝트 갤러리
│   │   │   ├── community/      # 커뮤니티
│   │   │   ├── user/           # 사용자 관리
│   │   │   ├── mypage/         # 마이페이지
│   │   │   └── global/         # 공통 기능
│   │   └── resources/
│   │       ├── application.yml
│   │       └── static/
│   └── test/
├── build.gradle
├── settings.gradle
├── Dockerfile
└── .env.example
```

## 🤝 기여하기

이 프로젝트는 GDGoC 성공회대학교 4기 멤버들이 함께 개발하고 있습니다.

## 📞 문의

프로젝트에 대한 문의사항이 있으시면 GDGoC 성공회대학교 운영진에게 연락해주세요.

---

**GDGoC SKHU** - Google Developer Groups on Campus, Sungkonghoe University

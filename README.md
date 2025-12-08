# 🚀 TeamBuildingSystem 

GDGoC (Google Developer Groups on Campus) 성공회대학교 4기 멤버들이 진행하는 **팀빌딩 프로젝트**의 백엔드 레포지토리입니다.

이 시스템은 GDGoC 멤버들이 프로젝트 아이디어를 공유하고, 팀을 구성하며, 협업할 수 있도록 돕는 플랫폼입니다.

## 📖 프로젝트 상세 설명

프로젝트의 전체적인 구조, 기능, 기술 스택에 대한 상세한 설명은 **[PROJECT_OVERVIEW.md](./PROJECT_OVERVIEW.md)** 파일을 참고해주세요.

## 🎯 주요 기능

- **👥 사용자 관리**: 회원가입, 인증, 권한 관리
- **💡 아이디어 관리**: 프로젝트 아이디어 등록 및 공유
- **👨‍👩‍👧‍👦 팀 빌딩**: 팀 구성 및 멤버 모집
- **📁 프로젝트 갤러리**: 완성된 프로젝트 전시
- **🏢 커뮤니티**: 프로젝트 모집 게시판
- **🔐 관리자 기능**: 사용자 및 프로젝트 관리

## 🛠 기술 스택

- **Backend**: Spring Boot 3.3.4, Java 21
- **Database**: MySQL, Redis
- **Security**: Spring Security, JWT, OAuth2
- **API Documentation**: Swagger/OpenAPI
- **Build Tool**: Gradle
- **Containerization**: Docker

## 🚀 로컬 환경에서 실행하기

### 1. 레포지토리 클론
```bash
git clone https://github.com/GDG-on-Campus-SKHU/25-26-GDGoC-Team-Building-System.git
cd 25-26-GDGoC-Team-Building-System
```

### 2. 환경 변수 설정
`.env.example` 파일을 참고하여 루트 디렉토리에 `.env` 파일을 생성하고 필요한 값을 입력합니다.

```bash
# .env.example을 복사하여 .env 파일 생성
cp .env.example .env

# .env 파일을 편집하여 실제 값 입력
# (데이터베이스 정보, JWT 시크릿, 이메일 설정 등)
```

### 3. MySQL 데이터베이스 준비
MySQL 서버를 실행하고 프로젝트에서 사용할 데이터베이스를 생성합니다.

```sql
CREATE DATABASE your_database_name CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 4. 애플리케이션 실행

**Gradle을 사용한 실행:**
```bash
./gradlew bootRun
```

**또는 빌드 후 실행:**
```bash
./gradlew build
java -jar build/libs/*.jar
```

### 5. API 문서 확인
애플리케이션이 실행되면 브라우저에서 Swagger UI에 접속할 수 있습니다:
```
http://localhost:8080/swagger-ui.html
```

## 🐳 Docker로 실행하기

```bash
# Docker 이미지 빌드
docker build -t gdgoc-teambuilding .

# 컨테이너 실행 (환경 변수 파일 사용)
docker run -p 8080:8080 --env-file .env gdgoc-teambuilding
```

## 📋 필수 환경 변수

프로젝트 실행을 위해 다음 환경 변수들이 필요합니다:

| 변수명 | 설명 | 예시 |
|--------|------|------|
| `SPRING_DATASOURCE_URL` | MySQL 데이터베이스 URL | `jdbc:mysql://localhost:3306/teambuilding` |
| `SPRING_DATASOURCE_USERNAME` | 데이터베이스 사용자명 | `root` |
| `SPRING_DATASOURCE_PASSWORD` | 데이터베이스 비밀번호 | `password` |
| `SPRING_MAIL_USERNAME` | Gmail 이메일 주소 | `your-email@gmail.com` |
| `SPRING_MAIL_PASSWORD` | Gmail 앱 비밀번호 | `your-app-password` |
| `JWT_SECRET` | JWT 시크릿 키 | `your-secret-key` |
| `JWT_ACCESS_EXPIRATION` | Access Token 만료 시간 (ms) | `3600000` (1시간) |
| `JWT_REFRESH_EXPIRATION` | Refresh Token 만료 시간 (ms) | `604800000` (7일) |
| `SWAGGER_SERVER_LOCAL` | 로컬 서버 주소 | `http://localhost:8080` |
| `SWAGGER_SERVER_PROD` | 배포 서버 주소 | `https://your-domain.com` |

## 🔧 개발 환경

- **Java**: 21 이상
- **MySQL**: 8.0 이상
- **Redis**: (선택사항)
- **Gradle**: 프로젝트에 포함됨 (Gradle Wrapper 사용)

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

package com.skhu.gdgocteambuildingproject.user.domain.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechStackType {
    ABLETON_LIVE("Ableton Live"),
    ACTIVITYPUB("ActivityPub"),
    ACTIX_WEB("Actix Web"),
    ADONISJS("AdonisJS"),
    ADOBE_AFTER_EFFECTS("Adobe After Effects"),
    AISCRIPT("AIScript"),
    ALPINE_JS("Alpine.js"),
    ANACONDA("Anaconda"),
    ANDROID_STUDIO("Android Studio"),
    ANGULAR("Angular"),
    ANSIBLE("Ansible"),
    APOLLO_GRAPHQL("Apollo GraphQL"),
    APPWRITE("Appwrite"),
    ARCH_LINUX("Arch Linux"),
    ARDUINO("Arduino"),
    ASTRO("Astro"),
    ATOM("Atom"),
    AUTOCAD("AutoCAD"),
    AWS("AWS"),
    AZURE("Microsoft Azure"),

    BABEL("Babel"),
    BASH("Bash"),
    BITBUCKET("Bitbucket"),
    BLENDER("Blender"),
    BOOTSTRAP("Bootstrap"),

    C("C"),
    CPP("C++"),
    CSHARP("C#"),
    CLION("CLion"),
    CLOJURE("Clojure"),
    CLOUDFLARE("Cloudflare"),
    CMAKE("CMake"),
    CODEPEN("CodePen"),
    COFFEESCRIPT("CoffeeScript"),
    CSS("CSS"),
    CYPRESS("Cypress"),

    DART("Dart"),
    DEBIAN("Debian"),
    DENO("Deno"),
    DEV_TO("Dev.to"),
    DISCORD("Discord"),
    DJANGO("Django"),
    DOCKER("Docker"),
    DOTNET(".NET"),
    DYNAMODB("DynamoDB"),

    ECLIPSE("Eclipse"),
    ELASTICSEARCH("Elasticsearch"),
    ELECTRON_JS("Electron.js"),
    ELIXIR("Elixir"),
    EXPRESS_JS("Express.js"),

    FASTAPI("FastAPI"),
    FIGMA("Figma"),
    FIREBASE("Firebase"),
    FLASK("Flask"),
    FLUTTER("Flutter"),

    GATSBY("Gatsby"),
    GCP("Google Cloud Platform"),
    GIT("Git"),
    GITHUB("GitHub"),
    GITLAB("GitLab"),
    GO("Go (Golang)"),
    GRAPHQL("GraphQL"),
    HASKELL("Haskell"),
    HEROKU("Heroku"),
    HTML("HTML"),

    JAVA("Java"),
    JAVASCRIPT("JavaScript"),
    JENKINS("Jenkins"),
    JEST("Jest"),
    JQUERY("jQuery"),

    KAFKA("Apache Kafka"),
    KALI_LINUX("Kali Linux"),
    KOTLIN("Kotlin"),
    KUBERNETES("Kubernetes"),

    LARAVEL("Laravel"),
    LINUX("Linux"),
    LUA("Lua"),

    MONGODB("MongoDB"),
    MYSQL("MySQL"),

    NESTJS("NestJS"),
    NEXT_JS("Next.js"),
    NGINX("NGINX"),
    NODE_JS("Node.js"),
    NPM("npm"),
    NUXT_JS("Nuxt.js"),

    PHP("PHP"),
    POSTGRESQL("PostgreSQL"),
    POSTMAN("Postman"),
    POWERSHELL("PowerShell"),
    PRISMA("Prisma"),
    PYTHON("Python"),
    PYTORCH("PyTorch"),

    RAILS("Ruby on Rails"),
    REACT("React.js"),
    REDIS("Redis"),
    REDUX("Redux"),
    REMIX("Remix"),
    RUST("Rust"),

    SASS("SASS"),
    SPRING_BOOT("Spring Boot"),
    SQLITE("SQLite"),
    STYLED_COMPONENTS("Styled Components"),
    SUPABASE("Supabase"),
    SVELTE("Svelte"),
    SWIFT("Swift"),

    TAILWIND_CSS("Tailwind CSS"),
    TERRAFORM("Terraform"),
    THREE_JS("Three.js"),
    TYPESCRIPT("TypeScript"),

    UBUNTU("Ubuntu"),
    UNITY("Unity"),
    UNREAL_ENGINE("Unreal Engine"),

    VERCEL("Vercel"),
    VIM("Vim"),
    VITE("Vite"),
    VS_CODE("VS Code"),
    VUE_JS("Vue.js"),

    WEBPACK("Webpack"),
    WORDPRESS("WordPress"),

    YARN("Yarn"),

    ZIG("Zig");

    private final String displayName;
}

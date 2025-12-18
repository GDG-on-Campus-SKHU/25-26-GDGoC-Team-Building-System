package com.skhu.gdgocteambuildingproject.user.domain.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechStackType {
    ABLETON_LIVE("Ableton Live", "abletonlive"),
    ACTIVITYPUB("ActivityPub", "activitypub"),
    ACTIX_WEB("Actix Web", "actix"),                    // override
    ADONISJS("AdonisJS", "adonisjs"),
    ADOBE_AFTER_EFFECTS("Adobe After Effects", "adobeaftereffects"),
    ALPINE_JS("Alpine.js", "alpinedotjs"),              // override
    ANACONDA("Anaconda", "anaconda"),
    ANDROID_STUDIO("Android Studio", "androidstudio"),
    ANGULAR("Angular", "angular"),
    ANSIBLE("Ansible", "ansible"),
    APOLLO_GRAPHQL("Apollo GraphQL", "apollographql"),
    APPWRITE("Appwrite", "appwrite"),
    ARCH_LINUX("Arch Linux", "archlinux"),
    ARDUINO("Arduino", "arduino"),
    ASTRO("Astro", "astro"),
    ATOM("Atom", "atom"),
    AUTOCAD("AutoCAD", "autocad"),
    AWS("AWS", "amazonwebservices"),                    // override
    AZURE("Microsoft Azure", "microsoftazure"),

    BABEL("Babel", "babel"),
    BASH("Bash", "gnubash"),
    BITBUCKET("Bitbucket", "bitbucket"),
    BLENDER("Blender", "blender"),
    BOOTSTRAP("Bootstrap", "bootstrap"),

    C("C", "c"),
    CPP("C++", "cplusplus"),                            // override
    CSHARP("C#", "csharp"),                             // override
    CLION("CLion", "clion"),
    CLOJURE("Clojure", "clojure"),
    CLOUDFLARE("Cloudflare", "cloudflare"),
    CMAKE("CMake", "cmake"),
    CODEPEN("CodePen", "codepen"),
    COFFEESCRIPT("CoffeeScript", "coffeescript"),
    CSS("CSS", "css3"),                                 // override
    CYPRESS("Cypress", "cypress"),

    DART("Dart", "dart"),
    DEBIAN("Debian", "debian"),
    DENO("Deno", "deno"),
    DEV_TO("Dev.to", "devdotto"),                       // override
    DISCORD("Discord", "discord"),
    DJANGO("Django", "django"),
    DOCKER("Docker", "docker"),
    DOTNET(".NET", "dotnet"),                           // override
    DYNAMODB("DynamoDB", "amazondynamodb"),             // override

    ECLIPSE("Eclipse", "eclipseide"),
    ELASTICSEARCH("Elasticsearch", "elasticsearch"),
    ELECTRON_JS("Electron.js", "electron"),             // override
    ELIXIR("Elixir", "elixir"),
    EXPRESS_JS("Express.js", "express"),                // override

    FASTAPI("FastAPI", "fastapi"),
    FIGMA("Figma", "figma"),
    FIREBASE("Firebase", "firebase"),
    FLASK("Flask", "flask"),
    FLUTTER("Flutter", "flutter"),

    GATSBY("Gatsby", "gatsby"),
    GCP("Google Cloud Platform", "googlecloud"),        // override
    GIT("Git", "git"),
    GITHUB("GitHub", "github"),
    GITLAB("GitLab", "gitlab"),
    GO("Go", "go"),
    GRAPHQL("GraphQL", "graphql"),
    HASKELL("Haskell", "haskell"),
    HEROKU("Heroku", "heroku"),
    HTML("HTML", "html5"),

    JAVA("Java", "oracle"),
    JAVASCRIPT("JavaScript", "javascript"),
    JENKINS("Jenkins", "jenkins"),
    JEST("Jest", "jest"),
    JQUERY("jQuery", "jquery"),

    KAFKA("Apache Kafka", "apachekafka"),
    KALI_LINUX("Kali Linux", "kalilinux"),
    KOTLIN("Kotlin", "kotlin"),
    KUBERNETES("Kubernetes", "kubernetes"),

    LARAVEL("Laravel", "laravel"),
    LINUX("Linux", "linux"),
    LUA("Lua", "lua"),

    MONGODB("MongoDB", "mongodb"),
    MYSQL("MySQL", "mysql"),

    NESTJS("NestJS", "nestjs"),
    NEXT_JS("Next.js", "nextdotjs"),                    // override
    NGINX("NGINX", "nginx"),
    NODE_JS("Node.js", "nodedotjs"),                    // override
    NPM("npm", "npm"),
    NUXT_JS("Nuxt.js", "nuxtdotjs"),                    // override

    PHP("PHP", "php"),
    POSTGRESQL("PostgreSQL", "postgresql"),
    POSTMAN("Postman", "postman"),
    PRISMA("Prisma", "prisma"),
    PYTHON("Python", "python"),
    PYTORCH("PyTorch", "pytorch"),

    RAILS("Ruby on Rails", "rubyonrails"),
    REACT("React.js", "react"),                         // override
    REDIS("Redis", "redis"),
    REDUX("Redux", "redux"),
    REMIX("Remix", "remix"),
    RUST("Rust", "rust"),

    SASS("SASS", "sass"),
    SPRING_BOOT("Spring Boot", "springboot"),
    SQLITE("SQLite", "sqlite"),
    STYLED_COMPONENTS("Styled Components", "styledcomponents"),
    SUPABASE("Supabase", "supabase"),
    SVELTE("Svelte", "svelte"),
    SWIFT("Swift", "swift"),

    TAILWIND_CSS("Tailwind CSS", "tailwindcss"),
    TERRAFORM("Terraform", "terraform"),
    THREE_JS("Three.js", "threedotjs"),                 // override
    TYPESCRIPT("TypeScript", "typescript"),

    UBUNTU("Ubuntu", "ubuntu"),
    UNITY("Unity", "unity"),
    UNREAL_ENGINE("Unreal Engine", "unrealengine"),

    VERCEL("Vercel", "vercel"),
    VIM("Vim", "vim"),
    VITE("Vite", "vite"),
    VS_CODE("VS Code", "visualstudiocode"),
    VUE_JS("Vue.js", "vuedotjs"),                       // override

    WEBPACK("Webpack", "webpack"),
    WORDPRESS("WordPress", "wordpress"),

    YARN("Yarn", "yarn"),

    ZIG("Zig", "zig");

    private final String displayName;
    private final String iconSlug;

    private static final String SIMPLE_ICONS_BASE_URL = "https://cdn.simpleicons.org/";

    public String getTechStackIconUrl() {
        return switch (this) {
            case ABLETON_LIVE -> "/icons/techstackicons/abletonlive_icon.svg";
            case ADOBE_AFTER_EFFECTS -> "/icons/techstackicons/adobeaftereffects_icon.svg";
            case ATOM -> "/icons/techstackicons/atom_icon.svg";
            case AWS -> "/icons/techstackicons/aws_icon.svg";
            case AZURE -> "/icons/techstackicons/azure_icon.svg";
            case CODEPEN -> "/icons/techstackicons/codepen_icon.svg";
            case CSHARP -> "/icons/techstackicons/csharp_icon.svg";
            case CSS -> "/icons/techstackicons/css3_icon.svg";
            case DYNAMODB -> "/icons/techstackicons/dynamodb_icon.svg";
            case HEROKU -> "/icons/techstackicons/heroku_icon.svg";
            case JAVA -> "/icons/techstackicons/java_icon.svg";
            case NUXT_JS -> "/icons/techstackicons/nuxtdotjs_icon.svg";
            case UNITY -> "/icons/techstackicons/unity_icon.svg";
            case VS_CODE -> "/icons/techstackicons/visualstudiocode_icon.svg";

            default -> SIMPLE_ICONS_BASE_URL + getIconSlug();
        };
    }
}

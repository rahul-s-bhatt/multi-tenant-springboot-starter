# 🚀 multi-tenant-springboot-starter

**Version:** `v0.1.0-SNAPSHOT`
**Status:** Developer Preview
**Author:** [Rahul S. Bhatt](https://github.com/rahul-s-bhatt)

A plug-and-play Spring Boot starter for building **multi-tenant SaaS applications**, with support for **subdomain-based** tenant resolution, **tenant registry**, and **dynamic datasource isolation**.

---

## 📆 Installation (via JitPack)

> Add this to your `pom.xml` or `build.gradle.kts` to include the starter in your project.

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
<groupId>com.github.rahul-s-bhatt</groupId>
<artifactId>multi-tenant-springboot-starter</artifactId>
<version>v0.1.0-SNAPSHOT</version>
</dependency>
```

### Gradle

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.rahul-s-bhatt:multi-tenant-springboot-starter:v0.1.0-SNAPSHOT")
}
```

---

## ✅ Features

* ✅ **Tenant isolation strategy**: per-database (others coming soon)
* ✅ **Subdomain-based resolution**: `acme.localhost:8080`
* ✅ **Tenant Registry**: auto-load tenant metadata at startup
* ⚙️ **Annotation-based activation** via `@EnableTenantIsolation (coming soon)
* 🧠 **Spring Boot auto-configuration** with zero boilerplate
* 🛡️ Safe, thread-isolated `TenantContextHolder`
* 🌱 Extendable: plug in header-based/JWT-based resolvers easily

---

## 🧹 Usage

### 1. Create your Spring Boot app

```java
@SpringBootApplication
public class MyApp {}
```

### 2. 📦 Add It via JitPack
Maven 
```
<dependency>
  <groupId>com.github.rahul-s-bhatt</groupId>
  <artifactId>multi-tenant-springboot-starter</artifactId>
  <version>v0.1.0-SNAPSHOT</version>
</dependency>
```

Gradle
```
dependencies {
  implementation("com.github.rahul-s-bhatt:multi-tenant-springboot-starter:v0.1.0-SNAPSHOT")
}
```

### 3. Use sample `application.yml`

```yaml
debug: true

management:
  endpoints:
    web:
      exposure:
        include: multitenancy-status

multi-tenancy:
  actuator-enabled: true

  default-tenant-id: "acme"
  registry:
    type: IN_MEMORY
    in-memory-tenants:
      - tenantId: acme
        datasourceUrl: jdbc:h2:mem:acme
        username: sa
        password:
      - tenantId: globex
        datasourceUrl: jdbc:h2:mem:globex
        username: sa
        password:

  isolation:
    type: TENANT_PER_DATABASE
    datasource-template:
      driver-class-name: org.h2.Driver
      max-pool-size: 5
      min-idle: 1
      idle-timeout: 300000
      max-lifetime: 600000

  resolution:
    enabled: true
    type: HTTP_HEADER
    sub-domain:
      base-domain: localhost
```

---

## 📆 Modules Overview

| Module                | Description                                        |
| --------------------- | -------------------------------------------------- |
| `starter/`            | The Spring Boot auto-configured core library       |
| `demo/`               | Sample Spring Boot app with in-memory tenant DBs   |
| `scripts/`            | (Optional) Shell utilities, CI, or bootstrap files |
| `ego-logs/`, `daily/` | Build artifacts, logs (for local dev only)         |

---

## 🧪 Demo Usage

Run the demo app locally:

```bash
cd demo
./mvnw spring-boot:run
```

Open different tenants in browser:

* `http://acme.localhost:8080`
* `http://globex.localhost:8080`

> You might need to map these to `127.0.0.1` in your `/etc/hosts`.

---

## 🧠 Roadmap

| Feature                              | Status        |
| ------------------------------------ | ------------- |
| Subdomain-based resolution           | ✅ Completed   |
| Header-based resolution              | ✅ Completed |
| JWT claim resolution                 | 🚧 Planned |
| Tenant-per-schema strategy           | 🚧 Planned    |
| Tenant onboarding at runtime         | 🚧 Planned    |
| Liquibase / Flyway integration       | 🚧 Planned    |
| Spring Security multi-tenant support | 🚧 Planned    |

---

## 🔧 Extending Resolvers

You can plug in your custom resolver by implementing:

```java
public interface TenantResolver {
    Optional<String> resolveTenant(HttpServletRequest request);
}
```

---

## 🧑‍💼 Contributing

1. Fork the repo
2. Create your feature branch (`git checkout -b feature/foo`)
3. Commit your changes with proper test coverage
4. Open a PR and describe the change clearly

---

## 📄 License

MIT License. See `LICENSE` file.

---

## 🔗 Useful Links

* 🔗 GitHub: [multi-tenant-springboot-starter](https://github.com/rahul-s-bhatt/multi-tenant-springboot-starter)
* 🔗 JitPack: [jitpack.io/#rahul-s-bhatt/multi-tenant-springboot-starter](https://jitpack.io/#rahul-s-bhatt/multi-tenant-springboot-starter)

---

## ✨ Speciality

This repository was born from personal motivation and resilience:

* 🔥 **EGO Logs**: Inspired by the anime *Blue Lock*, a folder named `ego-logs/` exists to track daily engineering discipline, breakthroughs, and failures — documenting the journey like a true striker!
* 💸 **Fuel from Setback**: This starter was sparked right after a demotivating 0.5 LPA raise — a pushback that ignited the fire to create something developers could be proud to use and learn from.

---

## 🤭 Maintainer

Made with ❤️ by [Rahul S. Bhatt](https://github.com/rahul-s-bhatt)

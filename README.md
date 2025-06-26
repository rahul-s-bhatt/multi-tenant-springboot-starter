# Multi-Tenant Spring Boot Starter

> **Plug-and-play multitenancy support for Spring Boot apps using per-database isolation**

![License](https://img.shields.io/badge/license-MIT-green)
![Spring Boot](https://img.shields.io/badge/spring--boot-3.1+-blue)

---

## 🚀 Overview

This starter simplifies the creation of multitenant Spring Boot applications by providing:

* ✅ Tenant-per-database isolation strategy
* ✅ Registry support via in-memory YAML or a central JDBC source
* ✅ Subdomain-based tenant resolution
* ✅ Thread-safe tenant context propagation
* ✅ Drop-in `DataSource` routing

Built with extensibility in mind — ready for schema, row-level, or microservice-aware expansions.

---

## 📦 Installation

### Using JitPack

1. Add the JitPack repository to your project:

<details>
<summary>Gradle</summary>

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}
```

</details>

<details>
<summary>Maven</summary>

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

</details>

2. Add the dependency:

```kotlin
dependencies {
    implementation("com.github.rahul-s-bhatt:multi-tenant-springboot-starter:0.1.0-SNAPSHOT")
}
```

---

## ⚙️ Quick Start

### `application.yml`

```yaml
multi-tenancy:
  registry:
    type: IN_MEMORY
    in-memory-tenants:
      acme:
        tenantId: acme
        datasourceUrl: jdbc:h2:mem:acme
        username: sa
        password:
      globex:
        tenantId: globex
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
    tenant-identifier-resolver: SUB_DOMAIN
    sub-domain:
      base-domain: localhost
```

### Example Request

* Visit `http://acme.localhost:8080/api/foo`
* Tenant `acme` is resolved from subdomain
* A dedicated H2 datasource is connected behind the scenes

---

## 🧩 Supported Strategies

| Category   | Strategy              | Enum Value            |
| ---------- | --------------------- | --------------------- |
| Registry   | In-memory YAML        | `IN_MEMORY`           |
|            | Central JDBC DB       | `JDBC`                |
| Isolation  | Per-database          | `TENANT_PER_DATABASE` |
| Resolution | Subdomain             | `SUB_DOMAIN`          |
|            | HTTP header (planned) | `HTTP_HEADER`         |

---

## 🧱 Architecture

* `MultiTenantRegistry` – resolves tenant metadata
* `TenantContextHolder` – thread-local tenant scope
* `TenantRoutingDataSource` – lazy-creates datasource per tenant
* `SubdomainTenantResolverFilter` – sets current tenant from request
* `MultiTenancyProperties` – central config model for YAML

---

## 🧪 Test Setup (if you want to test locally)

```bash
git clone https://github.com/rahul-s-bhatt/multi-tenant-springboot-starter.git
cd multi-tenant-springboot-starter
demo/gradlew bootRun
```

Visit: `http://acme.localhost:8080` → should route to acme datasource

> Tip: Use `/etc/hosts` to simulate subdomains locally:
>
> ```
> 127.0.0.1 acme.localhost globex.localhost
> ```

---

## 🔭 Roadmap

* [x] In-memory + JDBC tenant registries
* [x] Per-database isolation
* [x] Subdomain-based tenant resolution
* [ ] Header-based resolution strategy
* [ ] Schema and row-level isolation
* [ ] Runtime tenant registration API
* [ ] Liquibase / Flyway integration per tenant
* [ ] Micronaut & Quarkus compatibility

---

## 🤝 Contributing

1. Fork this repo
2. Create a feature branch (`feat/isolation-schema`)
3. Open a PR with context and use-case

---

## 📝 License

Licensed under the [MIT License](https://opensource.org/licenses/MIT).

---

## 🧠 Credits

Crafted with precision by [Rahul S. Bhatt](https://github.com/rahul-s-bhatt) – open source, always. ✨

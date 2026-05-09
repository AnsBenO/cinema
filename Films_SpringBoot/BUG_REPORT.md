# Bug Report — Films SpringBoot Application

**Date:** 2026-05-06  
**Reviewed by:** Axet Plugin (Senior Java Developer)  
**Scope:** `src/test/java/ntt/beca/films/`, `src/main/java/ntt/beca/films/auth/`

---

## Summary Table

| #   | File                         | Severity  | Description                                                 |
| --- | ---------------------------- | --------- | ----------------------------------------------------------- |
| 1   | `FilmsApplicationTests.java` | 🟡 Medium | No test DB isolation — will fail without a running database |
| 2   | `FilmsApplicationTests.java` | 🟢 Low    | Empty `contextLoads()` test — no assertions                 |

---

## Detailed Findings

---

### Bug 1 — No test database isolation

**File:** `src/test/java/ntt/beca/films/FilmsApplicationTests.java`  
**Severity:** 🟡 Medium

**Code:**

```java
@SpringBootTest(classes = FilmsApplication.class)
class FilmsApplicationTests {
    @Test
    void contextLoads() {
    }
}
```

**Problem:**  
`@SpringBootTest` without `@TestPropertySource` or a dedicated `application-test.yml` will load the real `application.yml` configuration, including the production/development database connection. The test will fail in any CI/CD environment or machine that does not have the database running and properly configured.

**Fix:**

```java
@SpringBootTest(classes = FilmsApplication.class)
@TestPropertySource(locations = "classpath:application-test.yml")
// OR use an in-memory H2 database:
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FilmsApplicationTests {
    ...
}
```

Create `src/test/resources/application-test.yml` with an H2 in-memory datasource:

```yaml
spring:
    datasource:
        url: jdbc:h2:mem:testdb
        driver-class-name: org.h2.Driver
    jpa:
        hibernate:
            ddl-auto: create-drop
```

---

### Bug 2 — Empty test method with no assertions

**File:** `src/test/java/ntt/beca/films/FilmsApplicationTests.java`  
**Severity:** 🟢 Low

**Code:**

```java
@Test
void contextLoads() {
    // empty — no assertions
}
```

**Problem:**  
The test passes trivially — it only verifies that the Spring context starts (and only if the DB is reachable, see Bug 1). No beans, services, or configurations are verified. Misconfigured beans will not be caught by this test.

**Fix:**  
Add meaningful assertions, or replace with targeted slice tests:

```java
@SpringBootTest(classes = FilmsApplication.class)
class FilmsApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context);
        assertNotNull(context.getBean(AuthenticationService.class));
        assertNotNull(context.getBean(JwtService.class));
    }
}
```

---

## Severity Legend

| Icon | Level  | Meaning                                                                           |
| ---- | ------ | --------------------------------------------------------------------------------- |
| 🔴   | High   | Can cause runtime exceptions, data corruption, or security vulnerabilities        |
| 🟡   | Medium | Causes incorrect behavior, broken functionality, or environment-specific failures |
| 🟢   | Low    | Code smell, bad practice, or maintainability concern                              |

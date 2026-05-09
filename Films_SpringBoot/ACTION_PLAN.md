# Action Plan — Films SpringBoot Bug Fixes

**Generated from:** `BUG_REPORT.md`  
**Date:** 2026-05-06  
**Owner:** Developer

---

## Execution Order (Priority-based)

Fix high-severity bugs first, then medium, then low.

---

## Phase 1 — 🔴 Critical Fixes (Do First)

### Task 1 · Fix always-true null check in `register()`

- **File:** `src/main/java/ntt/beca/films/auth/AuthenticationService.java`
- **Ref:** Bug #3
- **Status:** `[ ] TODO`

**Steps:**

1. Open `AuthenticationService.java`
2. In the `register()` method, remove the `if (user != null)` guard block
3. Remove the trailing `return null;` dead code
4. Keep the body of the `if` block as the direct method body
5. Run `mvn test` to verify no regressions

---

### Task 2 · Add duplicate email check before `userRepository.save()`

- **File:** `src/main/java/ntt/beca/films/auth/AuthenticationService.java`
- **Ref:** Bug #4
- **Status:** `[ ] TODO`

**Steps:**

1. At the top of `register()`, before building the `UserEntity`, add:
    ```java
    if (userRepository.existsUserEntityByEmail(request.email())) {
        throw new IllegalArgumentException("Email is already registered: " + request.email());
    }
    ```
2. (Optional but recommended) Create a custom exception (e.g., `EmailAlreadyExistsException`) and map it to HTTP 409 via `@ExceptionHandler` or `@ControllerAdvice`
3. Test manually: attempt to register with an already-existing email and confirm a 409 (or 400) is returned instead of 500

---

## Phase 2 — 🟡 Medium Fixes

### Task 3 · Fix JWT `"name"` claim — store username, not email

- **File:** `src/main/java/ntt/beca/films/auth/AuthenticationService.java`
- **Ref:** Bug #5
- **Status:** `[ ] TODO`

**Steps:**

1. Locate the `generateExtraClaims()` method
2. Change `user.getEmail()` to `user.getUsername()` for the `"name"` key:
    ```java
    extraClaims.put("name", user.getUsername());
    ```
3. Verify any frontend/API client reading the `"name"` JWT claim now receives the username

---

### Task 4 · Fix test DB isolation

- **File:** `src/test/java/ntt/beca/films/FilmsApplicationTests.java`
- **Ref:** Bug #1
- **Status:** `[ ] TODO`

**Steps:**

1. Add H2 dependency to `pom.xml` (test scope):
    ```xml
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>test</scope>
    </dependency>
    ```
2. Create `src/test/resources/application-test.yml`:
    ```yaml
    spring:
        datasource:
            url: jdbc:h2:mem:testdb
            driver-class-name: org.h2.Driver
        jpa:
            hibernate:
                ddl-auto: create-drop
    ```
3. Annotate `FilmsApplicationTests` with `@TestPropertySource(locations = "classpath:application-test.yml")` or `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)`
4. Run `mvn test` without a running database and confirm tests pass

---

### Task 5 · Externalize hardcoded CORS origin

- **File:** `src/main/java/ntt/beca/films/auth/SecurityConfig.java`  
  **Also touches:** `src/main/resources/application.yml`
- **Ref:** Bug #7
- **Status:** `[ ] TODO`

**Steps:**

1. Add to `application.yml`:
    ```yaml
    app:
        cors:
            allowed-origins: http://localhost:4200
    ```
2. In `SecurityConfig.java`, inject the value:
    ```java
    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;
    ```
3. Replace `List.of("http://localhost:4200")` with `List.of(allowedOrigins)`
4. For multiple origins, change the property to a comma-separated list and inject as `List<String>` using `@Value("${app.cors.allowed-origins}") List<String> allowedOrigins`
5. Verify CORS still works locally; set the correct value for staging/production environments

---

### Task 6 · Add missing HTTP method matchers in SecurityConfig

- **File:** `src/main/java/ntt/beca/films/auth/SecurityConfig.java`
- **Ref:** Bug #8
- **Status:** `[ ] TODO`

**Steps:**

1. After the existing `POST` matcher, add:
    ```java
    authConfig.requestMatchers(HttpMethod.PUT, "/api/**").authenticated();
    authConfig.requestMatchers(HttpMethod.DELETE, "/api/**").authenticated();
    authConfig.requestMatchers(HttpMethod.PATCH, "/api/**").authenticated();
    ```
2. Test with an HTTP client (curl/Postman) that `PUT /api/...` and `DELETE /api/...` return 401 (unauthenticated) or 200 (authenticated) — not 403 Forbidden

---

## Phase 3 — 🟢 Low / Code Quality Fixes

### Task 7 · Inject `PasswordEncoder` interface instead of `BCryptPasswordEncoder`

- **File:** `src/main/java/ntt/beca/films/auth/AuthenticationService.java`
- **Ref:** Bug #6
- **Status:** `[ ] TODO`

**Steps:**

1. Change the field declaration from:
    ```java
    private final BCryptPasswordEncoder encoder;
    ```
    to:
    ```java
    private final PasswordEncoder encoder;
    ```
2. Update the constructor parameter type accordingly
3. Confirm the Spring context still resolves the bean correctly

---

### Task 8 · Add meaningful assertions to `contextLoads()` test

- **File:** `src/test/java/ntt/beca/films/FilmsApplicationTests.java`
- **Ref:** Bug #2
- **Status:** `[ ] TODO`

**Steps:**

1. Inject `ApplicationContext` via `@Autowired`
2. Add `assertNotNull(context)` as a baseline assertion
3. Add spot-checks for critical beans, e.g.:
    ```java
    assertNotNull(context.getBean(AuthenticationService.class));
    assertNotNull(context.getBean(JwtService.class));
    ```
4. Run `mvn test` and confirm the test passes and provides meaningful output

---

## Completion Checklist

| Task                                | Bug Ref | Severity  | File(s)                                  | Done  |
| ----------------------------------- | ------- | --------- | ---------------------------------------- | ----- |
| 1 — Remove always-true null check   | #3      | 🔴 High   | `AuthenticationService.java`             | `[ ]` |
| 2 — Add duplicate email guard       | #4      | 🔴 High   | `AuthenticationService.java`             | `[ ]` |
| 3 — Fix JWT `"name"` claim          | #5      | 🟡 Medium | `AuthenticationService.java`             | `[ ]` |
| 4 — Fix test DB isolation           | #1      | 🟡 Medium | `FilmsApplicationTests.java`, `pom.xml`  | `[ ]` |
| 5 — Externalize CORS origin         | #7      | 🟡 Medium | `SecurityConfig.java`, `application.yml` | `[ ]` |
| 6 — Add PUT/DELETE/PATCH matchers   | #8      | 🟡 Medium | `SecurityConfig.java`                    | `[ ]` |
| 7 — Use `PasswordEncoder` interface | #6      | 🟢 Low    | `AuthenticationService.java`             | `[ ]` |
| 8 — Add test assertions             | #2      | 🟢 Low    | `FilmsApplicationTests.java`             | `[ ]` |

---

## Verification Steps (After All Fixes)

1. `mvn clean test` — all tests pass without a running database
2. Register with a duplicate email — confirm HTTP 409 (not 500)
3. Decode a JWT token — confirm `"name"` field contains username (not email)
4. Send `PUT /api/films/1` with a valid token — confirm HTTP 200 (not 403)
5. Deploy to a non-local environment — confirm CORS origin is read from config

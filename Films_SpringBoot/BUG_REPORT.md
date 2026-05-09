# Bug Report — Films SpringBoot Application

**Date:** 2026-05-06  
**Reviewed by:** Axet Plugin (Senior Java Developer)  
**Scope:** `src/test/java/ntt/beca/films/`, `src/main/java/ntt/beca/films/auth/`

---

## Summary Table

| #   | File                         | Severity  | Description                                                                              |
| --- | ---------------------------- | --------- | ---------------------------------------------------------------------------------------- |
| 1   | `FilmsApplicationTests.java` | 🟡 Medium | No test DB isolation — will fail without a running database                              |
| 2   | `FilmsApplicationTests.java` | 🟢 Low    | Empty `contextLoads()` test — no assertions                                              |
| 3   | `AuthenticationService.java` | 🔴 High   | Always-true null check + unreachable `return null` in `register()`                       |
| 4   | `AuthenticationService.java` | 🔴 High   | Missing duplicate email check before `userRepository.save()`                             |
| 5   | `AuthenticationService.java` | 🟡 Medium | JWT `"name"` claim stores email instead of username                                      |
| 6   | `AuthenticationService.java` | 🟢 Low    | `BCryptPasswordEncoder` injected by concrete type instead of `PasswordEncoder` interface |
| 7   | `SecurityConfig.java`        | 🟡 Medium | Hardcoded CORS origin `localhost:4200` — breaks in production                            |
| 8   | `SecurityConfig.java`        | 🟡 Medium | PUT/DELETE/PATCH on `/api/**` silently denied by `anyRequest().denyAll()`                |

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

### Bug 3 — Always-true null check and unreachable `return null` in `register()`

**File:** `src/main/java/ntt/beca/films/auth/AuthenticationService.java`  
**Severity:** 🔴 High

**Code:**

```java
public AuthenticationResponse register(RegisterUserDto request) {
    UserEntity user = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(Role.CUSTOMER)
                .build();
    if (user != null) {       // ← Builder NEVER returns null — always true
        userRepository.save(user);
        String token = jwtService.generateToken(user, generateExtraClaims(user));
        return new AuthenticationResponse(token, new CurrentUserDto(user.getUsername(), user.getEmail()));
    }
    return null;               // ← Unreachable dead code
}
```

**Problem:**  
`UserEntity.builder().build()` can never return `null`. The `if (user != null)` guard is permanently `true`, making it dead code. The final `return null` is unreachable. However, if this code were ever refactored and a `null` were somehow returned, the caller would receive `null` and throw a `NullPointerException` with no meaningful error message.

**Fix:**

```java
public AuthenticationResponse register(RegisterUserDto request) {
    UserEntity user = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(Role.CUSTOMER)
                .build();

    userRepository.save(user);
    String token = jwtService.generateToken(user, generateExtraClaims(user));
    return new AuthenticationResponse(token, new CurrentUserDto(user.getUsername(), user.getEmail()));
}
```

---

### Bug 4 — No duplicate email check before saving in `register()`

**File:** `src/main/java/ntt/beca/films/auth/AuthenticationService.java`  
**Severity:** 🔴 High

**Code:**

```java
public AuthenticationResponse register(RegisterUserDto request) {
    // ← No check for existing email
    UserEntity user = UserEntity.builder()...build();
    userRepository.save(user);  // ← Throws DataIntegrityViolationException if email already exists
    ...
}
```

**Problem:**  
`UserRepository` already exposes `existsUserEntityByEmail(String email)`, but it is never called in `register()`. When a user tries to register with an email that already exists in the database, Spring will throw an unhandled `DataIntegrityViolationException` (DB constraint violation), resulting in a 500 Internal Server Error instead of a meaningful 400/409 response.

**Fix:**

```java
public AuthenticationResponse register(RegisterUserDto request) {
    if (userRepository.existsUserEntityByEmail(request.email())) {
        throw new IllegalArgumentException("Email is already registered: " + request.email());
        // or a custom exception mapped to HTTP 409 Conflict
    }

    UserEntity user = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(Role.CUSTOMER)
                .build();

    userRepository.save(user);
    String token = jwtService.generateToken(user, generateExtraClaims(user));
    return new AuthenticationResponse(token, new CurrentUserDto(user.getUsername(), user.getEmail()));
}
```

---

### Bug 5 — JWT `"name"` claim stores email instead of username

**File:** `src/main/java/ntt/beca/films/auth/AuthenticationService.java`  
**Severity:** 🟡 Medium

**Code:**

```java
private Map<String, Object> generateExtraClaims(UserEntity user) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("name", user.getEmail()); // ← Stores email under "name" key
    extraClaims.put("role", user.getRole().name());
    return extraClaims;
}
```

**Problem:**  
The claim key `"name"` conventionally stores the user's display name (per JWT/OIDC standards — RFC 7519). Instead, the email address is being stored under `"name"`. Any JWT consumer (frontend, API client) reading the `"name"` claim will receive the email address, not the actual username. This causes identity data confusion.

**Fix:**

```java
private Map<String, Object> generateExtraClaims(UserEntity user) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("name", user.getUsername()); // ← Correct: store the display name
    extraClaims.put("role", user.getRole().name());
    return extraClaims;
}
```

---

### Bug 6 — Concrete `BCryptPasswordEncoder` injected instead of `PasswordEncoder` interface

**File:** `src/main/java/ntt/beca/films/auth/AuthenticationService.java`  
**Severity:** 🟢 Low

**Code:**

```java
// In AuthenticationService.java
private final BCryptPasswordEncoder encoder; // ← Concrete type

// In SecurityConfig.java
@Bean
PasswordEncoder passwordEncoder() {          // ← Bean declared as interface type
    return new BCryptPasswordEncoder();
}
```

**Problem:**  
The `@Bean` method declares the return type as `PasswordEncoder` (the interface), but `AuthenticationService` injects the concrete type `BCryptPasswordEncoder`. While Spring can resolve this at runtime via actual type inspection, it violates the Dependency Inversion Principle and creates tight coupling to the implementation. If the password encoder is ever swapped (e.g., to `Argon2PasswordEncoder`), `AuthenticationService` will break.

**Fix:**

```java
// Change in AuthenticationService.java
private final PasswordEncoder encoder; // ← Use the interface
```

---

### Bug 7 — Hardcoded CORS origin `localhost:4200`

**File:** `src/main/java/ntt/beca/films/auth/SecurityConfig.java`  
**Severity:** 🟡 Medium

**Code:**

```java
@Bean
CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:4200")); // ← Hardcoded
    ...
}
```

**Problem:**  
The allowed CORS origin is hardcoded to `http://localhost:4200`. This will silently block all legitimate cross-origin requests from staging or production frontends. There is no way to configure this without modifying and redeploying the source code.

**Fix:**  
Externalize to `application.yml`:

```yaml
# application.yml
app:
    cors:
        allowed-origins: http://localhost:4200
```

```java
// SecurityConfig.java
@Value("${app.cors.allowed-origins}")
private String allowedOrigins;

@Bean
CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(allowedOrigins));
    ...
}
```

---

### Bug 8 — PUT/DELETE/PATCH requests silently denied on `/api/**`

**File:** `src/main/java/ntt/beca/films/auth/SecurityConfig.java`  
**Severity:** 🟡 Medium

**Code:**

```java
authConfig.requestMatchers(HttpMethod.GET, "/api/**").permitAll();
authConfig.requestMatchers(HttpMethod.POST, "/api/**").authenticated();
authConfig.anyRequest().denyAll(); // ← Catches PUT, DELETE, PATCH on /api/**
```

**Problem:**  
Only `GET` and `POST` methods are explicitly configured for `/api/**`. Any `PUT`, `DELETE`, or `PATCH` request to `/api/**` falls through to `anyRequest().denyAll()` and returns HTTP 403 Forbidden with no explanation. If the REST API needs to support update or delete operations, those requests will silently fail.

**Fix:**  
Add explicit matchers for all required HTTP methods:

```java
authConfig.requestMatchers(HttpMethod.GET, "/api/**").permitAll();
authConfig.requestMatchers(HttpMethod.POST, "/api/**").authenticated();
authConfig.requestMatchers(HttpMethod.PUT, "/api/**").authenticated();
authConfig.requestMatchers(HttpMethod.DELETE, "/api/**").authenticated();
authConfig.requestMatchers(HttpMethod.PATCH, "/api/**").authenticated();
authConfig.anyRequest().denyAll();
```

---

## Severity Legend

| Icon | Level  | Meaning                                                                           |
| ---- | ------ | --------------------------------------------------------------------------------- |
| 🔴   | High   | Can cause runtime exceptions, data corruption, or security vulnerabilities        |
| 🟡   | Medium | Causes incorrect behavior, broken functionality, or environment-specific failures |
| 🟢   | Low    | Code smell, bad practice, or maintainability concern                              |

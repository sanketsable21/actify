================================================================
                         ACTIFY
              Task Management REST API — README
================================================================

A production-grade Spring Boot REST API with JWT authentication
and Role-Based Access Control (RBAC) for managing users, roles,
and tasks across three role tiers: Admin, Manager, and User.


================================================================
                     PROJECT VERSIONS
================================================================

  Java (JDK)                 : 21.0.11 (Oracle JDK)
  Spring Boot                : 4.1.1
  Spring Framework           : 7.0.9
  Spring Security            : 7.x (bundled with Boot 4.1.1)
  Spring Data JPA            : 4.x (bundled with Boot 4.1.1)
  Hibernate ORM              : 7.4.5.Final
  MySQL Server               : 8.0.40
  MySQL Connector/J          : 9.7.0
  Apache Maven               : 3.9.16
  Apache Tomcat (embedded)   : 11.0.24
  JJWT (JWT library)         : 0.12.6
  Lombok                     : bundled with Spring Boot 4.1.1
  IDE                        : Eclipse with Spring Tools 4


  ** COMPATIBILITY NOTE **
  Spring Boot 4.1.1 requires Java 21+ and embeds Tomcat 11.
  External Tomcat 10.1.x is NOT supported — the embedded
  Tomcat 11 is used instead.


================================================================
                        OVERVIEW
================================================================

Actify is a backend service built to demonstrate secure,
scalable REST API design using modern Spring Boot practices.

Supported features:

  - User registration and login with JWT tokens
  - Passwords hashed with BCrypt (never stored in plain text)
  - Role-based access control across three tiers
  - Task assignment workflow (Manager -> User)
  - Clean JSON error responses with proper HTTP status codes
  - Input validation (email format, password strength)


================================================================
                       TECH STACK
================================================================

  Layer               Technology              Version
  ------------------  ----------------------  ---------------
  Language            Java (Oracle JDK)       21.0.11
  Framework           Spring Boot             4.1.1
  Core Framework      Spring Framework        7.0.9
  Security            Spring Security         7.x
  Persistence         Spring Data JPA         4.x
  ORM                 Hibernate ORM           7.4.5.Final
  Build Tool          Apache Maven            3.9.16
  Database            MySQL Server            8.0.40
  JDBC Driver         MySQL Connector/J       9.7.0
  JWT Library         JJWT                    0.12.6
  Servlet Container   Apache Tomcat (embed)   11.0.24
  Validation          Jakarta Bean Validation 3.x
  Boilerplate         Lombok                  Boot-managed
  IDE                 Eclipse + STS4          Latest


================================================================
                       FEATURES
================================================================

  AUTHENTICATION
  ------------------------------------------------------------
  - User registration with auto-assigned USER role
  - Login with email + BCrypt-verified password
  - JWT token returned on success (24-hour expiry)
  - Stateless session (no server-side session storage)

  USER & ROLE MANAGEMENT
  ------------------------------------------------------------
  - Users can hold multiple roles (Many-to-Many)
  - Admin can assign any role to any user
  - Roles stored separately with unique names
  - Passwords never returned in any API response

  TASK ASSIGNMENT
  ------------------------------------------------------------
  - Managers can assign tasks to specific users
  - Users can only see tasks assigned to them
  - Managers can view all tasks and per-user tasks

  ROLE-BASED ACCESS CONTROL
  ------------------------------------------------------------
  - Admin   : full user CRUD + role assignment
  - Manager : view users, assign/delete tasks
  - User    : view own profile and own tasks only
  - All endpoints (except register/login) require a valid JWT

  ERROR HANDLING
  ------------------------------------------------------------
  - Global exception handler returns clean JSON
  - HTTP status codes: 400, 401, 403, 404, 409, 500
  - Field-level validation errors returned as a map


================================================================
                     ARCHITECTURE
================================================================

  +-------------------------------+
  |     Client (Postman / App)    |
  +---------------+---------------+
                  | HTTP + JWT
                  v
  +-------------------------------+
  |  JwtAuthenticationFilter      |
  |  (reads Bearer token)         |
  +---------------+---------------+
                  v
  +-------------------------------+
  |  SecurityConfig               |
  |  (URL rules + role checks)    |
  +---------------+---------------+
                  v
  +-------------------------------+
  |  Controllers:                 |
  |  Auth / Admin / Manager / User|
  +---------------+---------------+
                  v
  +-------------------------------+
  |  Services:                    |
  |  UserService, TaskService     |
  +---------------+---------------+
                  v
  +-------------------------------+
  |  Repositories (JPA)           |
  |  User, Role, Task             |
  +---------------+---------------+
                  v
  +-------------------------------+
  |  MySQL Database               |
  +-------------------------------+


================================================================
                    DATABASE SCHEMA
================================================================

  TABLE: roles
  ------------------------------------------------------------
    id      BIGINT        PRIMARY KEY, AUTO_INCREMENT
    name    VARCHAR(255)  UNIQUE, NOT NULL
                          Values: ADMIN, MANAGER, USER


  TABLE: users
  ------------------------------------------------------------
    id        BIGINT        PRIMARY KEY, AUTO_INCREMENT
    name      VARCHAR(255)  NOT NULL
    email     VARCHAR(255)  UNIQUE, NOT NULL
    password  VARCHAR(255)  BCrypt hash, NOT NULL


  TABLE: tasks
  ------------------------------------------------------------
    id           BIGINT         PRIMARY KEY, AUTO_INCREMENT
    title        VARCHAR(255)   NOT NULL
    description  VARCHAR(1000)  NULLABLE
    assigned_to  BIGINT         FOREIGN KEY -> users(id)


  TABLE: user_roles  (junction table)
  ------------------------------------------------------------
    user_id   BIGINT  FOREIGN KEY -> users(id)
    role_id   BIGINT  FOREIGN KEY -> roles(id)
                      PRIMARY KEY = (user_id, role_id)


================================================================
                  SETUP & INSTALLATION
================================================================

  PREREQUISITES
  ------------------------------------------------------------
    - Java 21 (JDK)
    - Maven 3.9+
    - MySQL 8+
    - Git (optional)
    - IDE: Eclipse with Spring Tools 4, or IntelliJ IDEA


  STEP 1 — CLONE OR COPY THE PROJECT
  ------------------------------------------------------------
    git clone <your-repo-url>
    cd actify


  STEP 2 — CREATE THE MYSQL DATABASE
  ------------------------------------------------------------
    CREATE DATABASE actify_db;


  STEP 3 — CONFIGURE DATABASE CREDENTIALS
  ------------------------------------------------------------
    Edit src/main/resources/application.properties:

    spring.datasource.url=jdbc:mysql://localhost:3306/actify_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    spring.datasource.username=root
    spring.datasource.password=YOUR_MYSQL_PASSWORD


  STEP 4 — BUILD THE PROJECT
  ------------------------------------------------------------
    mvn clean install -DskipTests


================================================================
                     CONFIGURATION
================================================================

  FULL application.properties
  ------------------------------------------------------------

    # ===== Server =====
    server.port=8080

    # ===== MySQL =====
    spring.datasource.url=jdbc:mysql://localhost:3306/actify_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    spring.datasource.username=root
    spring.datasource.password=YOUR_MYSQL_PASSWORD
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

    # ===== JPA / Hibernate =====
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.open-in-view=false

    # ===== JWT =====
    jwt.secret=ThisIsMyVerySecretKeyForActifyJwtTokenDoNotShareIt1234567890
    jwt.expiration=86400000

  PRODUCTION NOTE
  ------------------------------------------------------------
    In a real deployment, store jwt.secret and DB credentials
    in environment variables or a secrets manager, not in
    source control.


================================================================
                RUNNING THE APPLICATION
================================================================

  FROM ECLIPSE
  ------------------------------------------------------------
    1. Right-click ActifyApplication.java
    2. Run As -> Spring Boot App
    3. Wait for console output:
         Tomcat started on port 8080 (http)
         Started ActifyApplication in X.XXX seconds
         >>> Data initialization complete.

  FROM MAVEN
  ------------------------------------------------------------
    mvn spring-boot:run

  FROM JAR
  ------------------------------------------------------------
    java -jar target/actify-0.0.1-SNAPSHOT.jar

  Base URL: http://localhost:8080


================================================================
                     API ENDPOINTS
================================================================

  AUTH  (Public — no token needed)
  ------------------------------------------------------------
    POST  /api/auth/register   Register new user (USER role)
    POST  /api/auth/login      Login, returns JWT token


  ADMIN  (requires ROLE_ADMIN)
  ------------------------------------------------------------
    GET     /api/admin/users              List all users
    GET     /api/admin/users/{id}         Get user by ID
    POST    /api/admin/users              Create user
    PUT     /api/admin/users/{id}         Update user
    DELETE  /api/admin/users/{id}         Delete user
    POST    /api/admin/assign-role        Assign role to user


  MANAGER  (requires ROLE_MANAGER)
  ------------------------------------------------------------
    GET     /api/manager/users              View all users
    GET     /api/manager/users/{id}/tasks   View user's tasks
    GET     /api/manager/tasks              View all tasks
    POST    /api/manager/tasks              Assign task to user
    DELETE  /api/manager/tasks/{id}         Delete task


  USER  (any authenticated user)
  ------------------------------------------------------------
    GET     /api/user/profile   View own profile
    GET     /api/user/tasks     View own assigned tasks


================================================================
                 ROLE-BASED ACCESS CONTROL
================================================================

  Endpoint Prefix    Required Role       Behavior
  -----------------  ------------------  -----------------
  /api/auth/**       None                Public
  /api/admin/**      ROLE_ADMIN          403 if not Admin
  /api/manager/**    ROLE_MANAGER        403 if not Manager
  /api/user/**       Any authenticated   401/403 if no token

  IMPORTANT
  ------------------------------------------------------------
    hasRole("ADMIN") internally checks for the authority
    ROLE_ADMIN, which is set by CustomUserDetailsService
    using: new SimpleGrantedAuthority("ROLE_" + name).


================================================================
                      SAMPLE USERS
================================================================

  Seeded on first startup by DataInitializer:

    Email                 Password       Roles
    --------------------  -------------  ----------------
    admin@actify.com      Admin@123      ADMIN, USER
    manager@actify.com    Manager@123    MANAGER, USER
    user@actify.com       User@123       USER

  Seeding is idempotent — running the app multiple times
  will not create duplicates.


================================================================
              ERROR HANDLING & VALIDATION
================================================================

  VALIDATION RULES
  ------------------------------------------------------------
    name      @NotBlank
    email     @NotBlank, @Email
    password  @NotBlank, @Size(min=8)


  ERROR RESPONSE FORMAT
  ------------------------------------------------------------
    {
      "timestamp": "2026-09-12T11:21:20.0327218",
      "status": 400,
      "error": "Validation Failed",
      "message": "One or more fields have errors",
      "path": "/api/auth/register",
      "validationErrors": {
        "name": "Name is required",
        "email": "Email must be a valid format",
        "password": "Password must be at least 8 characters"
      }
    }


  HTTP STATUS CODES
  ------------------------------------------------------------
    200  Successful read/update
    201  Resource created
    204  Successful delete
    400  Validation failure
    401  Wrong credentials / invalid token
    403  Authenticated but insufficient role
    404  Resource not found
    409  Duplicate email
    500  Unexpected server error


================================================================
                 TESTING WITH POSTMAN
================================================================

  STEP 1 — LOGIN
  ------------------------------------------------------------
    POST http://localhost:8080/api/auth/login
    Content-Type: application/json

    {
      "email": "admin@actify.com",
      "password": "Admin@123"
    }

    Response -> save the "token" value.


  STEP 2 — CALL A PROTECTED ENDPOINT
  ------------------------------------------------------------
    GET http://localhost:8080/api/admin/users
    Authorization: Bearer <PASTE_TOKEN_HERE>


  KEY TESTS TO RUN
  ------------------------------------------------------------
    #   Test                                     Expected
    --  ---------------------------------------  ----------
    1   Admin   -> /api/admin/users               200 OK
    2   Manager -> /api/admin/users               403
    3   Manager -> /api/manager/users             200 OK
    4   Admin   -> /api/manager/users             403
    5   User    -> /api/user/profile              200 OK
    6   No token -> /api/admin/users              401/403
    7   Duplicate email -> /api/auth/register     409
    8   Bad email -> /api/auth/register           400


================================================================
                    PROJECT STRUCTURE
================================================================

  actify/
  |
  +-- pom.xml
  +-- README.md
  +-- mvnw
  +-- mvnw.cmd
  |
  +-- src/
      |
      +-- main/
      |   |
      |   +-- java/com/actify/
      |   |   |
      |   |   +-- ActifyApplication.java
      |   |   |
      |   |   +-- config/
      |   |   |   +-- DataInitializer.java
      |   |   |
      |   |   +-- controller/
      |   |   |   +-- AuthController.java
      |   |   |   +-- AdminController.java
      |   |   |   +-- ManagerController.java
      |   |   |   +-- UserController.java
      |   |   |
      |   |   +-- dto/
      |   |   |   +-- AssignRoleRequest.java
      |   |   |   +-- AuthResponse.java
      |   |   |   +-- ErrorResponse.java
      |   |   |   +-- LoginRequest.java
      |   |   |   +-- RegisterRequest.java
      |   |   |   +-- TaskRequest.java
      |   |   |   +-- TaskResponse.java
      |   |   |   +-- UserResponse.java
      |   |   |
      |   |   +-- entity/
      |   |   |   +-- Role.java
      |   |   |   +-- Task.java
      |   |   |   +-- User.java
      |   |   |
      |   |   +-- exception/
      |   |   |   +-- DuplicateResourceException.java
      |   |   |   +-- GlobalExceptionHandler.java
      |   |   |   +-- ResourceNotFoundException.java
      |   |   |
      |   |   +-- repository/
      |   |   |   +-- RoleRepository.java
      |   |   |   +-- TaskRepository.java
      |   |   |   +-- UserRepository.java
      |   |   |
      |   |   +-- security/
      |   |   |   +-- CustomUserDetailsService.java
      |   |   |   +-- JwtAuthenticationFilter.java
      |   |   |   +-- JwtUtil.java
      |   |   |   +-- SecurityConfig.java
      |   |   |
      |   |   +-- service/
      |   |       +-- TaskService.java
      |   |       +-- UserService.java
      |   |
      |   +-- resources/
      |       +-- application.properties
      |
      +-- test/
          +-- java/com/actify/
              +-- ActifyApplicationTests.java


================================================================
                    SECURITY NOTES
================================================================

    - Passwords   : BCrypt hashed with random salt on save
    - JWT         : HMAC-SHA256 signed; secret must be 32+ chars
    - Token life  : 24 hours (configurable)
    - Sessions    : Stateless — no HTTP sessions used
    - Endpoints   : Locked by role at URL prefix level
    - Leakage     : Passwords never serialized in responses


================================================================
                  FUTURE ENHANCEMENTS
================================================================

    [ ] Swagger / OpenAPI documentation
    [ ] JUnit + Mockito test coverage
    [ ] JSON body for 401/403 responses
    [ ] Refresh tokens
    [ ] Pagination for list endpoints
    [ ] Docker + docker-compose
    [ ] GitHub Actions CI pipeline
    [ ] Audit logging (who did what, when)


================================================================
                       AUTHOR
================================================================

    Your Name
    Backend Developer
    Project: Actify Task Management API


================================================================
                       LICENSE
================================================================

    This project is created for educational / demonstration
    purposes.


================================================================
   Built with Spring Boot 4.1.1 · Java 21 · MySQL 8 · JWT
================================================================
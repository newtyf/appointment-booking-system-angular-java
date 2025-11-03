# Appointment Booking System - Backend (Spring Boot 3)

## Project Setup

Este documento proporciona instrucciones para configurar y ejecutar el backend del Appointment Booking System con Spring Boot 3 y Java 17.

### Pre-requisitos

Asegúrate de tener instalado en tu sistema:

- Java 17 o superior (JDK)
- Maven 3.6+ (incluido en el wrapper)
- MySQL Server 8.0+

### Instrucciones de Configuración

1. **Clona el repositorio**

   ```bash
   git clone <repository-url>
   cd appointment-booking-system-angular-java/backend
   ```

2. **Configura las variables de entorno**

   Configura las siguientes variables de entorno o actualiza `application.yml`:

   ```bash
   export MYSQL_DB_HOST=localhost
   export MYSQL_DB_PORT=3306
   export MYSQL_DB_NAME=appointments_db
   export MYSQL_DB_USER=your_user
   export MYSQL_DB_PASSWORD=your_password
   export SECRET_KEY=your-secret-key-for-jwt
   export CULQI_SECRET_KEY=your-culqi-secret-key
   export EMAIL_SENDER=your-email@gmail.com
   export EMAIL_PASSWORD=your-app-password
   ```

3. **Ejecuta el servidor de base de datos MySQL**

   Asegúrate de que MySQL esté corriendo y que la base de datos `appointments_db` exista con las tablas necesarias.

4. **Compila el proyecto**

   ```bash
   ./mvnw clean install
   ```

5. **Ejecuta la aplicación**

   ```bash
   ./mvnw spring-boot:run
   ```

   O si prefieres usar Maven instalado globalmente:

   ```bash
   mvn spring-boot:run
   ```

   La aplicación estará disponible por defecto en `http://localhost:8080`.

6. **Health Check**

   Verifica que la aplicación esté corriendo visitando:

   ```
   http://localhost:8080/health
   ```

### Estructura del Proyecto

La estructura del backend Spring Boot es la siguiente:

```
backend/
    README.md                     # Documentación del backend
    pom.xml                       # Configuración de Maven y dependencias
    src/
        main/
            java/com/monarca/appointments/
                AppointmentsApplication.java    # Clase principal Spring Boot
                controller/                     # REST Controllers
                    AuthController.java
                    AppointmentController.java
                    UserController.java
                    ServiceController.java
                service/                        # Lógica de negocio
                    AuthService.java
                    AppointmentService.java
                    UserService.java
                    ServiceService.java
                repository/                     # Spring Data JPA Repositories
                    UserRepository.java
                    AppointmentRepository.java
                    ServiceRepository.java
                model/                          # Entidades JPA
                    User.java
                    Appointment.java
                    Service.java
                dto/                            # DTOs request/response
                    LoginRequest.java
                    RegisterRequest.java
                    AppointmentCreateRequest.java
                security/                       # Configuración de seguridad
                    SecurityConfig.java
                    JwtUtil.java
                    JwtAuthenticationFilter.java
                exception/                      # Manejo de excepciones
                    GlobalExceptionHandler.java
            resources/
                application.yml                 # Configuración de la aplicación
            __init__.py
            base.py            # Declaración base de modelos
            session.py         # Sesión y conexión a la base de datos
        models/                # Modelos ORM
            __init__.py
            user.py            # Modelo de usuario (ahora con roles)
        schemas/               # Esquemas Pydantic (serialización/validación)
            auth.py            # Esquemas de autenticación (login, token)
            user.py            # Esquema de usuario
        services/              # Lógica de negocio y servicios
            __init__.py
            auth_service.py    # Servicio de autenticación (login, registro, JWT)
            role_service.py    # Servicio para validación de roles
            user_service.py    # Servicio relacionado a usuarios
```

### Documentación

Utilizamos SWAGGER para documentar todos los endpoints de la API:

```
http://127.0.0.1:8000/docs
```

### Notas Adicionales

- Usa el archivo `requirements.txt` para gestionar las dependencias.
- El archivo `.env` está ignorado por Git por razones de seguridad. No lo compartas públicamente.
- Para cualquier inconveniente, revisa los logs o contacta al responsable del proyecto.
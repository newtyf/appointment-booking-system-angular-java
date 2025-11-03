# Monarca Appointment Booking System

Sistema completo de reserva de citas para el salón de belleza Monarca, desarrollado con **Spring Boot 3** (Java 21) en el backend y **Angular 18** en el frontend.

## 🚀 Stack Tecnológico

### Backend
- **Spring Boot 3.2.1** - Framework de aplicación Java
- **Java 21** - Lenguaje de programación
- **Spring Data JPA** - Acceso a datos con Hibernate
- **Spring Security** - Autenticación y autorización con JWT
- **MySQL 8.0** - Base de datos relacional
- **Maven** - Gestión de dependencias

### Frontend
- **Angular 18** - Framework web moderno
- **TypeScript** - Lenguaje de programación tipado
- **Tailwind CSS 3.4** - Framework de estilos
- **RxJS** - Programación reactiva
- **HttpClient** - Comunicación con API REST

### Integraciones
- **Culqi** - Procesamiento de pagos
- **Replicate AI** - Edición de imágenes con IA
- **JavaMailSender** - Envío de correos electrónicos

## 📋 Características Principales

### Sistema de Citas
- ✅ Soporte para clientes walk-in (sin registro) y clientes registrados
- ✅ Validación de horario de trabajo (8 AM - 8 PM)
- ✅ Verificación de disponibilidad de estilistas (sin conflictos)
- ✅ No permitir citas en el pasado
- ✅ Estados de cita: pending, confirmed, completed, cancelled

### Control de Acceso Basado en Roles (RBAC)
- **Admin**: Gestión completa del sistema
- **Receptionist**: Gestión de citas y clientes
- **Stylist**: Visualización de citas propias
- **Client**: Reserva y gestión de citas propias

### Seguridad
- Autenticación JWT con tokens Bearer
- Encriptación de contraseñas con BCrypt
- Autorización basada en roles con @PreAuthorize
- Interceptors para manejo de tokens y errores

## 🏗️ Estructura del Proyecto

```
appointment-booking-system-angular-java/
├── backend/                         # Spring Boot 3 + Java 21
│   ├── src/main/java/com/monarca/appointments/
│   │   ├── controller/              # REST Controllers
│   │   ├── service/                 # Lógica de negocio
│   │   ├── repository/              # Spring Data JPA
│   │   ├── model/                   # Entidades JPA
│   │   ├── dto/                     # DTOs request/response
│   │   ├── security/                # JWT, SecurityConfig
│   │   └── exception/               # Manejo de excepciones
│   ├── src/main/resources/
│   │   ├── application.yml          # Configuración
│   │   └── static/                  # Angular build (producción)
│   └── pom.xml                      # Dependencias Maven
└── frontend/                        # Angular 18
    ├── src/app/
    │   ├── core/                    # Services, guards, interceptors
    │   ├── features/                # Páginas por rol
    │   └── shared/                  # Componentes compartidos
    ├── package.json
    ├── angular.json
    └── tailwind.config.js
```

## 🚀 Inicio Rápido

### Pre-requisitos
- Java 21 (JDK)
- Node.js 20+
- MySQL 8.0+
- Maven 3.9+ (opcional, se incluye wrapper)

### 1. Configurar Base de Datos

```bash
# Iniciar MySQL (con Docker)
docker run --name mysql-monarca -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=appointments_db -p 3306:3306 -d mysql:8.0

# O crear manualmente la base de datos
mysql -u root -p
CREATE DATABASE appointments_db;
```

### 2. Configurar Backend

```bash
cd backend

# Configurar variables de entorno
export MYSQL_DB_HOST=localhost
export MYSQL_DB_PORT=3306
export MYSQL_DB_NAME=appointments_db
export MYSQL_DB_USER=root
export MYSQL_DB_PASSWORD=password
export SECRET_KEY=your-secret-key-min-32-chars
export CULQI_SECRET_KEY=sk_test_your_key
export EMAIL_SENDER=your-email@gmail.com
export EMAIL_PASSWORD=your-app-password

# Compilar y ejecutar
./mvnw spring-boot:run
```

El backend estará disponible en `http://localhost:8080`

### 3. Configurar Frontend

```bash
cd frontend

# Instalar dependencias
npm install --legacy-peer-deps

# Ejecutar en modo desarrollo
ng serve
```

El frontend estará disponible en `http://localhost:4200`

## 📡 API Endpoints

### Autenticación
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar nuevo usuario

### Citas
- `GET /api/appointments` - Listar todas las citas (Admin/Receptionist)
- `POST /api/appointments` - Crear nueva cita
- `GET /api/appointments/{id}` - Obtener cita por ID
- `PUT /api/appointments/{id}` - Actualizar cita
- `DELETE /api/appointments/{id}` - Eliminar cita
- `GET /api/appointments/client/{clientId}` - Citas de un cliente
- `GET /api/appointments/stylist/{stylistId}` - Citas de un estilista

### Usuarios
- `GET /api/users` - Listar usuarios (Admin/Receptionist)
- `GET /api/users/{id}` - Obtener usuario por ID
- `GET /api/users/role/{role}` - Listar usuarios por rol

### Servicios
- `GET /api/services` - Listar servicios
- `POST /api/services` - Crear servicio (Admin)
- `PUT /api/services/{id}` - Actualizar servicio (Admin)
- `DELETE /api/services/{id}` - Eliminar servicio (Admin)

### Health Check
- `GET /health` - Estado de la aplicación

## 🐳 Docker

### Construcción de Imagen

```bash
docker build -t monarca-appointments .
```

### Ejecución con Docker Compose

```bash
docker-compose up -d
```

## 📝 Variables de Entorno

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `MYSQL_DB_HOST` | Host de MySQL | `localhost` |
| `MYSQL_DB_PORT` | Puerto de MySQL | `3306` |
| `MYSQL_DB_NAME` | Nombre de la base de datos | `appointments_db` |
| `MYSQL_DB_USER` | Usuario de MySQL | `root` |
| `MYSQL_DB_PASSWORD` | Contraseña de MySQL | `password` |
| `SECRET_KEY` | Clave secreta para JWT | `min-32-caracteres` |
| `ACCESS_TOKEN_EXPIRE_MINUTES` | Expiración del token JWT | `1440` |
| `CULQI_SECRET_KEY` | Clave secreta de Culqi | `sk_test_xxx` |
| `CULQI_API_URL` | URL de la API de Culqi | `https://api.culqi.com/v2` |
| `EMAIL_SENDER` | Email del remitente | `sender@gmail.com` |
| `EMAIL_PASSWORD` | Contraseña del email | `app-password` |
| `SMTP_SERVER` | Servidor SMTP | `smtp.gmail.com` |
| `SMTP_PORT` | Puerto SMTP | `587` |
| `REPLICATE_API_TOKEN` | Token de Replicate | `r8_xxx` |

## 🧪 Testing

### Backend
```bash
cd backend
./mvnw test
```

### Frontend
```bash
cd frontend
ng test
```

## 📦 Build para Producción

### Backend + Frontend (Docker)
```bash
docker build -t monarca-appointments .
docker run -p 8080:8080 --env-file .env monarca-appointments
```

### Frontend Standalone
```bash
cd frontend
npm run build
# Los archivos estarán en dist/monarca-frontend/browser/
```

### Backend Standalone
```bash
cd backend
./mvnw clean package -DskipTests
# El JAR estará en target/appointments-1.0.0.jar
java -jar target/appointments-1.0.0.jar
```

## 🔐 Seguridad

- Contraseñas hasheadas con BCrypt
- Tokens JWT con expiración configurable
- Protección CSRF deshabilitada (API REST)
- CORS configurado para orígenes permitidos
- Validación de entrada en DTOs
- Autorización basada en roles en endpoints

## 📄 Licencia

Este proyecto es privado y pertenece a Monarca Beauty Salon.

## 👥 Equipo de Desarrollo

- Backend: Spring Boot 3 + Java 21
- Frontend: Angular 18 + Tailwind CSS
- Base de Datos: MySQL 8.0

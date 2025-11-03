# 💇 Sistema de Reserva de Citas - Monarca Salón de Belleza

Sistema completo de gestión y reserva de citas para salones de belleza, desarrollado con FastAPI, React y MySQL. Incluye autenticación basada en roles, procesamiento de pagos con Culqi, y edición de imágenes con IA.

![Version](https://img.shields.io/badge/version-0.0.1-blue.svg)
![Python](https://img.shields.io/badge/python-3.12-blue.svg)
![React](https://img.shields.io/badge/react-19.1-blue.svg)
![FastAPI](https://img.shields.io/badge/FastAPI-0.115-green.svg)
![License](https://img.shields.io/badge/license-Private-red.svg)

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
  - [Backend](#backend)
  - [Frontend](#frontend)
  - [Docker](#docker)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [Roles de Usuario](#-roles-de-usuario)
- [API Documentation](#-api-documentation)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Contribuir](#-contribuir)
- [Licencia](#-licencia)

## ✨ Características

### Funcionalidades Principales

- **Gestión de Citas**
  - Reserva de citas online con verificación de disponibilidad en tiempo real
  - Soporte para clientes registrados y walk-in (sin registro)
  - Sistema de recordatorios automáticos por email
  - Visualización de calendario con disponibilidad de estilistas

- **Sistema de Roles y Autenticación**
  - 4 roles: Admin, Recepcionista, Estilista y Cliente
  - Autenticación JWT con tokens de acceso
  - Control de acceso basado en roles (RBAC)
  - Registro y login de usuarios

- **Gestión de Servicios**
  - Catálogo de servicios con precios y duración
  - Administración de servicios por administradores
  - Visualización de servicios disponibles para clientes

- **Procesamiento de Pagos**
  - Integración con Culqi para pagos en línea
  - Soporte para pagos en PEN (Soles Peruanos)
  - Confirmación de pago automática

- **Edición de Imágenes con IA**
  - Visualización de estilos de cabello con Replicate AI
  - Prueba virtual de peinados antes de la cita

- **Dashboard y Reportes**
  - Panel administrativo con métricas de negocio
  - Estadísticas de citas, ingresos y servicios
  - Vista personalizada por rol

- **Sistema de Notificaciones**
  - Notificaciones por email para confirmación de citas
  - Recordatorios automáticos antes de las citas
  - Alertas de cambios de estado

## 🛠️ Tecnologías

### Backend
- **FastAPI** - Framework web moderno y rápido para Python
- **SQLAlchemy 2.0** - ORM con soporte async
- **aiomysql** - Driver asíncrono para MySQL
- **Alembic** - Migraciones de base de datos
- **Pydantic** - Validación de datos y settings
- **Python-Jose** - Tokens JWT
- **Bcrypt** - Hash de contraseñas
- **HTTPX** - Cliente HTTP asíncrono para APIs externas
- **Replicate** - SDK para edición de imágenes con IA

### Frontend
- **React 19** - Biblioteca de UI
- **Vite** - Herramienta de desarrollo y build
- **Tailwind CSS v4** - Framework CSS utility-first
- **React Router v7** - Navegación y rutas
- **Axios** - Cliente HTTP
- **Framer Motion** - Animaciones
- **Lucide React** - Iconos

### Base de Datos
- **MySQL** - Sistema de gestión de base de datos relacional

### Integraciones Externas
- **Culqi** - Procesamiento de pagos
- **Replicate** - Edición de imágenes con IA
- **Gmail SMTP** - Envío de emails

## 🏗️ Arquitectura

Este proyecto sigue una arquitectura de aplicación fullstack con separación clara entre backend y frontend:

```
┌─────────────────┐
│   React SPA     │ ← Frontend (Puerto 5173 en dev)
│   (Vite)        │
└────────┬────────┘
         │ HTTP/REST
         ↓
┌─────────────────┐
│   FastAPI       │ ← Backend API (Puerto 8000)
│   (Async)       │
└────────┬────────┘
         │
    ┌────┴────┬──────────┬──────────┐
    ↓         ↓          ↓          ↓
┌────────┐ ┌──────┐ ┌─────────┐ ┌──────────┐
│ MySQL  │ │ Culqi│ │Replicate│ │Gmail SMTP│
└────────┘ └──────┘ └─────────┘ └──────────┘
```

### Patrón de Arquitectura Backend

- **Dependency Injection** - Servicios inyectados vía FastAPI Depends()
- **Service Layer** - Lógica de negocio en capa de servicios
- **Repository Pattern** - Acceso a datos a través de SQLAlchemy ORM
- **Async/Await** - Operaciones asíncronas en toda la aplicación
- **RBAC** - Control de acceso basado en roles

### Flujo de Deployment

En producción, el frontend se construye y se sirve desde el backend:

```
1. Build Frontend → dist/
2. Copy dist/ → backend/app/static/
3. FastAPI sirve static/ y SPA routing
```

## 📦 Requisitos Previos

Asegúrate de tener instalado:

- **Python 3.12+**
- **Node.js 20+** y npm
- **MySQL 8.0+**
- **Docker** (opcional, para deployment)
- **Git**

## 🚀 Instalación

### Backend

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/newtyf/appointment-booking-system-angular-java.git
   cd appointment-booking-system-angular-java/backend
   ```

2. **Crea y activa un entorno virtual**
   ```bash
   python3 -m venv venv
   source venv/bin/activate  # En macOS/Linux
   # venv\Scripts\activate   # En Windows
   ```

3. **Instala las dependencias**
   ```bash
   pip install -r requirements.txt
   ```

4. **Configura las variables de entorno**
   ```bash
   cp .env.example .env
   # Edita .env con tus credenciales
   ```

5. **Ejecuta las migraciones** (opcional, auto-crea en development)
   ```bash
   alembic upgrade head
   ```

6. **Inicia el servidor**
   ```bash
   fastapi dev app/app.py
   ```
   
   El backend estará disponible en `http://localhost:8000`

### Frontend

1. **Navega a la carpeta frontend**
   ```bash
   cd frontend
   ```

2. **Instala las dependencias**
   ```bash
   npm install
   ```

3. **Inicia el servidor de desarrollo**
   ```bash
   npm run dev
   ```
   
   El frontend estará disponible en `http://localhost:5173`

### Docker

Para deployment con Docker:

```bash
# Desde la raíz del proyecto
docker build -t monarca-appointment-system .
docker run -p 8000:8000 --env-file backend/.env monarca-appointment-system
```

La aplicación completa (frontend + backend) estará disponible en `http://localhost:8000`

## ⚙️ Configuración

### Variables de Entorno

Crea un archivo `.env` en la carpeta `backend/` con las siguientes variables:

```env
# Base de Datos
MYSQL_DB_USER=tu_usuario
MYSQL_DB_PASSWORD=tu_contraseña
MYSQL_DB_HOST=localhost
MYSQL_DB_PORT=3306
MYSQL_DB_NAME=appointment_booking

# API
API_PREFIX=/api
ENVIRONMENT=development  # o 'production'

# Seguridad y JWT
SECRET_KEY=tu_clave_secreta_muy_larga_y_segura
ALGORITHM=HS256
ACCESS_TOKEN_EXPIRE_MINUTES=30

# Culqi (Pagos)
CULQI_SECRET_KEY=sk_test_tu_clave_culqi
CULQI_API_URL=https://api.culqi.com/v2

# Email (Gmail)
EMAIL_SENDER=tu_correo@gmail.com
EMAIL_PASSWORD=tu_app_password
SMTP_SERVER=smtp.gmail.com
SMTP_PORT=587

# Replicate (IA)
REPLICATE_API_TOKEN=tu_token_replicate
```

### Configuración de MySQL

1. Crea la base de datos:
   ```sql
   CREATE DATABASE appointment_booking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. En modo development, las tablas se crean automáticamente al iniciar el servidor.

3. En modo production, ejecuta las migraciones:
   ```bash
   cd backend
   alembic upgrade head
   ```

## 🎯 Uso

### Acceso a la Aplicación

1. **Página de Inicio**: `http://localhost:5173` (desarrollo) o `http://localhost:8000` (producción)

2. **Registro de Usuario**: 
   - Navega a `/register`
   - Completa el formulario con tus datos
   - Por defecto, los nuevos usuarios tienen rol `client`

3. **Login**:
   - Navega a `/login`
   - Ingresa email y contraseña
   - Serás redirigido a tu dashboard según tu rol

### Rutas por Rol

- **Admin**: `/admin/*` - Gestión completa del sistema
- **Recepcionista**: `/receptionist/*` - Gestión de citas y clientes
- **Estilista**: `/stylist/*` - Vista de agenda y citas asignadas
- **Cliente**: `/client/*` - Reserva de citas y perfil

## 👥 Roles de Usuario

El sistema maneja 4 roles con diferentes permisos:

| Rol | Permisos |
|-----|----------|
| **Admin** | Acceso completo: usuarios, servicios, citas, reportes, configuración |
| **Recepcionista** | Gestión de citas, clientes walk-in, visualización de agenda |
| **Estilista** | Visualización de citas asignadas, actualización de estado de citas |
| **Cliente** | Reserva de citas, visualización de historial, gestión de perfil |

### Cambio de Rol

Solo los administradores pueden cambiar roles de usuarios. Para crear el primer admin:

```bash
cd backend
python generate_hashes.py  # Para generar hash de contraseña
# Luego inserta manualmente en la base de datos con role='admin'
```

## 📚 API Documentation

### Swagger UI

La documentación interactiva de la API está disponible en:

```
http://localhost:8000/docs
```

### Endpoints Principales

#### Autenticación
- `POST /api/auth/register` - Registro de usuario
- `POST /api/auth/login` - Login y obtención de token
- `GET /api/auth/me` - Información del usuario actual

#### Usuarios
- `GET /api/users` - Listar usuarios (admin)
- `GET /api/users/{id}` - Obtener usuario por ID
- `PUT /api/users/{id}` - Actualizar usuario
- `DELETE /api/users/{id}` - Eliminar usuario (admin)

#### Citas
- `GET /api/appointments` - Listar citas
- `POST /api/appointments` - Crear cita
- `GET /api/appointments/{id}` - Obtener cita por ID
- `PUT /api/appointments/{id}` - Actualizar cita
- `DELETE /api/appointments/{id}` - Cancelar cita
- `GET /api/appointments/availability` - Verificar disponibilidad

#### Servicios
- `GET /api/services` - Listar servicios
- `POST /api/services` - Crear servicio (admin)
- `PUT /api/services/{id}` - Actualizar servicio (admin)
- `DELETE /api/services/{id}` - Eliminar servicio (admin)

#### Pagos
- `POST /api/payments/charge` - Procesar pago con Culqi
- `GET /api/payments/history` - Historial de pagos

#### Dashboard
- `GET /api/dashboard/metrics` - Métricas del negocio

#### IA
- `POST /api/ai/hairstyle` - Editar imagen de cabello con IA

### Health Check

Verifica el estado del servidor y la base de datos:

```bash
curl http://localhost:8000/health
```

## 📁 Estructura del Proyecto

```
appointment-booking-system-angular-java/
├── backend/                       # Backend FastAPI
│   ├── alembic/                  # Migraciones de BD
│   ├── app/
│   │   ├── api/
│   │   │   ├── dependencies/     # Dependencias (auth, db, roles)
│   │   │   └── routes/           # Endpoints API
│   │   ├── core/                 # Configuración y seguridad
│   │   ├── db/                   # Sesión y base de datos
│   │   ├── models/               # Modelos SQLAlchemy
│   │   ├── schemas/              # Esquemas Pydantic
│   │   ├── services/             # Lógica de negocio
│   │   ├── utils/                # Utilidades
│   │   └── app.py                # Punto de entrada FastAPI
│   ├── .env.example              # Ejemplo de variables de entorno
│   ├── requirements.txt          # Dependencias Python
│   └── README.md                 # Documentación del backend
│
├── frontend/                      # Frontend React
│   ├── public/                   # Archivos estáticos
│   ├── src/
│   │   ├── assets/               # Imágenes, iconos
│   │   ├── components/           # Componentes reutilizables
│   │   ├── hooks/                # Custom hooks
│   │   ├── layouts/              # Layouts de páginas
│   │   ├── pages/                # Páginas por rol
│   │   │   ├── Admin/
│   │   │   ├── Client/
│   │   │   ├── Receptionist/
│   │   │   ├── Stylist/
│   │   │   └── Auth/
│   │   ├── routes/               # Configuración de rutas
│   │   ├── services/             # API services
│   │   ├── App.jsx               # Componente raíz
│   │   └── main.jsx              # Punto de entrada
│   ├── package.json              # Dependencias Node.js
│   ├── vite.config.js            # Configuración de Vite
│   └── README.md                 # Documentación del frontend
│
├── .gitignore
├── Dockerfile                     # Dockerfile para producción
├── instalar.txt                  # Notas de instalación
└── README.md                     # Este archivo
```

## 🤝 Contribuir

Este es un proyecto privado. Si eres parte del equipo de desarrollo, sigue estas pautas:

### Git Flow

Usamos [Git Flow](https://nvie.com/posts/a-successful-git-branching-model/) como estrategia de branching:

- **main**: Rama de producción (solo releases)
- **develop**: Rama de integración para desarrollo
- **feature/**: Nuevas características (`feature/nombre-feature`)
- **fix/**: Correcciones de bugs (`fix/nombre-bug`)
- **hotfix/**: Correcciones urgentes en producción
- **release/**: Preparación de releases

### Flujo de Trabajo

1. **Crea una rama desde develop**
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/mi-nueva-funcionalidad
   ```

2. **Desarrolla y haz commits siguiendo [Conventional Commits](https://www.conventionalcommits.org/es/v1.0.0/)**
   ```bash
   git commit -m "feat: añade endpoint para cancelar citas"
   git commit -m "fix: corrige validación de fechas en reservas"
   git commit -m "docs: actualiza documentación de API"
   ```

3. **Sube tu rama**
   ```bash
   git push origin feature/mi-nueva-funcionalidad
   ```

4. **Crea un Pull Request**
   - Ve a GitHub y abre un PR hacia `develop`
   - Describe los cambios realizados
   - Espera revisión y aprobación

5. **Merge a develop**
   - Una vez aprobado, haz merge a `develop`
   - Elimina la rama feature

### Convenciones de Commits

Usa [Conventional Commits](https://www.conventionalcommits.org/es/v1.0.0/):

- `feat:` Nueva funcionalidad
- `fix:` Corrección de bug
- `docs:` Cambios en documentación
- `style:` Cambios de formato (no afectan código)
- `refactor:` Refactorización de código
- `test:` Añadir o modificar tests
- `chore:` Tareas de mantenimiento

### Code Style

**Backend (Python)**
- Sigue PEP 8
- Usa type hints con `Annotated`
- Usa async/await para operaciones I/O
- Nombrado: `snake_case`

**Frontend (JavaScript/React)**
- Usa componentes funcionales
- Usa hooks para estado y efectos
- Nombrado: `camelCase` para variables, `PascalCase` para componentes
- Usa Tailwind CSS para estilos

## 📄 Licencia

Este proyecto es privado y confidencial. Todos los derechos reservados.

## 📧 Contacto

Para dudas, problemas o sugerencias:

- Abre un **Issue** en GitHub
- Contacta al equipo de desarrollo

---

**Monarca - Sistema de Reserva de Citas**  
Desarrollado con ❤️ para salones de belleza


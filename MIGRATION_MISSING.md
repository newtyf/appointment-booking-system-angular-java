# Migration Audit: Missing Backend Functionality

This document maps all missing functionality from the original FastAPI application that needs to be implemented in Spring Boot.

## Executive Summary

- **Original FastAPI Files Analyzed**: 48 Python files
- **Current Spring Boot Files**: 23 Java files
- **Missing Components**: 10 major areas
- **Total Missing Endpoints**: ~25 endpoints
- **Missing Services**: 8 services
- **Missing Integrations**: 3 external APIs

---

## 1. Authentication & Security (CRITICAL - MISSING)

### Original Files
- `backend/app/api/routes/auth.py`
- `backend/app/services/auth_service.py`
- `backend/app/core/security.py`
- `backend/app/api/dependencies/deps.py`

### Missing Functionality

#### Endpoints
| Endpoint | Method | Original Route | Description | Status |
|----------|--------|---------------|-------------|---------|
| Register | POST | `/api/auth/register` | User registration with password hashing | ❌ Missing |
| Login | POST | `/api/auth/login` | User login with JWT token generation | ❌ Missing |

#### Core Components
- **JWT Token Generation**: Using HS256 algorithm with SECRET_KEY
- **JWT Token Validation**: Parse and validate Bearer tokens
- **Password Hashing**: BCrypt with salt rounds
- **Role-Based Access Control**: `@PreAuthorize` for admin, receptionist, stylist, client
- **Current User Extraction**: From SecurityContext
- **Authentication Filter**: JwtAuthenticationFilter to parse tokens
- **Security Configuration**: SecurityFilterChain with CORS, CSRF disabled

#### Original Implementation Details
```python
# From auth_service.py
- create_access_token(data: dict) -> str
- verify_password(plain_password: str, hashed_password: str) -> bool
- get_password_hash(password: str) -> str
- authenticate_user(login_data: Login) -> User | None
- register_user(user_create: UserCreate) -> User | None

# From security.py
- SECRET_KEY, ALGORITHM = "HS256", ACCESS_TOKEN_EXPIRE_MINUTES = 1440
- Uses python-jose for JWT, passlib for BCrypt

# From deps.py
- get_current_user() -> User (extracts from JWT Bearer token)
- check_user_role(*roles) -> bool (validates user has required role)
```

#### Required Spring Components
- `JwtUtil` class for token operations
- `JwtAuthenticationFilter` for request processing
- `SecurityConfig` with SecurityFilterChain
- `AuthController` for login/register endpoints
- `AuthService` for business logic
- `UserDetailsService` implementation

---

## 2. Appointment Business Logic (PARTIALLY IMPLEMENTED)

### Original Files
- `backend/app/api/routes/appointments.py`
- `backend/app/services/appointment_service.py`
- `backend/app/services/availability_service.py`

### Missing Endpoints

| Endpoint | Method | Original Route | Description | Status |
|----------|--------|---------------|-------------|---------|
| List All | GET | `/api/appointments` | List all appointments (admin/receptionist) | ⚠️ Partially |
| My Appointments | GET | `/api/appointments/my-appointments` | Role-based appointment listing | ❌ Missing |
| Availability | GET | `/api/appointments/availability` | Calculate available time slots | ❌ Missing |
| Get One | GET | `/api/appointments/{id}` | Get appointment by ID with auth check | ⚠️ Partially |
| Create | POST | `/api/appointments/` | Create appointment (admin/receptionist) | ⚠️ Partially |
| Book | POST | `/api/appointments/book` | Client self-booking | ❌ Missing |
| Walk-in | POST | `/api/appointments/walk-in` | Walk-in appointment creation | ❌ Missing |
| Update | PUT | `/api/appointments/{id}` | Update appointment | ⚠️ Partially |
| Update Status | PATCH | `/api/appointments/{id}/status` | Change appointment status | ❌ Missing |
| Cancel | DELETE | `/api/appointments/{id}` | Cancel appointment | ⚠️ Partially |

### Missing Business Logic

#### Availability Calculation
```python
# From appointment_service.py:get_availability()
- Calculate available time slots for a given date
- Consider service duration (e.g., 60 minutes)
- Check business hours (8 AM to 8 PM)
- Exclude existing bookings for each stylist
- Return slots per stylist or specific stylist
- Time slot granularity: 15 or 30 minutes
```

#### Conflict Checking
```python
# From appointment_service.py:_check_stylist_availability()
- Verify stylist is not double-booked
- Calculate appointment end time (date + service.duration_min)
- Check for overlapping appointments (same stylist, pending/confirmed status)
- Query: WHERE stylist_id = X AND status IN ('pending', 'confirmed')
         AND ((new_start <= existing_end AND new_end >= existing_start))
```

#### Business Hours Validation
```python
# From appointment_service.py:_validate_datetime()
- No past appointments: appointment_date >= now (UTC)
- Hour must be >= 8 and < 20 (8 AM to 7:59 PM)
- Appointment end time must also be before 8 PM
```

#### Walk-In Support
```python
# From appointment_service.py:create_walk_in_appointment()
- client_id = NULL
- is_walk_in = true
- client_name, client_phone, client_email are required
- Only admin/receptionist can create walk-ins
```

#### Role-Based Listing
```python
# From appointments.py:list_my_appointments()
- admin/receptionist: see ALL appointments
- stylist: see only their appointments (stylist_id = user.id)
- client: see only their appointments (client_id = user.id)
```

#### Appointment Status Workflow
```python
# From appointment_service.py:update_appointment_status()
- Valid statuses: pending, confirmed, completed, cancelled, no-show
- Stylist can update their own appointments
- Client can only DELETE (cancel), not PATCH status
- Modified_by field tracks who made the change
```

---

## 3. Payment Integration - Culqi (MISSING)

### Original Files
- `backend/app/api/routes/payments.py`
- `backend/app/services/payment_service.py`

### Missing Endpoints

| Endpoint | Method | Original Route | Description | Status |
|----------|--------|---------------|-------------|---------|
| Create Charge | POST | `/api/payments/charge` | Create payment charge | ❌ Missing |
| Get Charge | GET | `/api/payments/{charge_id}` | Get charge details | ❌ Missing |
| Payment History | GET | `/api/payments/history` | List charges | ❌ Missing |

### Implementation Details

#### API Integration
```python
# From payment_service.py
- Base URL: settings.CULQI_API_URL (https://api.culqi.com/v2)
- Auth: Bearer token with CULQI_SECRET_KEY
- Uses httpx.AsyncClient for HTTP calls

# Endpoints:
POST /charges
  Payload: {
    "amount": 10000,  # in centavos (100.00 PEN)
    "currency_code": "PEN",
    "email": "customer@example.com",
    "source_id": "tkn_live_...",  # from Culqi Checkout
    "description": "Appointment payment",
    "metadata": {}
  }

GET /charges/{charge_id}
  Returns: charge object with status, amount, etc.

GET /charges?email=...&limit=10
  Returns: { data: [...], paging: {...} }
```

#### Error Handling
```python
- HTTPStatusError: extract error_data.user_message
- RequestError: "Error de conexión con el procesador de pagos"
- Return 400 for Culqi errors, 500 for connection errors
```

#### Security
```python
# From payments.py:create_charge()
- Requires authentication (current_user)
- Admin can see all payments
- Non-admin can only see their own (filter by email)
- Logs all payment operations with user ID
```

---

## 4. AI Integration - Replicate (MISSING)

### Original Files
- `backend/app/api/routes/ai.py`

### Missing Endpoints

| Endpoint | Method | Original Route | Description | Status |
|----------|--------|---------------|-------------|---------|
| Process Image | POST | `/api/ai/process-image` | AI image editing | ❌ Missing |

### Implementation Details

#### API Integration
```python
# From ai.py
- Uses replicate Python SDK
- Model version: "12b5a5a61e3419f792eb56cfc16eed046252740ebf5d470228f9b4cf2c861610"
- Requires REPLICATE_API_TOKEN environment variable

# Process:
1. Accept UploadFile (multipart/form-data)
2. Accept prompt (Form field)
3. Convert image to base64 data URI
4. Call replicate.predictions.async_create()
5. Poll prediction status with asyncio.sleep(1)
6. Wait until status = "succeeded", "failed", or "canceled"
7. Return output image URL

# Response:
{
  "message": "Image processed and saved successfully",
  "image_url": "https://replicate.delivery/..."
}
```

#### Validation
```python
- Check file.content_type starts with "image/"
- Return 400 if not an image
- Return 500 if Replicate fails or times out
```

---

## 5. Email & Notifications (MISSING)

### Original Files
- `backend/app/api/routes/notifications.py`
- `backend/app/services/notification_service.py`
- `backend/app/services/email_service.py`
- `backend/app/utils/email_templates.py`

### Missing Endpoints

| Endpoint | Method | Original Route | Description | Status |
|----------|--------|---------------|-------------|---------|
| Create Notification | POST | `/api/notifications/` | Create and send notification | ❌ Missing |
| Test Email | GET | `/api/notifications/send` | Send test email | ❌ Missing |

### Email Service Details

#### SMTP Configuration
```python
# From email_service.py
- SMTP_SERVER, SMTP_PORT (e.g., smtp.gmail.com:587)
- EMAIL_SENDER, EMAIL_PASSWORD
- Uses smtplib with STARTTLS
- Supports plain text and HTML emails (MIMEMultipart)
```

#### Email Templates
```python
# From email_templates.py:generate_appointment_email_html()
- notification_type: "reservado", "confirmado", "cancelado", "recordatorio"
- Each type has color scheme, icon, title, message
- Uses HTML table layout for email compatibility
- Includes appointment details: service, stylist, date, time
- Formatted with Monarca branding (pink/amber colors)

# Template sections:
- Header: Gradient background with Monarca logo and icon
- Body: Personalized greeting, message, appointment card
- Footer: Salon name and tagline
```

#### Notification Service
```python
# From notification_service.py
- create_notification(appointment_id, user_id, type, channel, status, title, body)
- send_email_notification(notification_id) - fetches notification and sends email
- Uses BackgroundTasks for async email sending
```

---

## 6. Dashboard & Metrics (MISSING)

### Original Files
- `backend/app/api/routes/dashboard.py`
- `backend/app/services/dashboard_service.py`
- `backend/app/schemas/dashboard.py`

### Missing Endpoints

| Endpoint | Method | Original Route | Description | Status |
|----------|--------|---------------|-------------|---------|
| Dashboard | GET | `/api/dashboard` | Role-based dashboard data | ❌ Missing |

### Dashboard Types

#### Admin Dashboard
```python
# Returns:
- appointments_stats: { total, pending, confirmed, completed, cancelled, no_show }
- total_clients, total_stylists, total_services
- top_stylists: [{ stylist_id, stylist_name, total_appointments, completed_appointments }]
- top_services: [{ service_id, service_name, times_booked }]
- walk_in_percentage
- recent_appointments: [last 5 appointments]
```

#### Receptionist Dashboard
```python
# Returns:
- appointments_today: [all appointments today, sorted by date]
- pending_confirmations: [appointments with status=pending, limit 10]
- stylists_availability: [{ stylist_id, stylist_name, appointments_today, next_available_slot }]
- total_appointments_today
- walk_ins_today
```

#### Stylist Dashboard
```python
# Returns:
- next_appointment: { id, client_name, service_name, date, duration_min, is_walk_in }
- appointments_today: [all appointments today for this stylist]
- appointments_upcoming: [next 7 days, pending/confirmed]
- total_completed_this_month
```

#### Client Dashboard
```python
# Returns:
- upcoming_appointments: [future appointments, pending/confirmed]
- past_appointments: [last 5 past appointments]
- total_appointments
- favorite_service: (most frequently booked service name)
```

---

## 7. User Management (PARTIALLY IMPLEMENTED)

### Current Status
- ✅ User entity exists
- ✅ UserRepository exists
- ✅ UserService exists
- ✅ UserController exists

### Missing Functionality
- ❌ Password hashing on user creation/update
- ❌ Role validation
- ❌ User filtering by role (e.g., get all stylists)
- ❌ Proper authentication required for endpoints

### Required Enhancements
```java
// UserService needs:
- BCryptPasswordEncoder for hashing
- findByEmail(String email) method
- findByRole(String role) method
- Validate role in ["admin", "receptionist", "stylist", "client"]

// UserController needs:
- @PreAuthorize("hasRole('admin')") on create/update/delete
- Return proper error codes (409 for duplicate email)
```

---

## 8. Service Management (PARTIALLY IMPLEMENTED)

### Current Status
- ✅ Service entity exists
- ✅ ServiceRepository exists
- ✅ ServiceService exists
- ✅ ServiceController exists

### Missing Functionality
- ❌ Authentication required for write operations
- ❌ Role-based access (admin only for create/update/delete)

### Required Enhancements
```java
// ServiceController needs:
- @PreAuthorize("hasRole('admin')") on POST/PUT/DELETE
- Public GET for read operations
```

---

## 9. Repositories Extensions

### Missing Query Methods

#### AppointmentRepository
```java
// From original appointment_service.py queries:
List<Appointment> findByClientId(Long clientId);
List<Appointment> findByStylistId(Long stylistId);
List<Appointment> findByStylistIdAndDateBetween(Long stylistId, LocalDateTime start, LocalDateTime end);
List<Appointment> findByStatusIn(List<String> statuses);
List<Appointment> findByDateBetween(LocalDateTime start, LocalDateTime end);
```

#### UserRepository
```java
Optional<User> findByEmail(String email);
List<User> findByRole(String role);
```

---

## 10. Configuration & Environment Variables

### Missing Configuration
- ❌ JWT configuration (SECRET_KEY, ALGORITHM, EXPIRATION)
- ❌ Culqi API configuration (API_URL, SECRET_KEY)
- ❌ Replicate configuration (API_TOKEN)
- ❌ Email/SMTP configuration (SERVER, PORT, SENDER, PASSWORD)
- ❌ CORS configuration for frontend

### Required application.yml Structure
```yaml
# Database (already exists)
spring:
  datasource:
    url: jdbc:mysql://${MYSQL_DB_HOST}:${MYSQL_DB_PORT}/${MYSQL_DB_NAME}
    username: ${MYSQL_DB_USER}
    password: ${MYSQL_DB_PASSWORD}

# JWT
jwt:
  secret: ${SECRET_KEY}
  algorithm: HS256
  expiration: ${ACCESS_TOKEN_EXPIRE_MINUTES:1440}

# Culqi
culqi:
  api-url: ${CULQI_API_URL:https://api.culqi.com/v2}
  secret-key: ${CULQI_SECRET_KEY}

# Replicate
replicate:
  api-token: ${REPLICATE_API_TOKEN}

# Email
spring:
  mail:
    host: ${SMTP_SERVER}
    port: ${SMTP_PORT}
    username: ${EMAIL_SENDER}
    password: ${EMAIL_PASSWORD}
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true

# CORS
cors:
  allowed-origins: ${CORS_ORIGINS:http://localhost:4200}
```

---

## 11. Models/Entities Status

### ✅ Already Implemented
- User (with role field)
- Appointment (with walk-in support)
- Service (with duration_min field)
- Notification
- Reminder
- Access

### Required Enhancements
- User entity needs @JsonProperty for snake_case fields if needed
- Appointment entity needs @JsonProperty annotations
- All entities need proper Jackson configuration for date serialization (UTC)

---

## Implementation Priority

### Phase 1: Critical (Blocks Everything)
1. Authentication & Security (JWT, BCrypt, Spring Security)
2. User password hashing
3. Role-based access control

### Phase 2: Core Business Logic
4. Availability calculation
5. Appointment conflict checking
6. Walk-in appointment support
7. Appointment status transitions

### Phase 3: Integrations
8. Payment service (Culqi)
9. Email service with templates
10. Notification system

### Phase 4: Advanced Features
11. AI image processing (Replicate)
12. Dashboard metrics
13. Reminder scheduling

### Phase 5: Polish
14. CORS configuration
15. Error handling improvements
16. Documentation updates

---

## Estimated Implementation

- **Total Components**: ~30 new classes
- **Total Endpoints**: ~25 endpoints
- **Dependencies to Add**: 6 (Spring Security, JWT, WebClient, Mail, Lombok, Validation)
- **Configuration Files**: 1 (application.yml updates)
- **Documentation Files**: 1 (MIGRATION_COMPLETION.md)

---

## Notes

1. **No DTOs**: Use entities directly in controllers as per new requirement
2. **Jackson Configuration**: Add @JsonProperty where needed for field name compatibility
3. **UTC Datetime**: Use `ZonedDateTime.now(ZoneOffset.UTC)` or `LocalDateTime` with UTC
4. **Centavos**: All payment amounts must be integers representing cents
5. **Token Compatibility**: Use same HS256 algorithm and SECRET_KEY for JWT
6. **No Tests**: Do not create unit or integration tests as per requirement

---

**Generated**: November 5, 2025  
**Status**: Ready for implementation  
**Next Step**: Phase 1 - Authentication & Security

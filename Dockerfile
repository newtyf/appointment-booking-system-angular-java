# Dockerfile for fullstack appointment booking system (Spring Boot + Angular)
# 1. Build frontend
FROM node:20 AS frontend-build
WORKDIR /app/frontend
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install --legacy-peer-deps
COPY frontend/ ./
RUN npm run build

# 2. Build backend
FROM maven:3.9.11-eclipse-temurin-21 AS backend-build
WORKDIR /app/backend
COPY backend/pom.xml ./
RUN mvn dependency:go-offline
COPY backend/src ./src
# Copy frontend build to backend static directory
COPY --from=frontend-build /app/frontend/dist/monarca-frontend/browser ./src/main/resources/static
RUN mvn clean package -DskipTests

# 3. Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the JAR from build stage
COPY --from=backend-build /app/backend/target/*.jar app.jar

EXPOSE 8080

# Start Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

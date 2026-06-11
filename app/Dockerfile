# =========================
# 1. BUILD STAGE
# =========================
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Instalar Maven (más estable que mvnw en Railway)
RUN apt-get update && apt-get install -y maven

# Copiar primero pom para aprovechar cache de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el resto del proyecto
COPY . .

# Build de producción
RUN mvn clean package -DskipTests -B


# =========================
# 2. RUNTIME STAGE
# =========================
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copiar jar generado
COPY --from=build /app/target/app.jar app.jar

# Railway usa esta variable para el puerto
ENV PORT=8080

# Exponer puerto (no obligatorio pero recomendado)
EXPOSE 8080

# Arranque
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
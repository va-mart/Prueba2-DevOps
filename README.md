# Prueba2-DevOps

# Microservicio Cliente - Veterinaria

Microservicio REST para la gestión de clientes de una veterinaria, desarrollado con Spring Boot 3, MySQL, Docker y CI/CD con GitHub Actions.

---

## Tecnologías utilizadas

| Tecnología | Propósito |
|---|---|
| Spring Boot 3.2.5 | Framework principal |
| Java 21 | Lenguaje de programación |
| MySQL 8 | Base de datos relacional |
| Flyway | Migraciones de base de datos |
| Spring Security + JWT | Autenticación y autorización |
| Docker | Contenerización |
| Docker Compose | Orquestación local |
| GitHub Actions | Pipeline CI/CD |
| JaCoCo | Cobertura de código |
| Snyk | Análisis de seguridad |
| Dependabot | Actualización automática de dependencias |

---

## Cómo ejecutar el proyecto

### Opción 1: Docker Compose (recomendado)

Requiere tener instalado Docker Desktop.

```bash
# Clonar el repositorio
git clone <URL-del-repositorio>
cd Cliente

# Levantar la aplicación y la base de datos
docker compose up

# O en segundo plano
docker compose up -d
```

La aplicación estará disponible en `http://localhost:8080`.
La base de datos MySQL estará disponible en `localhost:3306`.

Para detener:
```bash
docker compose down
```

Para detener y eliminar los datos:
```bash
docker compose down -v
```

### Opción 2: Desarrollo local

Requisitos: Java 21, Maven, MySQL corriendo en `localhost:3306`.

1. Crear la base de datos en MySQL:
```sql
CREATE DATABASE clientes_db;
```

2. Verificar la configuración en `src/main/resources/application.yml` (usuario y contraseña).

3. Ejecutar:
```bash
./mvnw spring-boot:run
```

---

## Endpoints de la API

### Autenticación

| Método | Ruta | Descripción | Auth requerida |
|---|---|---|---|
| POST | `/auth/login` | Obtener token JWT | No |

**Body de login:**
```json
{
  "username": "admin",
  "password": "1234"
}
```

**Respuesta:**
```json
{
  "tipo": "Bearer",
  "token": "eyJhbGci..."
}
```

### Clientes

Todos los endpoints requieren el header: `Authorization: Bearer <token>`

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/v1/clientes` | Listar todos los clientes |
| GET | `/api/v1/clientes/{id}` | Buscar cliente por ID |
| GET | `/api/v1/clientes/rut/{rut}` | Buscar cliente por RUT |
| POST | `/api/v1/clientes` | Crear nuevo cliente |
| PUT | `/api/v1/clientes/{id}` | Actualizar cliente |
| DELETE | `/api/v1/clientes/{id}` | Eliminar cliente |

**Ejemplo de body para crear/actualizar:**
```json
{
  "nombre": "Juan Pérez González",
  "rut": "12345678-9",
  "telefono": "+56912345678",
  "email": "juan.perez@email.com",
  "direccion": "Av. Providencia 1234, Santiago",
  "fechaRegistro": "2024-01-10"
}
```

---

## Cómo funciona el pipeline CI/CD

El pipeline se define en [`.github/workflows/ci-cd.yml`](.github/workflows/ci-cd.yml) y se activa automáticamente con cada `push` a `main` o `develop`, y en cada Pull Request hacia `main`.

### Etapas del pipeline

```
Push/PR
   │
   ▼
┌─────────────────┐
│  1. BUILD & TEST │  → Compila el proyecto, ejecuta tests JUnit y verifica
│                 │    cobertura mínima de 60% con JaCoCo.
│                 │    FALLA si la cobertura es insuficiente.
└────────┬────────┘
         │ (solo si pasa)
         ▼
┌─────────────────┐
│  2. SECURITY    │  → Ejecuta Snyk para detectar vulnerabilidades en
│                 │    dependencias. FALLA si encuentra vulnerabilidades HIGH
│                 │    o CRITICAL.
└────────┬────────┘
         │ (solo en push a main)
         ▼
┌─────────────────┐
│  3. DOCKER      │  → Compila el JAR y construye la imagen Docker.
│                 │    La publica en GitHub Container Registry (GHCR).
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  4. DEPLOY      │  → Despliega la aplicación con Docker Compose.
└─────────────────┘
```

### Variables y secretos requeridos en GitHub

Ir a: **Settings → Secrets and variables → Actions** y agregar:

| Secret | Descripción |
|---|---|
| `SNYK_TOKEN` | Token de autenticación de Snyk (obtenido en snyk.io) |
| `DB_PASSWORD` | Contraseña para MySQL en producción |
| `JWT_SECRET` | Clave secreta para firmar los tokens JWT |

El secret `GITHUB_TOKEN` es generado automáticamente por GitHub Actions y se usa para publicar la imagen en GHCR.

---

## Cómo se garantiza la calidad y trazabilidad

### Calidad de código - JaCoCo

JaCoCo mide la cobertura de código de los tests unitarios. La configuración en `pom.xml` establece:

- **Cobertura mínima requerida: 60% de líneas**
- El pipeline **falla automáticamente** si no se alcanza ese umbral
- El reporte HTML se genera en `target/site/jacoco/` y se publica como artefacto en cada ejecución del pipeline

Para generar el reporte localmente:
```bash
./mvnw clean verify
# Abrir: target/site/jacoco/index.html
```

### Seguridad - Snyk

Snyk analiza las dependencias del proyecto en busca de vulnerabilidades conocidas (CVEs):

- Se ejecuta automáticamente en cada pipeline
- El umbral configurado es `--severity-threshold=high`
- El pipeline **falla** si se detectan vulnerabilidades de severidad HIGH o CRITICAL
- Los resultados se suben a la pestaña **Security** del repositorio en GitHub (formato SARIF)

Para escanear localmente (requiere Snyk CLI):
```bash
npm install -g snyk
snyk auth
snyk test --severity-threshold=high
```

Crear cuenta gratuita en: https://app.snyk.io

### Dependabot

Configurado en [`.github/dependabot.yml`](.github/dependabot.yml) para revisar semanalmente:
- Dependencias Maven (`pom.xml`)
- Imagen base de Docker (`Dockerfile`)
- Actions de GitHub Actions (`.github/workflows/`)

Crea Pull Requests automáticos cuando hay actualizaciones disponibles, garantizando que las dependencias se mantengan actualizadas y seguras.

### Trazabilidad

- Cada imagen Docker se etiqueta con el SHA del commit: `ghcr.io/<usuario>/veterinaria-cliente:<sha>`
- El reporte de cobertura se guarda como artefacto en cada ejecución (retención: 7 días)
- Los logs de la aplicación se guardan en `logs/clientes.log`
- Flyway mantiene el historial completo de migraciones de base de datos en `flyway_schema_history`

---

## Estructura del proyecto

```
Cliente/
├── src/
│   ├── main/
│   │   ├── java/Veterinaria/Cliente/
│   │   │   ├── Controller/        # Endpoints REST
│   │   │   ├── Service/           # Lógica de negocio
│   │   │   ├── Repository/        # Acceso a datos (JPA)
│   │   │   ├── Model/             # Entidades JPA
│   │   │   ├── DTO/               # Objetos de transferencia
│   │   │   ├── Exception/         # Manejo de errores
│   │   │   ├── security/          # JWT Filter y Service
│   │   │   └── config/            # Spring Security Config
│   │   └── resources/
│   │       ├── application.yml    # Configuración (con variables de entorno)
│   │       └── db/migration/      # Scripts Flyway
│   └── test/
│       └── java/Veterinaria/Cliente/
│           ├── service/           # Tests unitarios del servicio
│           └── controller/        # Tests del controlador (MockMvc)
├── .github/
│   ├── workflows/
│   │   └── ci-cd.yml              # Pipeline GitHub Actions
│   └── dependabot.yml             # Configuración Dependabot
├── Dockerfile                     # Build multi-etapa
├── docker-compose.yml             # Orquestación local
└── pom.xml                        # Dependencias + JaCoCo
```

---

## Configuración con variables de entorno

La aplicación usa variables de entorno con valores por defecto para desarrollo local:

| Variable | Valor por defecto | Descripción |
|---|---|---|
| `DB_HOST` | `localhost` | Host de MySQL |
| `DB_PORT` | `3306` | Puerto de MySQL |
| `DB_NAME` | `clientes_db` | Nombre de la base de datos |
| `DB_USERNAME` | `root` | Usuario de MySQL |
| `DB_PASSWORD` | *(vacío)* | Contraseña de MySQL |
| `JWT_SECRET` | *(valor por defecto)* | Clave secreta JWT |

En producción (Docker Compose), estas variables se inyectan automáticamente desde el archivo `.env` o desde los Secrets de GitHub Actions.

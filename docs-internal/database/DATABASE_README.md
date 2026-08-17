# Database Setup & Management

**Authored and maintained by:** Miranda Murphy  
**Last updated:** 2026-08-17


## Overview

This project uses PostgreSQL 18 with Flyway-managed schema migrations.

Database responsibilities are separated as follows:

| Component        | Responsibility                          |
|------------------|-----------------------------------------|
| PostgreSQL Roles | Database-level permissions              |
| Flyway           | Schema migrations and schema ownership  |
| Spring Security  | Application users and API authorization |

Database roles do not manage application users or API access (managed by Spring Security).

---
### Docker Compose Environment

This project uses Docker Compose to run the Spring Boot backend and PostgreSQL databases locally.

### Services:

| Service      | Purpose                                 |
|--------------|-----------------------------------------|
| `backend`    | Spring Boot application                 |
| `app-db`     | Primary application PostgreSQL database |
| `sandbox-db` | Local development sandbox database      |


### Backend Service

#### Spring Boot application container.

| Setting        | Description                 |
|----------------|-----------------------------|
| Container name | `flexbox-backend`           |
| Port           | `8080:8080`                 |
| Build context  | `./flexbox-backend`         |
| Database       | `app-db` PostgreSQL service |
| Restart policy | `unless-stopped`            |

#### The backend receives the database configuration through environment variables:

| Variable                     | Purpose                       |
|------------------------------|-------------------------------|
| `SPRING_DATASOURCE_URL`      | PostgreSQL connection URL     |
| `SPRING_DATASOURCE_USERNAME` | Application database role     |
| `SPRING_DATASOURCE_PASSWORD` | Application database password |
| `DB_MIGRATION_ROLE`          | Flyway migration role         |
| `MIGRATION_DB_PASSWORD`      | Flyway migration password     |

The backend waits for the application database health check before starting.

---
### Application Database

Primary PostgreSQL database.

| Setting        | Description      |
|----------------|------------------|
| Image          | `postgres:18`    |
| Container name | `flexbox-app-db` |
| Host port      | `5434`           |
| Database       | `${APP_DB}`      |

---

### Persistent Storage

Database data is stored using the Docker volume:

```text
app-postgres-data
```

Mounted to:

```text
/var/lib/postgresql/18/docker
```

Docker volumes allow database data to persist when containers are stopped or recreated.

---
### Example workflow: 

**Stop containers:**

```bash
docker compose stop
```

Outcome: Containers stop and volume remains.

**Start containers:**

```bash
docker compose up
```

Outcome: Existing database data is restored.

**Remove database volumes:** \
_Note: The database volume is only removed when explicitly deleted._

```bash
docker compose down -v
```

---
### Sandbox Database

Secondary PostgreSQL database used for development and experimentation.

| Setting        | Description          |
|----------------|----------------------|
| Image          | `postgres:18`        |
| Container name | `flexbox-sandbox-db` |
| Host port      | `5433`               |
| Database       | `${SANDBOX_DB}`      |

The sandbox database is isolated from the application database.

**Persistent storage uses a separate Docker volume:**

```text
sandbox-postgres-data
```
--- 


### Database Roles

| Database Role       | Purpose                                | Used By                  |
|---------------------|----------------------------------------|--------------------------|
| `postgres`          | Database superuser                     | Docker / DBA operations  |
| `postgres_sandbox`  | Sandbox database access                | Local development        |
| `flexbox_migration` | Flyway migrations and schema ownership | Flyway                   |
| `flexbox_app`       | Application queries                    | Spring Boot backend      |
| `flexbox_test`      | Integration testing                    | Spring Boot Test profile |

### Role Rules

- Never connect the Spring Boot application using the `postgres` role
- Backend queries must use `flexbox_app`
- All schema changes must be committed as Flyway migrations

---
### Database Initialization Workflow

When the application starts:

1. PostgreSQL container starts
2. Database initialization scripts create roles and permissions
3. Spring Boot connects using configured credentials
4. Flyway applies migrations using `flexbox_migration`
5. JPA validates the schema
6. Application starts

#### Initialization scripts are located in:

```text
database/app-init
```

#### and mounted to:

```text
/docker-entrypoint-initdb.d
```

These scripts **only** execute when the PostgreSQL data directory is empty.

--- 
### Database Reset

Reset the local database when:

- Initialization scripts are modified

Run:

```bash
docker compose down -v
docker compose up --build
```

**This will:**

1. Stop all containers
2. Remove PostgreSQL volumes
3. Recreate the databases
4. Run initialization scripts
5. Apply Flyway migrations

> Warning: This deletes all local database data.

**Create a backup before resetting if required by running:**

```bash
pg_dump
```
---
### Environment Variables

Database credentials are managed through environment variables.

| File           | Purpose                                |
|----------------|----------------------------------------|
| `.env`         | Local database credentials             |
| `.env.example` | Required environment variable template |

Rules:

- `.env` is ignored by Git
- Never commit `.env`
- Update `.env.example` when adding variables

---
## Integration Testing

Integration tests use Testcontainers to create isolated PostgreSQL database instances.

### Test Workflow

1. Testcontainers starts a temporary PostgreSQL container
2. Spring Boot connects using generated connection details
3. Flyway applies migrations
4. JPA validates the schema
5. Tests execute against the temporary database



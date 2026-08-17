# Docker
The `docker-compose.yml` file creates all the applications containers.

### PostgreSQL

| Service      | Database          | Purpose                                          | Database Roles                             |
|--------------|-------------------|--------------------------------------------------|--------------------------------------------|
| `app-db`     | `flexbox`         | Development/Production                           | `postgres, flexbox_migration, flexbox_app` |
| `sandbox-db` | `flexbox-sandbox` | Experimenting (queries, procedures, views, etc.) | `postgres_sandbox`                         |

## How to load database:






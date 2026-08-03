## Database Role Boundaries

| DB Account        | Purpose                            | Used By                  |
|-------------------|------------------------------------|--------------------------|
| postgres          | App superuser                      | Docker/DBA               |
| postgres_sandbox  | Sandbox superuser                  | Docker/DBA               |
| flexbox_migration | Flyway migrations, schema owner    | Flyway                   |
| flexbox_app       | All API queries (customer & admin) | Spring Boot              |
| flexbox_test      | Integration tests                  | Spring Boot Test Profile |

Notes:

### Scope ###

IN: 
- Postgres database permissions 
- Schema changes via Flyway

OUT: 
- API endpoint auth: Spring Security handles users and roles (including auth for /admin).

Postgres handles backend permissions.
Flyway handles schema changes.

- Only use roles for their intended purpose
  - Never connect `postgres` role to Spring Boot backend.
  - `postgres_sandbox` CAN be connected to the Spring Boot backend **ONLY** for sandbox purposes.
- Database role credentials are listed in the `.env` file. 
- `.env.example` defines the structure of the `.env` file.
- `.env` is included in the `.gitignore` file and should **never** be removed/committed.
- ALL database schema changes Flyway must be used with the `flexbox_migration` role. 
  - Do not make manual schema changes to any database outside of migrations under any circumstances.
- Authorization for admin-only API endpoints is out of scope for DB roles and is handled by **Spring Security**.


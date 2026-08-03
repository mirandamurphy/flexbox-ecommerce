## Resetting the local database

If you pull a change that modifies the initial schema or your local database becomes out of sync:

```bash
docker compose down -v
docker compose up --build
```

This removes the local PostgreSQL volumes and recreates the database from scratch. 

Note: All local database data will be lost.
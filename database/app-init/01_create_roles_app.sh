#!/bin/bash

set -e
echo "Creating the following roles for the ${APP_DB} database: ${DB_MIGRATION_ROLE}, ${DB_APP_ROLE}"

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$APP_DB" <<-EOSQL

  CREATE ROLE ${DB_MIGRATION_ROLE} WITH LOGIN PASSWORD '${MIGRATION_DB_PASSWORD}';
  CREATE ROLE ${DB_APP_ROLE} WITH LOGIN PASSWORD '${DB_APP_PASSWORD}';

EOSQL

echo "The following roles were successfully created for the ${APP_DB} database: ${DB_MIGRATION_ROLE}, ${DB_APP_ROLE}"





#!/bin/bash

set -e
echo "Granting privileges for ${DB_MIGRATION_ROLE} and ${DB_APP_ROLE} on the ${APP_DB} database"

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$APP_DB" <<-EOSQL

  -- Grants the specified roles the ability to connect to the 'flexbox' database
  GRANT CONNECT ON DATABASE ${APP_DB} TO ${DB_MIGRATION_ROLE}, ${DB_APP_ROLE};

  -- Grants the specified role the ability to use and create within the 'public' schema
  GRANT USAGE, CREATE ON SCHEMA public TO ${DB_MIGRATION_ROLE};
  -- Grants the specified role the ability to use the 'public' schema
  GRANT USAGE ON SCHEMA public TO ${DB_APP_ROLE};

  -- Grants the specified roles to perform specific actions on tables within the 'public' schema
  GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO ${DB_APP_ROLE};
  GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO ${DB_APP_ROLE};

  -- When the migration role creates a new table/seq. in future, auto grant access to that object
  ALTER DEFAULT PRIVILEGES FOR ROLE ${DB_MIGRATION_ROLE} IN SCHEMA public
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO ${DB_APP_ROLE};

  ALTER DEFAULT PRIVILEGES FOR ROLE ${DB_MIGRATION_ROLE} IN SCHEMA public
  GRANT USAGE, SELECT ON SEQUENCES TO ${DB_APP_ROLE};

EOSQL

echo "Successfully granted privileges for ${DB_MIGRATION_ROLE} and ${DB_APP_ROLE} on the ${APP_DB} database"
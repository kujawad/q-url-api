#!/usr/bin/env sh
set -e

# Create the 'q-url-api' database
"${psql[@]}" <<- 'EOSQL'
CREATE DATABASE "q-url-api";
EOSQL

# Load uuid-ossp into 'q-url-api'
echo "Loading uuid-ossp extensions into q-url-api"
	"${psql[@]}" --dbname="q-url-api" <<-'EOSQL'
		CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
EOSQL
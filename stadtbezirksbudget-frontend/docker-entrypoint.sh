#!/bin/sh
set -e

envsubst < ../runtime-env.template.js > ./runtime-env.js

exec "$@"

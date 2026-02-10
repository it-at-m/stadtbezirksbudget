#!/bin/sh
set -e

: "${ZAMMAD_BASE_URL}"
: "${EAKTE_BASE_URL}"

mv ./runtime-env.template.js ./runtime-env.js

sed -i "s|\${ZAMMAD_BASE_URL}|$ZAMMAD_BASE_URL|g" ./runtime-env.js
sed -i "s|\${EAKTE_BASE_URL}|$EAKTE_BASE_URL|g" ./runtime-env.js

exec "$@"

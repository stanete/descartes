#!/bin/bash
set -e

# Extract the protocol.
protocol="$(echo "$DATABASE_URL" | grep :// | sed -e's,^\(.*://\).*,\1,g')"
# Remove the protocol.
url="${DATABASE_URL/$protocol/}"

# Extract the user and password.
user_pass="$(echo "$url" | grep @ | cut -d@ -f1)"
pass="$(echo "$user_pass" | grep : | cut -d: -f2)"
if [ -n "$pass" ]; then
  user="$(echo "$user_pass" | grep : | cut -d: -f1)"
else
    user=$user_pass
fi

# Extract the host and port.
host_port="$(echo "${url/$user_pass@/}" | cut -d/ -f1)"
# By request host without port.
host="$(echo "$host_port" | sed -e 's,:.*,,g')"
# Extract the port.
port="$(echo "$host_port" | sed -e 's,^.*:,:,g' -e 's,.*:\([0-9]*\).*,\1,g' -e 's,[^0-9],,g')"
# Extract the path.
path="$(echo "$url" | grep / | cut -d/ -f2-)"

# Init the app.
java -Djava.security.egd=file:/dev/./urandom \
      -Dserver.port=$PORT \
      -Dspring.datasource.url="jdbc:postgresql://$host:$port/$path?user=$user&password=$pass" \
      -jar /app/descartes-*.jar \
      "web"

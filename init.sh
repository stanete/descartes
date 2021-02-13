#!/bin/bash
set -e

java -Djava.security.egd=file:/dev/./urandom \
      -Dserver.port=$PORT \
      -Dspring.datasource.url="jdbc:${DATABASE_URL}" \
      -jar /app/descartes-*.jar \
      "web"

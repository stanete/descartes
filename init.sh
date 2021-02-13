#!/bin/bash
set -e

java -Djava.security.egd=file:/dev/./urandom \
      -Dserver.port=$PORT \
      -Dspring.datasource.url="jdbc:${${DATABASE_URL}/postgres/postgresql}" \
      -jar /app/descartes-*.jar \
      "web"

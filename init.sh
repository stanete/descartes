#!/bin/bash
set -e

java -Djava.security.egd=file:/dev/./urandom \
      -Dserver.port=$PORT \
      -Dspring.datasource.url=${DATABASE_URL} \
      -jar /app/descartes-*.jar \
      "web"

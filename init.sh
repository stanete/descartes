#!/bin/bash
set -e

java -Djava.security.egd=file:/dev/./urandom \
      -Dserver.port=$PORT \
      -jar /app/descartes-*.jar \
      "web"

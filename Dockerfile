FROM gradle:6.8.3 as builder
USER root
ENV APP_DIR /app
WORKDIR $APP_DIR
COPY build.gradle.kts $APP_DIR/
COPY settings.gradle.kts $APP_DIR/
RUN gradle dependencies
COPY . $APP_DIR
RUN gradle build -x test
USER guest

FROM openjdk:13-slim-buster
WORKDIR /app
COPY --from=builder /app/init.sh /app
COPY --from=builder /app/build/libs/descartes-*.jar /app/
COPY --from=builder /app/db/ db/
ENTRYPOINT ["bash", "init.sh"]

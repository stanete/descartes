#!/usr/bin/make -f

PROJECT_NAME := 'Descartes'
.DEFAULT_GOAL := help

ANALYSER_APIKEY?="REPLACE_ME"
ONLY?="REPLACE_ME"

run-db: ## Runs PostgreSQL database withing a Docker container.
	docker-compose up -d db

stop-db: ## Stops the PostgreSQL database running withing a Docker container.
	docker-compose stop db

run-broker: ## Runs Rabbitmq management withing a Docker container.
	docker-compose up -d rabbitmq

stop-broker: ## Stops the Rabbitmq management running withing a Docker container.
	docker-compose stop rabbitmq

migrate: run-db ## Runs migrations after checking the PostgreSQL database is up.
	./gradlew flywayClean flywayMigrate

clean: ## Cleans all the migrations of the PostgreSQL database.
	./gradlew flywayClean

lint: ## Checks & enforces Kotlin code style.
	./gradlew detekt

test: run-db migrate ## Runs test suite.
ifeq ($(ONLY),"REPLACE_ME")
	./gradlew test flywayClean
else
	./gradlew test --tests $(ONLY) flywayClean
endif


set-analyser-apikey: ## Sets the text analyser api key as an environment variable.
ifeq ($(ANALYSER_APIKEY),"REPLACE_ME")
	$(error ANALYSER_APIKEY is not set. Pass it as an argument)
else
	export ANALYSER_APIKEY=$(ANALYSER_APIKEY)
endif

stop: ## Stops only the application running locally.
	./gradlew -stop

run: ## Runs only the application locally with gradle.
	./gradlew bootRun

up: set-analyser-apikey run-broker run-db clean migrate run ## Runs the application with gradle and its dependencies within Docker containers.
down: stop stop-broker clean stop-db ## Stops the application and all its dependencies cleaning the PostgreSQL database.

help: ## Display this help text
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

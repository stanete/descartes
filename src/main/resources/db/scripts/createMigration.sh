#!/usr/bin/env bash

SCRIPTNAME=$(basename $0)
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
CURRENT_FOLDER=$(pwd)
MIGRATIONS_FOLDER="../migration"
GRADLE_PROPERTIES_PATH="../../gradle.properties"
APGDIFF_PATH="./apgdiff.jar"
ORIGIN_BRANCH="origin/main"
OLD_SCHEMA="./old.sql"
NEW_SCHEMA="./new.sql"
PGDUMP=
MIGRATION_NAME=
MIGRATION_FILE=

function error_exit() {
    echo "${SCRIPTNAME}: ${1}" 1>&2
    exit 1
}

function install_apgdiff() {
    if ! [[ $(which curl) ]]
    then
        error_exit "curl is required to automatically install apgdiff. \
                    You can install curl and try again or install manually apgdiff. \
                    Save it in ./apgdiff.jar"
    fi
    curl -L https://github.com/fordfrog/apgdiff/releases/download/2.5.0-alpha.2/apgdiff-2.5.0-SNAPSHOT.jar -o ./${APGDIFF_PATH}
    echo "Apgdiff successfuly installed!"
}

function install_postgres() {
    if ! [[ $(which brew) ]]
    then
        error_exit "Homebrew is required to automatically install of Postgres.\
                    you can install homebrew and try again or install manually Postgres."
    fi
    brew install postgresql
    echo "Postgres successfully installed!"
}

function set_migration_name() {
    printf "\e[32mEnter a name for your new migration:\e[m\n"
    read -r MIGRATION_NAME
}

function set_pgdump_conf() {
    db_chain=`sed '/^\#/d' $GRADLE_PROPERTIES_PATH | grep 'flyway.url'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`
    db_user=`sed '/^\#/d' $GRADLE_PROPERTIES_PATH | grep 'flyway.user'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`
    db_name=${db_chain##*/}

    db_chain_nodb=$(echo $db_chain | sed -e "s/\/$db_name//g")
    db_port=${db_chain_nodb##*:}

    db_chain_noport=$(echo $db_chain_nodb | sed -e "s/:$db_port//g")
    db_host=${db_chain_noport##*/}

    PGDUMP="pg_dump --dbname=$db_name --username=$db_user -h $db_host -p $db_port -s"
}

function get_old_schema() {
    git checkout $ORIGIN_BRANCH
    cd ../../ && ./gradlew flywayClean && ./gradlew flywayMigrate
    cd $CURRENT_FOLDER
    git checkout $CURRENT_BRANCH
    $($PGDUMP --file=$OLD_SCHEMA)
}

function get_new_schema() {
    cd ../../ && ./gradlew flywayClean && ./gradlew clean && ./gradlew build -Dspring.profiles.active=dev
    cd $CURRENT_FOLDER
    $($PGDUMP --file=$NEW_SCHEMA)
}

function generate_migration() {
    MIGRATION_FILE=$MIGRATIONS_FOLDER/V$(date +'%Y%m%d%H%m')__$MIGRATION_NAME.sql
    echo "--- -------------------------------------------------------------------------------------" >  $MIGRATION_FILE
    echo "--- WARNING! This is an automatically generated file. You must check migration content" >> $MIGRATION_FILE
    echo "--- before commit it. Some changes can't be handled by an automatic process." >> $MIGRATION_FILE
    echo "--- -------------------------------------------------------------------------------------" >> $MIGRATION_FILE
    java -jar $APGDIFF_PATH --ignore-start-with $OLD_SCHEMA $NEW_SCHEMA >> $MIGRATION_FILE
    sed -i '' '/flyway_schema_history/d' $MIGRATION_FILE #Removes flyway_history diff in migration file
}

function clean() {
    rm ./*.sql
}

# Apgdiff dependency installation
if [ ! -f ${APGDIFF_PATH} ];
then
    install_apgdiff
fi

# Pgdump dependency installation
if ! [[ $(which pg_dump) ]];
then
    install_postgres
fi

# Check proper branch
if [[ CURRENT_BRANCH == $ORIGIN_BRANCH ]];
then
     echo "Your current branch and origin branch is the same '$ORIGIN_BRANCH'. Can't compare a branch with itself."
     exit 0
fi

# Check no changes in your current branch
if [[ $(git status --porcelain) ]];
then
  echo "To perform this operation there can't be uncommitted changes; please commit them."
  exit 0
fi

# Main flow
set_migration_name
set_pgdump_conf
get_old_schema
get_new_schema
generate_migration
clean

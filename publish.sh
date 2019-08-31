#!/bin/bash

echo "THIS PROCEDURE IS DEPRECATED. FOR RELEASE WE USE CI/CD PIPELINE."
read

MY_PATH="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/$(basename "${BASH_SOURCE[0]}")"
MY_DIR="$(dirname $MY_PATH)"
echo $MY_DIR

cd $MY_DIR/berlioz-sdk
mvn clean deploy

cd $MY_DIR/sql
mvn clean deploy

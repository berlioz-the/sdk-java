#!/bin/bash

MY_PATH="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/$(basename "${BASH_SOURCE[0]}")"
MY_DIR="$(dirname $MY_PATH)"
echo $MY_DIR

cd $MY_DIR/berlioz-sdk
mvn clean deploy

cd $MY_DIR/sql
mvn clean deploy

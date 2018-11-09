#!/bin/sh
###############################################################################
# Maven command with centos 7 image behind it.
###############################################################################
# This command makes it possible to do a maven build on your local maven
# project but with centos architecture. The reason for this command is that
# some projects need the Rados native bindings in order to be able to build and
# test. Rados is the protocol to talk to Ceph. On centos based images this
# native binding is easy to install but on windows and mac very difficult.
# This command makes it possible to do a "normal" maven build with the bindings
# available. As java makes bytecode it will be cross platform and completely
# ok.
###############################################################################
# SOURCE: https://dzone.com/articles/maven-build-local-project-with-docker-why
#
PARAMS="$@"
if [ -z "$PARAMS" ]; then
    echo "[ERROR] Syntaxis $0 [clean|install|package|verify|compile|test]"
    echo "Standard maven commands are allowed"
    echo "Do not do a release with this command but let the build server to it"
    echo "This command has no access to the git / nexus credentials"
    exit 1
fi
docker run --rm  \
           --memory=1024g \
           -v /d/Repos/berlioz-corp/sdk-java.git/public-sample-1:/project \
           -v /c/Users/Ruben/.m2/repository:/repository \
           ivonet/centos-maven:7-3.0.5 mvn $PARAMS
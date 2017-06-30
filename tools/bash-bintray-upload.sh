#!/bin/sh

./gradlew decorator:bintrayUpload -PbintrayUser=$1 -PbintrayKey=$2 -PdryRun=$3
./gradlew no-op:bintrayUpload -PbintrayUser=$1 -PbintrayKey=$2 -PdryRun=$3
./gradlew value:bintrayUpload -PbintrayUser=$1 -PbintrayKey=$2 -PdryRun=$3

./gradlew common:bintrayUpload -PbintrayUser=$1 -PbintrayKey=$2 -PdryRun=$3
./gradlew decorator-compiler:bintrayUpload -PbintrayUser=$2 -PbintrayKey=$1 -PdryRun=$3
./gradlew no-op-compiler:bintrayUpload -PbintrayUser=$1 -PbintrayKey=$2 -PdryRun=$3

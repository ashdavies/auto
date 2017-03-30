#!/bin/sh

if [ $TRAVIS_BRANCH = "master" ]; then
  echo "Deploying branch $TRAVIS_BRANCH"
  ./bash-bintray-upload.sh
else
  echo "Not deploying $TRAVIS_BRANCH"
fi

#!/usr/bin/env bash

if [ "$TRAVIS_REPO_SLUG" == "ashdavies/auto" ] && \
   [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && \
   [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
  openssl aes-256-cbc -K $encrypted_977df3c49344_key -iv $encrypted_977df3c49344_iv -in $TRAVIS_BUILD_DIR/secring.gpg.enc -out $TRAVIS_BUILD_DIR/secring.gpg.enc -d
  ./gradlew uploadArchives \
      -Psigning.keyId=$signing_key_id \
      -Psigning.password=$signing_password \
      -Psigning.secretKeyRingFile=$TRAVIS_BUILD_DIR/secring.gpg.enc
fi

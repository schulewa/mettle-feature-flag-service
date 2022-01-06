#!/bin/bash

BIN_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

if [[ x"${KEY_STORE}" = x || x"${KEY_STORE_PASSWORD}" = x || x"${KEY_ALIAS}" = x || x"${KEY_PASSWORD}" = x || x"${KEY_PASSPHRASE}" ]]
then
  . ${BIN_DIR}/set-env.sh
fi

echo "Checking for JKS store"
if [[ ! -r ${KEY_STORE} ]]
then
  echo "Creating JKS key store in ${KEY_STORE}"
  keytool -genkey -alias ${KEY_ALIAS} -keyalg RSA -keysize 4096 -storetype JKS -keystore ${KEY_STORE} -validity 3650 -storepass ${KEY_STORE_PASSWORD}
fi

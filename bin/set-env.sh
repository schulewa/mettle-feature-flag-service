#!/bin/bash

echo Setting environment for Mettle Feature Flag Service

# override the port used for SSL
# export PORT=8443

# for Production inject these values from a secure location such as Hashicorp Vault
export KEY_STORE=jks_store.jks
export KEY_STORE_PASSWORD=password
export KEY_ALIAS=mettlefeatureflagservice
export KEY_PASSWORD=password
export KEY_PASSPHRASE=password



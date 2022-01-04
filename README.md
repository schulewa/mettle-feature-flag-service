# Mettle Feature Flag Service

## Pre-requisites

Ensure the following software is installed:
- Java 11
- gradle
- keystore (for generating SSL certificates)
- change the file permissions for bin/*.sh from the project root directory
  - chmod 750 bin/*.sh

## Create JKS keystore and certificate

Create a JKS keystore by running the shell script from the project root directory:
  bin/create-jks.sh

Follow the prompts from the keytool command for values you need to supply.

The JKS store must be placed on the classpath in order for it to be found. For development purposes it can be 
placed in folder src/main/resources or in src/test/resources for testing purposes.

Remember to copy file jks_store.jks from the project root directory to either src/main/resources to run live else
copy to src/test/resources for use in testing.

## How to build locally
- either build using your IDE or
- build from the command line using command
  - gradlew build

## How to run locally
- either run from within your IDE or
- build from the command line using command
  - gradlew run

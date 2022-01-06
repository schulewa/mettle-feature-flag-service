# Mettle Feature Flag Service

The Mettle Feature Flag Service provides the ability to maintain a list of feature flags. All flags are held in an in-memory database (for this POC).

Security has been disabled in this initial version with the objective being to protect REST endpoints for ADMIN and non-ADMIN roles at a later date.
The checks for whether the user hasAuthority have been commented out in the FeatureFlagController.

## Pre-requisites

Ensure the following software is installed:
- Java 11
- gradle
- keystore (for generating SSL certificates)
- change the file permissions for bin/*.sh from the project root directory
  - chmod 750 bin/*.sh
- run command bin/create-jks.sh to create the jks_store.jks under the project src/main/resources

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
Ensure you are in the project directory and have executed bin/set-env.sh to set required environment variables, then:
- either run from within your IDE (having set the environment variables as per bin/set-env.sh) or
- build from the command line using command and then run
  - gradlew run

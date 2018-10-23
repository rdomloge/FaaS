#Each of these mount points are specific dependencies for Ramsay's functions
#Martin - you'll need to create your own
MOUNT1=/Users/rdomloge/.m2:/Users/rdomloge/.m2
MOUNT2=/Users/rdomloge/Documents/workspace/FaaS/src/main/test-functions/:/src/main/test-functions
MOUNT3=/Users/rdomloge/Documents/workspace/FaaS/src/main/test-functions:/Users/rdomloge/Documents/workspace/FaaS/src/main/test-functions

# --network faasnet
# This must be the same as the container network for the Postgres container
# Then, we set the JDBC connection URL host to be the name of the container that Postgres is running
# under. This is hardcoded ATM to postgrescontainer.
docker run -v $MOUNT1  -v $MOUNT2 -v $MOUNT3 --network faasnet -ti faas:faas
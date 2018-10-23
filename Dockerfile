FROM faas:vm
VOLUME /tmp
ARG FN_FILE
COPY target/faas.war faas.war
COPY src/main/resources/${FN_FILE} functions.xml

RUN apk update
RUN apk add curl

ARG M2_HOME
RUN mkdir execution-workspace
RUN mkdir libs
RUN curl http://central.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.8.6/jackson-core-2.8.6.jar --output libs/jackson-core.jar
RUN curl http://central.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.8.6/jackson-databind-2.8.6.jar --output libs/jackson-databind.jar 
RUN curl http://central.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.8.0/jackson-annotations-2.8.0.jar --output libs/jackson-annotations.jar 

EXPOSE 8080

# Run the jar file 
ENTRYPOINT ["java","-jar","-Dtarget.root.path=/execution-workspace",\
"-Dapi.lib.path=./faas-api.jar",\
"-Dfork.vm.lib.path=./faas-vm.jar",\
"-Djackson.lib.paths=libs/jackson-core.jar,libs/jackson-databind.jar,libs/jackson-annotations.jar",\
"-DRMQ_USER=faas-gw",\
"-DRMQ_PASSWORD=0pen5esame",\
"-Dfunctions.xml.file=functions.xml",\
"faas.war"]

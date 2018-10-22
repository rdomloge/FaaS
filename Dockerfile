FROM ubuntu

RUN apt-get update

RUN apt-get install -y git

RUN apt-cache search maven
RUN apt-get install -y maven

RUN git clone git://github.com/rdomloge/FaaS-DTO.git
RUN mvn -f FaaS-DTO/pom.xml install

RUN git clone git://github.com/rdomloge/FaaS-API.git
RUN mvn -f FaaS-API/pom.xml install

RUN git clone git://github.com/rdomloge/FaaS-VM.git
RUN mvn -f FaaS-VM/pom.xml install

RUN git clone git://github.com/rdomloge/FaaS.git
RUN mvn -f FaaS/pom.xml package

EXPOSE 8080

RUN mkdir execution-workspace
RUN mkdir libs
ENV JACKSON_HOME /root/.m2/repository/com/fasterxml/jackson/core 
RUN ln -s $JACKSON_HOME/jackson-core/2.8.6/jackson-core-2.8.6.jar libs/jackson-core.jar
RUN ln -s $JACKSON_HOME/jackson-databind/2.8.6/jackson-databind-2.8.6.jar libs/jackson-databind.jar
RUN ln -s $JACKSON_HOME/jackson-annotations/2.8.0/jackson-annotations-2.8.0.jar libs/jackson-annotations.jar

# Run the jar file 
ENTRYPOINT ["java","-jar","-Dtarget.root.path=/execution-workspace",\
"-Dapi.lib.path=/FaaS-API/target/faas-api.jar",\
"-Dfork.vm.lib.path=/FaaS-VM/target/faas-vm.jar",\
"-Djackson.lib.paths=libs/jackson-core.jar,libs/jackson-databind.jar,libs/jackson-annotations.jar",\
"-DRMQ_USER=faas-gw",\
"-DRMQ_PASSWORD=0pen5esame",\
"FaaS/target/faas.war"]

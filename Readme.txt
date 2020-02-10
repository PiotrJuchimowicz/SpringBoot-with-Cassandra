Application is secured with basic auth. - Credentials are in application.properties file.

If you want to run this app in local developer environment:

1. Change in application.properties file:
spring.data.cassandra.contact-points = localhost
2. Get Cassandra image:
docker run -p 9042:9042 --rm --name cassandra -d cassandra:3.11
3. Enter Cassandra container shell:
docker exec -it cassandra bash
4. Type cqlsh and copy below script to initialize keyspace:(script also in resources directory with name: create-keyspace.cql)
CREATE KEYSPACE IF NOT EXISTS test
WITH durable_writes = true
AND replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};
5. Run mvn clean package

If you want to make docker image of this app and run it in container:
1. Change in application.properties file:
spring.data.cassandra.contact-points = cassandra
2. Get Cassandra image and configure it:
docker network create messaging-cassandra
docker run --network messaging-cassandra -p 9042:9042 --rm --name cassandra -d cassandra:3.11
3. Enter Cassandra container shell:
docker exec -it cassandra bash
4.Type cqlsh and copy below script to initialize keyspace:(script also in resources directory with name: create-keyspace.cql)
CREATE KEYSPACE IF NOT EXISTS test
WITH durable_writes = true
AND replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};
5. If you use Ubuntu make sure that you have properly DNS configured.
6. Make executable jar:
mvn clean install -DskipTests
7.Build image:
docker build -f Dockerfile -t messaging-app .
8.Run container:
docker container run --network messaging-cassandra --name messaging-app  -p 8080:8080 -p 587:587 -d messaging-app



 docker build -t java .
 
  docker run -d -it --name sync-server \
  -v /usr/local/sync-server/debezium-kafka-clickhouse-sync-1.0.4.jar:/starter.jar \
  -v /usr/local/sync-server/application.properties:/application.properties \
   java

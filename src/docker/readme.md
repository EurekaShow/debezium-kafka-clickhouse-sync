docker build -t sync .

docker run -d -it --name sync -p 8080:8080 \
-v /usr/local/sync/debezium-kafka-clickhouse-sync-1.0.4.jar:/debezium-kafka-clickhouse-sync-1.0.4.jar \
-v /usr/local/sync/application.properties:/application.properties \
-v /usr/local/sync/logback.xml:/logback.xml \
 sync

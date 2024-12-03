
# ----------------Apache Kafka--------------------------
# Zookeeper start
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

# Kafka Start
.\bin\windows\kafka-server-start.bat .\config\server.properties

# Topic  Creation
bin\windows> kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic sm-test-topic
bin\windows> kafka-topics.bat --create --bootstrap-server localhost:9092 --partitions 3 --topic smtest

# Topic description
kafka-topics.bat --bootstrap-server localhost:9092 --topic smtest --describe

# List of topic in 9092
.\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --list

kafka-broker-api-versions.bat --bootstrap-server localhost:9092

# For zookeeper related and broker related information
C:\kafka_store\apache_kafka\bin\windows> zookeeper-shell.bat localhost:2181
Type : ls /brokers/ids

# Producer creation
kafka-console-producer.bat --broker-list localhost:9092 --topic test

# Consumer
kafka-console-consumer.bat --topic smtest --bootstrap-server localhost:9092 --from-beginning

# ---------------Confluent Kafka---------------------

# Start Zookeeper Server
.\bin\windows\zookeeper-server-start.bat .\etc\kafka\zookeeper.properties

# Start Kafka Server / Broker
.\bin\windows\kafka-server-start.bat .\etc\kafka\server.properties

# Create topic
kafka-topics --bootstrap-server localhost:9092 --create --topic NewTopic1 --partitions 3 --replication-factor 1

## list out all topic names
kafka-topics --bootstrap-server localhost:9092 --list

## Describe topics
kafka-topics --bootstrap-server localhost:9092 --describe --topic NewTopic1

# Produce message
kafka-console-producer --broker-list localhost:9092 --topic NewTopic1
## Using round-robin partition for message(don't recommend this, it is for testing, not for production)
kafka-console-producer --broker-list localhost:9092 --topic NewTopic1 --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner
## Produce With Key
kafka-console-producer --broker-list localhost:9092 --topic sm-test-topic --property parse.key=true --property key.separator=:
ex name:Champa

# Consume message
kafka-console-consumer --bootstrap-server localhost:9092 --topic NewTopic1 --from-beginning

# Send CSV File data to kafka
kafka-console-producer --broker-list localhost:9092 --topic NewTopic1 < sample_data.csv

# Kafka with KRaft
# Generate a Cluster UUID
$KAFKA_CLUSTER_ID="$(bin/windows/kafka-storage.bat random-uuid)"
# Format Log Directories
.\bin\windows\kafka-storage.bat format -t $KAFKA_CLUSTER_ID -c .\etc\kafka\kraft\server.properties

ERROR SOL - If Kafka_CLUSTER_ID is not matching, delete metadeta.properties frorm kraft-combined log and run above two cmd again in powershell

# Start the Kafka Server
.\bin\windows\kafka-server-start.bat .\etc\kafka\kraft\server.properties

# ERROR SOl LINK
https://medium.com/@praveenkumarsingh/confluent-kafka-on-windows-how-to-fix-classpath-is-empty-cf7c31d9c787

# To decode a log files we need to use the kafka-dump-log.bat
.\bin\windows\kafka-dump-log.bat --cluster-metadata-decoder --files .\kraft-combined-logs\"__cluster_metadata-0"\"00000000000000000000.log" --print-data-log

# For Kalfka cluster and replication factor (file -> kafka-metadata-quorum.bat)
.\bin\windows\kafka-metadata-quorum.bat --bootstrap-server localhost:9092 describe --status
.\bin\windows\kafka-metadata-quorum.bat --bootstrap-server localhost:9092 describe --replication

# Consumer Group (--group parameter) [This will divide the msg w.r.t partitions, in different consumers in a group]
## Start consumer with the group id/name
kafka-console-consumer --bootstrap-server localhost:9092 --topic <topic-name> --group <group-name-id>
## Start another consumer with the same group id/name
kafka-console-consumer --bootstrap-server localhost:9092 --topic <topic-name> --group <group-name-id>

# Command - kafka-consumer-group.bat
## list consumer groups
kafka-consumer-groups --bootstrap-server localhost:9092 --list
## describe one specific group
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group con-group-app-1
## describe another group
kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group con-group-app-2

## NOTE
## IF Consumer absorb all message from producer and updated, "LAG" will be zero, else LAG will be more than zero
## ALSO, CONSUMER-ID indicate which partition's message is coming to that consumer.
## CURRENT-OFFSET will show the end position the msg has been read

## Reset offsets
### Dry Run [Only show results without executing changes on Consumer Groups. Supported operations: reset-offsets.]
kafka-consumer-groups --bootstrap-server localhost:9092 --group con-group-app-2 --reset-offsets --to-earliest --topic sm-test-topic-2 --dry-run
### Execute flag is needed
kafka-consumer-groups --bootstrap-server localhost:9092 --group con-group-app-2 --reset-offsets --to-earliest --topic sm-test-topic-2 --execute


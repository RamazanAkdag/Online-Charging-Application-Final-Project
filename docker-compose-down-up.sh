sudo docker-compose -f hazelcast-compose.yml -f ignite-compose.yaml -f kafka-compose.yml down --remove-or

sudo docker-compose -f ignite-compose.yaml up -d

sudo docker-compose -f hazelcast-compose.yml up -d

sudo docker-compose -f kafka-compose.yml up -d


sudo docker-compose -f hazelcast-compose.yml -f ignite-compose.yml -f kafka-compose.yml down --remove-or

sudo docker-compose -f ignite-compose.yml up -d

sudo docker-compose -f hazelcast-compose.yml up -d

sudo docker-compose -f kafka-compose.yml up -d


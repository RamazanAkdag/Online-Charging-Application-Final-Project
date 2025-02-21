docker-compose -f docker-compose.yml down
docker-compose -f hazelcast-compose.yml down
docker-compose -f ignite-compose.yaml down

docker-compose -f docker-compose.yml -f hazelcast-compose.yml -f ignite-compose.yaml down

docker-compose -f ignite-compose.yaml up -d

docker-compose -f hazelcast-compose.yml up -d


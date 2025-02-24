

docker network prune -f
docker container prune -f
docker volume prune -f


sudo docker-compose -f docker-compose.yml -f hazelcast-compose.yml -f ignite-compose.yaml down --remove-or

sudo docker-compose -f ignite-compose.yaml up -d

sudo docker-compose -f hazelcast-compose.yml up -d

sudo docker-compose -f kafka-compose.yml up -d


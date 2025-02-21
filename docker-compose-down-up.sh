
sudo docker-compose -f docker-compose.yml -f hazelcast-compose.yml -f ignite-compose.yaml down

sudo docker-compose -f ignite-compose.yaml up -d

sudo docker-compose -f hazelcast-compose.yml up -d


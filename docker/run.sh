#/bin/sh

docker run -p 5038:5038 --name asterisk_1 -d asterisk /bin/sh -c "while true; do ping 8.8.8.8; done"
docker start asterisk_1
docker exec asterisk_1 asterisk

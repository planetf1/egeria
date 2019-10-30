<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Running Apache Kafka in Docker.  

This demo was created on an MacOS machine.    
  

## Get Docker
Install Docker for Mac. https://docs.docker.com/docker-for-mac/install/


## Get the Kafka image
On http://wurstmeister.github.io/kafka-docker/ download the tar.gz image and expanded it into a folder

#### Machine setup  
Update your /etc/hosts to add lines

```text
127.0.0.1   localhost
127.0.0.1   kafka
```

Open file docker-compose-single-broker.yml and replace it's contents with : 

```yaml
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    hostname: zookeeper
    ports:
      - "2181:2181"
  kafka:
    build: .
    hostname: kafka
    ports:
      - "9092:9092"
    environment:
       KAFKA_ADVERTISED_HOST_NAME: <local ip>
       KAFKA_ADVERTISED_PORT: 9092
       KAFKA_CREATE_TOPICS: "test:1:1,cocoCohort:1:1"
       KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
       KAFKA_ADVERTISED_LISTENERS: "PLAINTEXT://kafka:9092"
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
```

This file defines the configuration for your Kafka. Note that
  - 'hostname: kafka' means that Kafka machine in the docker container will have hostname kafka.
  - The KAFKA_ADVERTISED... are what Kafka needs to be known as outside its machine.
  -  You need to replace <local ip> with your local ip or hostname (or your Mac)
  - 'KAFKA_CREATE_TOPICS: "test:1:1,cocoCohort:1:1"' creates 2 topics test and cocoCohort. 
  - this yml starts Kafka with one broker. 
         
#### Starting up the environment

```bash
$ docker-compose -f docker-compose-single-broker.yml up -d  --force-recreate
```

#### Check it is running 

```bash
$ docker container ls
CONTAINER ID        IMAGE                                     COMMAND                  CREATED             STATUS              PORTS                                                NAMES
bfcbcf26e260        wurstmeister/zookeeper                    "/bin/sh -c '/usr/sb…"   3 minutes ago       Up 3 minutes        22/tcp, 2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp   wurstmeister-kafka-docker-39f4872_zookeeper_1
38be12058832        wurstmeister-kafka-docker-39f4872_kafka   "start-kafka.sh"         3 minutes ago       Up 3 minutes        0.0.0.0:9092->9092/tcp                               wurstmeister-kafka-docker-39f4872_kafka_1
```

or

```bash
$ docker ps
```

or 

```bash
$ docker-compose-ps
```

#### Check the topics are there

```bash
$ docker ps
```

- get the container id then put it in the following command to list topics from within the container.

    ```bash
    $ docker exec 92c55ad44ab9   /opt/kafka_2.12-2.0.0/bin/kafka-topics.sh --list --zookeeper zookeeper:2181
    ```

#### Docker logs 

```bash
$ docker-compose logs 
```

#### Start a prompt from within docker.

```bash
$ docker exec -t -i <image id from docker ps> /bin/bash
```

#### Deleting the containers images

List the containers 

```bash
$ docker ps -a
```

Get the containerid and remove it with this command

```bash 
$ docker rm <containerid>
```

Delete all containers

```bash
$ docker rm $(docker ps -a -q)
```

Delete all images 

```bash
$ docker rmi $(docker images -q)
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

#!/bin/bash
nohup java -DapplicationConfig=application.properties -DkafkaConfig=kafka.properties -jar trp-event-resolver.jar 2>&1 < /dev/null &
#!/bin/bash
echo "构建镜像 ..."
mvn clean package -X -U -Dmaven.test.skip=true

docker build -t registry.cn-qingdao.aliyuncs.com/x-lab/riskscanner:v1.6.0 .
docker push registry.cn-qingdao.aliyuncs.com/x-lab/riskscanner:v1.6.0
docker images|grep riskscanner|awk '{print "docker rmi -f "$3}'|sh

mvn clean package -X -U -Dmaven.test.skip=true

docker build -t registry.cn-qingdao.aliyuncs.com/x-lab/riskscanner/riskscanner:master .
docker push registry.cn-qingdao.aliyuncs.com/x-lab/riskscanner/riskscanner:master
docker images|grep riskscanner/riskscanner|awk '{print "docker rmi -f "$3}'|sh
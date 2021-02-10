echo "构建镜像 ..."
mvn clean package -X -U -Dmaven.test.skip=true

docker build -t registry.fit2cloud.com/riskscanner/riskscanner:riskscanner .
docker push registry.fit2cloud.com/riskscanner/riskscanner:riskscanner
docker images|grep riskscanner/riskscanner|awk '{print "docker rmi -f "$3}'|sh
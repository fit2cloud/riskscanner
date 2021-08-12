FROM registry.cn-qingdao.aliyuncs.com/x-lab/nuclei:v1.4 as build-env

FROM registry.cn-qingdao.aliyuncs.com/x-lab/custodian:v1.4.1

ARG RS_VERSION=dev

RUN mkdir /var/cache/apk && apk add --no-cache bind-tools ca-certificates

COPY --from=build-env /usr/local/bin/nuclei /usr/local/bin/nuclei

RUN mkdir -p /opt/apps

COPY backend/target/backend-1.0.jar /opt/apps

ENV JAVA_APP_JAR=/opt/apps/backend-1.0.jar

ENV AB_OFF=true

ENV RS_VERSION=${RS_VERSION}

ENV JAVA_OPTIONS="-Dfile.encoding=utf-8 -Djava.awt.headless=true"

HEALTHCHECK --interval=15s --timeout=5s --retries=20 --start-period=30s CMD curl -f 127.0.0.1:8088

CMD ["/deployments/run-java.sh"]

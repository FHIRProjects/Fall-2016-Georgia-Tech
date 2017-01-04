FROM centos:latest
EXPOSE 8080
COPY . /tmp/
RUN yum update && yum install -y wget bzip2 && \
wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" \
"http://download.oracle.com/otn-pub/java/jdk/8u60-b27/jre-8u60-linux-x64.rpm" && \
yum localinstall -y jre-8u60-linux-x64.rpm && \
export JAVA_HOME=/usr/java/jre1.8.0_60 && \
wget https://github.com/jamesagnew/hapi-fhir/releases/download/v2.1/hapi-fhir-2.1-cli.tar.bz2 && \
tar xvjf hapi-fhir-2.1-cli.tar.bz2
ENTRYPOINT ./hapi-fhir-cli run-server -f dstu3
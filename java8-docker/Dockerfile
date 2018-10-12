FROM centos:6.8

MAINTAINER edwon <wangwenquan@nj.fiberhome.com.cn>

RUN yum update -y && \
yum install -y wget && \
wget --no-cookies --no-check-certificate \
  --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" \
   "http://download.oracle.com/otn-pub/java/jdk/8u102-b14/jdk-8u102-linux-x64.rpm" && \
yum localinstall -y jdk-8u102-linux-x64.rpm && \
rm -f jdk-8u102-linux-x64.rpm && \
yum clean all

# Set time zone
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /root

# Define default command.
CMD ["bash"]


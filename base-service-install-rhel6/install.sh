#!/bin/bash

INSTALL_DIR=`pwd`
FASTDFS_PATH=${INSTALL_DIR}/fastDFS
FASTDFS_BASE_PATH=/data
NGINX_VERSION=1.8.1
PCRE_VERSION=8.38
ZLIB_VERSION=1.2.8
OPENSSL_VERSION=1.0.2g

echo ">>>Install gd-devel......"
unzip -o ${INSTALL_DIR}/soft/gd-devel.zip
rpm -qa | grep libxcb | xargs rpm -e --nodeps
rpm -ivh --force ${INSTALL_DIR}/gd-devel/*.rpm
echo "<<<gd-devel installed."

echo ">>>Install fastdfs......"
cd  ${INSTALL_DIR}
echo "#compile the libfastcommon"
unzip -o ${INSTALL_DIR}/soft/libfastcommon-master.zip
cd  ${INSTALL_DIR}/libfastcommon-master
./make.sh && ./make.sh install

echo "#compile the fastdfs"
cd  ${INSTALL_DIR}
unzip -o ${INSTALL_DIR}/soft/fastdfs-master.zip
cd  ${INSTALL_DIR}/fastdfs-master
./make.sh && ./make.sh install

mkdir -p ${FASTDFS_PATH}
mkdir -p ${FASTDFS_BASE_PATH}
mkdir -p ${FASTDFS_PATH}/nginx
mkdir -p ${FASTDFS_PATH}/lib

unzip -o ${INSTALL_DIR}/soft/fastdfs-nginx-module-master.zip -d ${FASTDFS_PATH}/lib
tar zxvf ${INSTALL_DIR}/soft/nginx-${NGINX_VERSION}.tar.gz -C ${FASTDFS_PATH}/nginx
tar zxvf ${INSTALL_DIR}/soft/openssl-${OPENSSL_VERSION}.tar.gz -C ${FASTDFS_PATH}/lib
tar zxvf ${INSTALL_DIR}/soft/pcre-${PCRE_VERSION}.tar.gz -C ${FASTDFS_PATH}/lib
tar zxvf ${INSTALL_DIR}/soft/zlib-${ZLIB_VERSION}.tar.gz -C ${FASTDFS_PATH}/lib

cd ${FASTDFS_PATH}/nginx/nginx-${NGINX_VERSION}
./configure --with-pcre=${FASTDFS_PATH}/lib/pcre-${PCRE_VERSION} \
              --with-zlib=${FASTDFS_PATH}/lib/zlib-${ZLIB_VERSION} \
              --with-openssl=${FASTDFS_PATH}/lib/openssl-${OPENSSL_VERSION} \
              --with-http_ssl_module \
              --with-debug \
              --with-http_image_filter_module \
              --add-module=${FASTDFS_PATH}/lib/fastdfs-nginx-module-master/src \
 && make \
 && make install
rm -fr ${FASTDFS_PATH}
echo "<<<fastdfs installed."

echo ">>>copy configure files......"
echo "#unzip fastdfs config file to /etc/fdfs"
unzip -o ${INSTALL_DIR}/soft/fastdfs-conf.zip -d /etc/fdfs

echo "#unzip nginx config file to /usr/local/nginx/conf/"
unzip -o ${INSTALL_DIR}/soft/nginx-conf.zip -d /usr/local/nginx/conf/
echo "<<<configure files copied."


echo "!!!!Please input the ip address of this machine:"
read ipaddress

echo "The ip is: $ipaddress, and whill set in /etc/fdfs/storage.conf & /etc/fdfs/mod_fastdfs.conf"
sed -i "s/192.168.160.162/$ipaddress/g" /etc/fdfs/storage.conf
sed -i "s/192.168.160.162/$ipaddress/g" /etc/fdfs/mod_fastdfs.conf
sed -i "s/192.168.160.162/$ipaddress/g" /etc/fdfs/client.conf
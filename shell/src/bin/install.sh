#/bin/sh

APP_NAME=${project.name}
JAR_NAME=${project.build.finalName}
CUR_PATH=`pwd`
cd `dirname $0`
APP_PATH=`pwd`

userinstall=mapps
group=fiberhome  
  
#create group if not exists  
egrep "^$group" /etc/group >& /dev/null  
if [ $? -ne 0 ]  
then  
    groupadd $group  
fi  
  
#create user if not exists  
egrep "^$userinstall" /etc/passwd >& /dev/null  
if [ $? -ne 0 ]  
then  
    useradd -mg $group $userinstall  
fi
#usermod -G root $userinstall
echo "info:create new user[mapps] success   [  OK  ]"


basepath=$(cd `dirname $0`; pwd)
shpath=/install.sh
installpath=$basepath$shpath
confpath=$basepath/$JAR_NAME.conf
logpath=$basepath/logs
mkdir $logpath

OPTNUM=1

while [ 1 -eq 1 ]
do
	echo -n "Select install type:override-install:1 ,upgrade:2  "
	read OPTMODE
	if [[ "$OPTMODE" = "1" ]];then
		OPTNUM=1
		break
	fi
	if [[ "$OPTMODE" = "2" ]];then
		OPTNUM=2
		break
	fi                                       
done

if [[ "$OPTNUM" = "1" ]];then
	while [ 1 -eq 1 ]
	do
		echo -n "Select service mode:standalone-service:1(default) ,micro-services:2  "
		read INSTALLMODE
		if [[ "$INSTALLMODE" = "1" ]];then
			\cp -fr $basepath/application-standalone.yml $basepath/config/application.yml
			break
		fi
		if [[ "$INSTALLMODE" = "2" ]];then
			\cp -fr $basepath/application-cloud.yml $basepath/config/application.yml
			break
		fi                                       
	done
fi

echo "current JAVA_OPTS:"
while read LINE
do
		echo $LINE
done  < $confpath
echo "----------------------------"

echo -n "modify settings?(recommend) y/n:"
read VAR1
if [[ "$VAR1" = "" ]];then
	VAR1="Y"
fi
if [[ "$VAR1" = "y" ]];then
	VAR1="Y"
fi
JAVA_H=`echo $JAVA_HOME`
if [[ "$JAVA_H" = "" ]];then
	JAVA_H=/opt/exmobi-thirdsoft/installed/java
fi
if [[ "$VAR1" = "Y" ]];then
	echo "----------------------------"
	echo "please enter whole JAVA_HOME(e.g. JAVA_HOME=$JAVA_H):"
	read content1
	sed -i "1c $content1" $confpath
	echo "----------------------------"
	echo "please enter whole JAVA_OPTS(e.g. JAVA_OPTS=\"-Xmx512M -Djava.security.egd=file:/dev/./urandom -Dspring.config.location=./config -Duser.timezone=Asia/Shanghai\"):"
	read content2
	sed -i "2c $content2" $confpath
	echo "----------------------------"
	echo "please enter whole RUN_ARGS(e.g. if not use-->#RUN_ARGS=\"\"):"
	read content3
	sed -i "3c $content3" $confpath
	echo "info:modify settings success         [  OK  ]"
fi

chown -R mapps:fiberhome ../$JAR_NAME
chmod u+x $APP_PATH/$JAR_NAME.jar
chmod 744 *.sh
ln -sf $APP_PATH/service.sh /etc/init.d/$APP_NAME
ln -sf /etc/init.d/$APP_NAME /etc/rc2.d/S87$APP_NAME
ln -sf /etc/init.d/$APP_NAME /etc/rc3.d/S87$APP_NAME
ln -sf /etc/init.d/$APP_NAME /etc/rc4.d/S87$APP_NAME
ln -sf /etc/init.d/$APP_NAME /etc/rc5.d/S87$APP_NAME

echo "***************************************************************************"
echo "* $APP_NAME has been installed. The service name is $APP_NAME.            *"
echo "* You can start|stop|restart with service command.                        *"
echo "* It will write console log to $JAR_NAME/logs/spring.log.                 *"
echo "***************************************************************************"

cd $CUR_PATH
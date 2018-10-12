#/bin/sh

APP_NAME=${project.name}
JAR_NAME=${project.build.finalName}
CUR_PATH=`pwd`
cd `dirname $0`
APP_PATH=`pwd`

chmod +x $APP_PATH/$JAR_NAME.jar
ln -sf $APP_PATH/service.sh /etc/init.d/$APP_NAME

echo "***************************************************************************"
echo "* $APP_NAME has been installed. The service name is $APP_NAME.            *"
echo "* You can start|stop|restart with service command.                        *"
echo "* It will write console log to /var/log/$APP_NAME.log.                   *"
echo "***************************************************************************"

cd $CUR_PATH
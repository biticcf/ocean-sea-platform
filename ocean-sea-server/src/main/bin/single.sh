#!/bin/bash

APP_NAME="proj_ocean-sea"

#取当前目录
BASE_PATH=`cd "$(dirname "$0")"; pwd`

#取外部数据
XMX_VAR=${Xmx}
DEVTOOLS_RESTART_ENABLED=${DEVTOOLS.RESTART.ENABLED}
#去掉结尾的G
XMX_VAR=${XMX_VAR%%G}
#数值*3/4,m
XMX_V=`expr $XMX_VAR \* 3072 / 4`
#数值*3/4,m
XMS_V=`expr $XMX_VAR \* 3072 / 4`
#数值*3/8,m
XMN_V=`expr $XMX_VAR \* 3072 / 8`

#设置java运行参数
#DEFAULT_JAVA_OPTS=" -server -Xmx${XMX_V}m -Xms${XMS_V}m -Xmn${XMN_V}m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+DoEscapeAnalysis -XX:+EliminateAllocations -XX:+EliminateLocks -Dfile.encoding=UTF-8 -Dspring.devtools.restart.enabled=${DEVTOOLS_RESTART_ENABLED}"
DEFAULT_JAVA_OPTS=" "

if (( XMX_VAR <= 4 )); then
	DEFAULT_JAVA_OPTS=" -server -Xmx${XMX_V}M  -Xms${XMS_V}M -Xmn${XMN_V}M -XX:MaxMetaspaceSize=256M -XX:MetaspaceSize=256M -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark -XX:+DoEscapeAnalysis -XX:+EliminateAllocations -XX:+EliminateLocks -XX:ErrorFile=./tmp/hs_err_pid%p.log   -Xloggc:./tmp/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -verbose:class -XX:+PrintCommandLineFlags -Dfile.encoding=UTF-8 -Dspring.devtools.restart.enabled=${DEVTOOLS_RESTART_ENABLED}"
elif (( XMX_VAR > 4 )); then
	DEFAULT_JAVA_OPTS=" -server -Xmx${XMX_V}M -Xms${XMS_V}M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled -XX:+DoEscapeAnalysis -XX:+EliminateAllocations -XX:+EliminateLocks -XX:ErrorFile=./tmp/hs_err_pid%p.log -Xloggc:./tmp/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -verbose:class -XX:+PrintCommandLineFlags -Dfile.encoding=UTF-8 -Dspring.devtools.restart.enabled=${DEVTOOLS_RESTART_ENABLED}"
fi

#定义变量:
APP_PATH=${APP_PATH:-`dirname "$BASE_PATH"`}
CLASS_PATH=${CLASS_PATH:-$APP_PATH/config:$APP_PATH/lib/*}
JAVA_OPTS=${JAVA_OPTS:-$DEFAULT_JAVA_OPTS}
DEFAULT_JAR=$(find $APP_PATH/lib/ -name *.jar)
LOG_PATH="${LOGGING_PATH}"
if [ ! -d "$LOG_PATH" ]; then 
	mkdir -p $LOG_PATH
fi

SERVER_ACCESSABLE_IP=$SERVER_ACCESSABLE_IP
if [ -z "$SERVER_ACCESSABLE_IP" ]; then
	SERVER_ACCESSABLE_IP=$(ifconfig eth0 |grep 'inet add'|awk -F ":" '{print $2}'|awk '{print $1}')
fi
SERVER_ACCESSABLE_PORT=$SERVER_ACCESSABLE_PORT
if [ -z "$SERVER_ACCESSABLE_PORT" ]; then
	SERVER_ACCESSABLE_PORT=10000
fi
export SERVER_ACCESSABLE_IP=${SERVER_ACCESSABLE_IP}
export SERVER_ACCESSABLE_PORT=${SERVER_ACCESSABLE_PORT}
export SERVER_ENVIROMENT=${PROJECTENV}
echo "APP_PATH=$APP_PATH"
echo "BASE_PATH=$BASE_PATH"
echo "DEFAULT_JAR=$DEFAULT_JAR"
echo "CLASS_PATH=$CLASS_PATH"
echo "traceXApi is started."

cd $APP_PATH
ulimit -HSn ${ULIMIT}

exist(){
			if test $( pgrep -f "$APP_NAME" | wc -l ) -eq 0 
			then
				return 1
			else
				return 0
			fi
}

start(){
		if exist; then
				echo "$APP_NAME is already running."
				exit 1
		else
	    	cd $APP_PATH
				nohup java $JAVA_OPTS -cp $CLASS_PATH -jar $DEFAULT_JAR $APP_NAME > /dev/null & 
				echo "$APP_NAME is started."
		fi
}

stop(){
		runningPID=`pgrep -f "$APP_NAME"`
		if [ "$runningPID" ]; then
				echo "$APP_NAME pid: $runningPID"
        count=0
        kwait=5
        echo "$APP_NAME is stopping, please wait..."
        kill -15 $runningPID
					until [ `ps --pid $runningPID 2> /dev/null | grep -c $runningPID 2> /dev/null` -eq '0' ] || [ $count -gt $kwait ]
		        do
		            sleep 1
		            let count=$count+1;
		        done

	        if [ $count -gt $kwait ]; then
	            kill -9 $runningPID
	        fi
        clear
        echo "$APP_NAME is stopped."
    else
    		echo "$APP_NAME has not been started."
    fi
}

check(){
   if exist; then
   	 echo "$APP_NAME is alive."
   	 exit 0
   else
   	 echo "$APP_NAME is dead."
   	 exit -1
   fi
}

restart(){
        stop
        start
}

case "$1" in

start)
        start
;;
stop)
        stop
;;
restart)
        restart
;;
check)
        check
;;
*)
        echo "available operations: [start|stop|restart|check]"
        exit 1
;;
esac

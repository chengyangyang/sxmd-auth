#!bin/bash

# 最简洁的启动
resource_name=summit-project-1.0-SNAPSHOT.jar

nohup java -jar ./target/$resource_name  >/dev/null 2>&1 &

echo $!>process.pid

echo start success!


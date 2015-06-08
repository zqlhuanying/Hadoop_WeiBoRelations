#!/bin/sh

# $1:名字，用于查询粉丝列表
# $2:jar 所在的路径，即需要向hadoop集群提交的作业
# $3:class 主程序入口
# $4:输出的文件路径

# 判断HDFS上某个目录或文件是否存在
hadoop fs -test -e $4
if [ $? -eq 0 ] ; then
    hadoop fs -rm -r $4
fi

# 提交作业
hadoop jar $2 $3 $1 $4
#!/bin/sh

# $1:HDFS上的文件
# $2:拷贝到本地目录

# 判断$1的文件是否存在，若存在，则拷贝
hadoop fs -test -e $1;

if [ $? -eq 0 ] ; then
    hadoop fs -copyToLocal $1 $2;
fi


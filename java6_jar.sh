#!/bin/bash
ant clean
export JAVA6_BOOTCLASSES=""
for i in /usr/lib/jvm/java-6-openjdk-amd64/jre/lib/*.jar; do
    export JAVA6_BOOTCLASSES=$JAVA6_BOOTCLASSES:$i
done
echo $JAVA6_BOOTCLASSES
ant jar

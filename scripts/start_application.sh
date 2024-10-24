#!/bin/bash
cd /opt/skoda-backend
nohup java -jar app.jar > /dev/null 2>&1 &

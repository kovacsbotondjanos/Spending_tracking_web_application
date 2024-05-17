#!/bin/bash

docker stop monthlyspendings_frontend monthlyspendings_backend
docker rm monthlyspendings_backend monthlyspendings_frontend
docker image rm monthlyspendings_frontend monthlyspendings_backend

cd monthlySpendingsBackend/
# docker build --build-arg JAR_FILE=target/*.jar -t monthlyspendings_backend .
docker build -t monthlyspendings_backend .
cd ..
cd spending_tracking_frontend/
docker build -t monthlyspendings_frontend .
cd ..
# docker run -d --network monthly_spending_network -p 8080:8080 -e "TIMEZONE=UTC" -e "PASSWORD=asd123" -e "USERNAME=root" -e "DATABASEURL=jdbc:mysql://172.18.0.2:3306/monthly_spendings" --name monthlyspendings_backend kovacsbotondjanos/monthlyspendings_backend
# docker run -d --network monthly_spending_network -p 8080:8080 -e "TIMEZONE=UTC" -e "PASSWORD=asd123" -e "USERNAME=root" -e "DATABASEURL=jdbc:mysql://mysql-server:3306/monthly_spendings" --name monthlyspendings_backend kovacsbotondjanos/monthlyspendings_backend
# docker run -d --network monthly_spending_network -it -p 5173:8080 --name monthlyspendings_frontend  kovacsbotondjanos/monthlyspendings_frontend
# docker run -d --network monthly_spending_network --name mysql-server -e MYSQL_ROOT_PASSWORD=asd123 -v monthly_expense:/var/lib/mysql mysql:latest
# mysql-server
# monthlyspendings_backend
# monthlyspendings_frontend
# [Unit]
# Description=My Script
# After=network.target

# [Service]
# ExecStart=~/monthly_spending_init_files/init.sh

# [Install]
# WantedBy=default.target
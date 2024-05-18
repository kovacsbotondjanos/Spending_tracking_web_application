#!/bin/bash

# stopping the container and removing the image
docker container stop monthlyspendings_backend
docker container rm monthlyspendings_backend
docker image rm kovacsbotondjanos/monthlyspendings_backend

# building the container then pushing the image to my docker profile, if needed
# mvn package
# docker build -t monthlyspendings_backend .
# docker tag monthlyspendings_backend kovacsbotondjanos/monthlyspendings_backend
# docker push kovacsbotondjanos/monthlyspendings_backend

# creating docker volume and docker network, if needed
# docker volume create monthly_expense
# docker network create monthly_spending_network

# starting the application (change passwords accordingly)
docker run -d --network monthly_spending_network --name mysql-server -e MYSQL_ROOT_PASSWORD=asd123 -v monthly_expense:/var/lib/mysql mysql:latest
docker run -d --network monthly_spending_network -p 8080:8080 -e "TIMEZONE=UTC" -e "PASSWORD=asd123" -e "USERNAME=root" -e "DATABASEURL=jdbc:mysql://mysql-server:3306/monthly_spendings" --name monthlyspendings_backend kovacsbotondjanos/monthlyspendings_backend
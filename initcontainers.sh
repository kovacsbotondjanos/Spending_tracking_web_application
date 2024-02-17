#!/bin/bash

docker stop monthlyspendings_frontend monthlyspendings_backend
docker rm monthlyspendings_backend monthlyspendings_frontend
docker image rm monthlyspendings_frontend monthlyspendings_backend

cd monthlySpendingsBackend/
docker build --build-arg JAR_FILE=target/*.jar -t monthlyspendings_backend .
cd ..
cd spending_tracking_frontend/
docker build -t monthlyspendings_frontend .
cd ..
docker run -d -p 8080:8080 --name monthlyspendings_backend monthlyspendings_backend
docker run -d -it -p 5173:8080 --name monthlyspendings_frontend  monthlyspendings_frontend

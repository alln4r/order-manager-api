#!/bin/bash

echo "A fazer Build da aplicação Java..."
mvn clean package -DskipTests

echo "A Iniciar containers com Docker Compose..."
docker-compose up --build -d

echo "Ambiente iniciado. Acede à API em: http://localhost:8080/api"

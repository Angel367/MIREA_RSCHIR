FROM mongo:latest
COPY configs/init-mongo.js /docker-entrypoint-initdb.d/


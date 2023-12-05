FROM golang:1.21

WORKDIR /usr/src/app

COPY go.mod go.sum ./
COPY gen ./gen
COPY .env ./
COPY main.go ./

CMD ["go","run","main.go"]

EXPOSE 4000
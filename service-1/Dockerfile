FROM golang:1.21

WORKDIR /usr/src/app

COPY go.mod go.sum ./
COPY cmd ./cmd
COPY gen ./gen
COPY internal ./internal
COPY .env ./

CMD ["go","run","cmd/main.go"]

EXPOSE 3000

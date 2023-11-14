# Dockerfile.filestorage
FROM golang:1.21 as builder

WORKDIR /app

COPY go.mod  ./
COPY go.sum  ./
RUN go mod download github.com/stretchr/objx
RUN go mod download


COPY ../../ .

RUN CGO_ENABLED=0 GOOS=linux go build -o filestorage main.go

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /app
COPY --from=builder /app/filestorage .

EXPOSE 8080
CMD ["./filestorage"]
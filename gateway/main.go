package main

import (
	"context"
	"flag"
	"github.com/grpc-ecosystem/grpc-gateway/v2/runtime"
	"github.com/joho/godotenv"
	"github/gateway/gen/go/auth"
	pb "github/gateway/gen/go/book"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"log"
	"net/http"
	"os"
)

var (
	// command-line options:
	// gRPC server endpoint
	grpcBookServerEndpoint = flag.String("grpc-book-endpoint", "microservice-1:3000", "gRPC server endpoint")

	grpcAuthServerEndpoint = flag.String("grpc-auth-endpoint", "microservice-2:5000", "gRPC server endpoint")
)

func run() error {
	ctx := context.Background()
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()

	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}
	// Register gRPC server endpoint
	// Note: Make sure the gRPC server is running properly and accessible
	mux := runtime.NewServeMux()
	opts := []grpc.DialOption{grpc.WithTransportCredentials(insecure.NewCredentials())}
	err = pb.RegisterBookMicroserviceHandlerFromEndpoint(ctx, mux, *grpcBookServerEndpoint, opts)
	if err != nil {
		log.Fatal(err)
	}

	err = auth.RegisterAuthServiceHandlerFromEndpoint(ctx, mux, *grpcAuthServerEndpoint, opts)
	if err != nil {
		log.Fatal(err)
	}

	log.Println("Server listening : localhost:" + os.Getenv("PORT"))
	// Start HTTP server (and proxy calls to gRPC server endpoint)
	return http.ListenAndServe(":"+os.Getenv("PORT"), mux)
}

func main() {
	err := run()
	if err != nil {
		return
	}
}

package main

import (
	"context"
	"log"
	"net/http"
	"os"

	"MIREA_RSCHIR/proto"
	"github.com/grpc-ecosystem/grpc-gateway/runtime"
	"google.golang.org/grpc"
)

func run() error {
	ctx := context.Background()
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()

	mux := runtime.NewServeMux()
	opts := []grpc.DialOption{grpc.WithInsecure()}

	err := proto.RegisterMyServiceHandlerFromEndpoint(ctx, mux, "localhost:50051", opts)
	if err != nil {
		return err
	}

	err = proto.RegisterMyServiceHandlerFromEndpoint(ctx, mux, "localhost:50052", opts)
	if err != nil {
		return err
	}

	return http.ListenAndServe(":8080", mux)
}

func main() {
	if err := run(); err != nil {
		log.Fatalf("Failed to run server: %v", err)
		os.Exit(1)
	}
}

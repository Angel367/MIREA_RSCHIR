package services

import (
	"context"
	"log"
	"net"

	"MIREA_RSCHIR/proto"
	"google.golang.org/grpc"
)

type bookServer struct{}

func (s *bookServer) GetBookInfo(ctx context.Context, in *proto.Book) (*proto.Book, error) {
	// Реализуйте логику получения информации о книге
	return &proto.Book{Isbn: "123456789", AuthorName: "Jane Doe", PagesAmount: 200}, nil
}

func main() {
	lis, err := net.Listen("tcp", ":50052")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}
	s := grpc.NewServer()
	proto.RegisterMyServiceServer(s, &bookServer{})
	if err := s.Serve(lis); err != nil {
		log.Fatalf("Failed to serve: %v", err)
	}
}

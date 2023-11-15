package services

import (
	"context"
	"log"
	"net"

	"MIREA_RSCHIR/proto"
	"google.golang.org/grpc"
)

type studentServer struct{}

func (s *studentServer) GetStudentInfo(ctx context.Context, in *proto.Student) (*proto.Student, error) {
	// Реализуйте логику получения информации о студенте
	return &proto.Student{Age: 20, AverageScore: 85.5, Name: "John Doe"}, nil
}

func main() {
	lis, err := net.Listen("tcp", ":50051")
	if err != nil {
		log.Fatalf("Failed to listen: %v", err)
	}
	s := grpc.NewServer()
	proto.RegisterMyServiceServer(s, &studentServer{})
	if err := s.Serve(lis); err != nil {
		log.Fatalf("Failed to serve: %v", err)
	}
}

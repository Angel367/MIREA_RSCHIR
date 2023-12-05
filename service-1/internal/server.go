package server

import (
	pb "github/service/1/gen/go"
	"github/service/1/internal/db"
	"github/service/1/internal/model"
	"golang.org/x/net/context"
)

type Server struct {
	pb.UnimplementedBookMicroserviceServer
}

func (s *Server) CreateBook(ctx context.Context, in *pb.BookForm) (*pb.Response, error) {

	db, err := db.Init()
	if err != nil {
		return nil, err
	}

	err = db.AutoMigrate(&model.Book{})
	if err != nil {
		return nil, err
	}

	book := &model.Book{
		Author:   in.GetAuthor(),
		SellerId: in.GetSellerId(),
		Price:    in.GetPrice(),
		Name:     in.GetName(),
	}

	db.Create(book)

	return &pb.Response{
		Book: &pb.BookInDb{
			Id:       int64(book.ID),
			Author:   book.Author,
			SellerId: book.SellerId,
			Price:    book.Price,
			Name:     book.Name,
		},
		Status: "OK",
	}, nil
}

func (s *Server) UpdateBook(ctx context.Context, in *pb.BookInDb) (*pb.Response, error) {
	db, err := db.Init()
	if err != nil {
		return nil, err
	}

	book := new(model.Book)

	result := db.First(&book, in.GetId())
	if result.Error != nil {
		return nil, result.Error
	}

	book.Author = in.GetAuthor()
	book.SellerId = in.GetSellerId()
	book.Price = in.GetPrice()
	book.Name = in.GetName()

	db.Save(&book)

	return &pb.Response{
		Book: &pb.BookInDb{
			Id:       int64(book.ID),
			Author:   book.Author,
			SellerId: book.SellerId,
			Price:    book.Price,
			Name:     book.Name,
		},
		Status: "OK",
	}, nil
}

func (s *Server) DeleteBook(ctx context.Context, in *pb.BookID) (*pb.Response, error) {
	db, err := db.Init()
	if err != nil {
		return nil, err
	}

	book := new(model.Book)

	result := db.First(&book, in.GetId())
	if result.Error != nil {
		return nil, result.Error
	}

	db.Delete(book)

	return &pb.Response{
		Book: &pb.BookInDb{
			Id:       int64(book.ID),
			Author:   book.Author,
			SellerId: book.SellerId,
			Price:    book.Price,
			Name:     book.Name,
		},
		Status: "OK",
	}, nil
}

func (s *Server) GetAllBooks(ctx context.Context, in *pb.EmptyForm) (*pb.ResponseArray, error) {
	db, err := db.Init()
	if err != nil {
		return nil, err
	}

	var books []model.Book

	result := db.Find(&books)

	if result.Error != nil {
		return nil, result.Error
	}

	booksInBb := []*pb.BookInDb{}

	for _, book := range books {
		booksInBb = append(booksInBb, &pb.BookInDb{
			Id:       int64(book.ID),
			Author:   book.Author,
			SellerId: book.SellerId,
			Price:    book.Price,
			Name:     book.Name,
		})
	}
	return &pb.ResponseArray{
		Books:  booksInBb,
		Status: "OK",
	}, nil
}

func (s *Server) GetOneBook(ctx context.Context, in *pb.BookID) (*pb.Response, error) {
	db, err := db.Init()
	if err != nil {
		return nil, err
	}

	book := new(model.Book)

	result := db.First(&book, in.GetId())
	if result.Error != nil {
		return nil, result.Error
	}

	return &pb.Response{
		Book: &pb.BookInDb{
			Id:       int64(book.ID),
			Author:   book.Author,
			SellerId: book.SellerId,
			Price:    book.Price,
			Name:     book.Name,
		},
		Status: "OK",
	}, nil
}

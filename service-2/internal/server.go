package internal

import (
	"context"
	"github.com/golang-jwt/jwt/v5"
	"github/service2/gen/go"
	"github/service2/internal/db"
	"github/service2/internal/model"
	"os"
	"time"
)

var jwtSecretKey = []byte(os.Getenv("SECRETE_KEY"))

type Server struct {
	auth.UnimplementedAuthServiceServer
}

func (s *Server) Login(_ context.Context, in *auth.LoginForm) (*auth.Response, error) {
	db, err := db.Init()
	if err != nil {
		return nil, err
	}

	err = db.AutoMigrate(&model.User{})
	if err != nil {
		return nil, err
	}

	user := &model.User{
		Login:    in.GetLogin(),
		Password: in.GetPassword(),
	}

	result := db.First(&user)
	if result.Error != nil {
		return nil, result.Error
	}

	payload := jwt.MapClaims{
		"sub": user.ID,
		"exp": time.Now().Add(time.Hour * 72).Unix(),
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, payload)

	t, err := token.SignedString(jwtSecretKey)
	if err != nil {
		return &auth.Response{}, err
	}
	return &auth.Response{
		Token: t,
	}, nil

	return &auth.Response{Token: "1"}, nil
}

func (s *Server) Registration(_ context.Context, in *auth.RegistrationForm) (*auth.Response, error) {
	db, err := db.Init()
	if err != nil {
		return nil, err
	}

	err = db.AutoMigrate(&model.User{})
	if err != nil {
		return nil, err
	}

	user := &model.User{
		Username: in.GetUsername(),
		Login:    in.GetLogin(),
		Password: in.GetPassword(),
	}

	db.Create(user)

	payload := jwt.MapClaims{
		"sub": user.ID,
		"exp": time.Now().Add(time.Hour * 72).Unix(),
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, payload)

	t, err := token.SignedString(jwtSecretKey)
	if err != nil {
		return &auth.Response{}, err
	}
	return &auth.Response{
		Token: t,
	}, nil
}

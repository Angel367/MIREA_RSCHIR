package internal

import (
	"context"
	"errors"
	"github.com/golang-jwt/jwt/v5"
	"github/service2/gen/go"
	"github/service2/internal/db"
	"github/service2/internal/model"
	"gorm.io/gorm"
	"log"
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
		log.Printf("Ошибка в бд")
		return nil, err
	}

	err = db.AutoMigrate(&model.User{})
	if err != nil {
		log.Printf("Ошибка в бд")
		return nil, err
	}

	if in.GetLogin() == "" || in.GetPassword() == "" {
		log.Printf("Некорректный запрос")
		return &auth.Response{
			Token: "Некорректный запрос",
		}, nil
	}

	user := &model.User{
		Login:    in.GetLogin(),
		Password: in.GetPassword(),
	}
	log.Printf("LOGIN " + in.GetLogin())
	result := db.First(&user)

	if result.Error != nil {
		// Обработка ошибки при выполнении запроса
		log.Printf("Ошибка при выполнении запроса к базе данных: %v", result.Error)
		return nil, result.Error
	}
	log.Printf("USERNAME " + user.Username)
	if errors.Is(result.Error, gorm.ErrRecordNotFound) {
		// Запись не найдена
		log.Printf("Такой пользователь не найден")
		return &auth.Response{
			Token: "Пользователь не найден",
		}, nil
	}

	var users []model.User

	for _, user := range users {
		log.Printf("ID: %d, Имя: %s, Логин: %s", user.ID, user.Username, user.Login)
	}

	// Здесь можно использовать данные пользователя из переменной `user`

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

func (s *Server) Registration(_ context.Context, in *auth.RegistrationForm) (*auth.Response, error) {
	db, err := db.Init()
	if err != nil {
		log.Printf("Ошибка в бд")
		return nil, err
	}

	err = db.AutoMigrate(&model.User{})
	if err != nil {
		log.Printf("Ошибка в бд")
		return nil, err
	}

	if in.GetLogin() == "" || in.GetUsername() == "" || in.GetPassword() == "" {
		log.Printf("Некорректный запрос")
		return &auth.Response{
			Token: "Некорректный запрос",
		}, nil
	}

	user := &model.User{
		Username: in.GetUsername(),
		Login:    in.GetLogin(),
		Password: in.GetPassword(),
	}

	result := db.Create(user)
	if result.Error != nil {
		log.Fatal("Ошибка при создании пользователя")
		return nil, result.Error
	}

	payload := jwt.MapClaims{
		"sub": user.ID,
		"exp": time.Now().Add(time.Hour * 72).Unix(),
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, payload)

	t, err := token.SignedString(jwtSecretKey)
	if err != nil {
		return nil, err
	}

	return &auth.Response{
		Token: t,
	}, nil
}

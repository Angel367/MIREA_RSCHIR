package api

import (
	"github.com/Angel367/MIREA_RSCHIR/cookie"
	"github.com/Angel367/MIREA_RSCHIR/logger"
	"net/http"
	"time"
)

func LinearHandler(w http.ResponseWriter, r *http.Request) {
	// Получаем данные из запроса
	// ...

	// Сохраняем данные в куку
	cookie.SetEncryptedCookie(w, "userdata", []byte("yourdata"), []byte("yourkey"))

	// Некоторая линейная обработка
	time.Sleep(1 * time.Second)

	// Отправляем ответ
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("Data processed linearly"))
}

func ConcurrentHandler(w http.ResponseWriter, r *http.Request) {
	// Получаем данные из куки
	data, err := cookie.GetEncryptedCookie(r, "userdata", []byte("yourkey"))
	if err != nil {
		// Обработка ошибки чтения куки
		return
	}

	// Некоторая обработка в горутинах
	for i := 0; i < 5; i++ {
		go func() {
			// Некоторая конкурентная обработка
			time.Sleep(500 * time.Millisecond)
			// Логирование
			logger.Logger.Info("Concurrent processing")
		}()
	}

	// Отправляем ответ
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("Data processed concurrently"))
}

// package main
//
// import (
//
//	"encoding/json"
//	"fmt"
//	"log"
//	"net/http"
//
// )
//
//	type student struct {
//		Age          int     `json:"age"`
//		AverageScore float32 `json:"averageScore"`
//		Name         string  `json:"name"`
//	}
//
// var studentData student
//
//	func getStudent(w http.ResponseWriter, r *http.Request) {
//		w.Header().Set("Content-Type", "application/json")
//		print(studentData.Name)
//		json.NewEncoder(w).Encode(studentData)
//	}
//
//	func createStudent(w http.ResponseWriter, r *http.Request) {
//		decoder := json.NewDecoder(r.Body)
//
//		err := decoder.Decode(&studentData)
//		if err != nil {
//			http.Error(w, err.Error(), http.StatusBadRequest)
//			return
//		}
//		w.WriteHeader(http.StatusCreated)
//		b, err := json.Marshal(studentData)
//		fmt.Fprint(w, string(b))
//	}
//
//	func updateStudent(w http.ResponseWriter, r *http.Request) {
//		decoder := json.NewDecoder(r.Body)
//		var newData student
//		err := decoder.Decode(&newData)
//		if err != nil {
//			http.Error(w, err.Error(), http.StatusBadRequest)
//			return
//		}
//		studentData = newData
//		w.WriteHeader(http.StatusNoContent)
//	}
//
//	func deleteStudent(w http.ResponseWriter, r *http.Request) {
//		studentData = student{}
//		w.WriteHeader(http.StatusOK)
//	}
//
//	func main() {
//		http.HandleFunc("/api/student", func(w http.ResponseWriter, r *http.Request) {
//			switch r.Method {
//			case http.MethodGet:
//				getStudent(w, r)
//			case http.MethodPost:
//				createStudent(w, r)
//			case http.MethodPut:
//				updateStudent(w, r)
//			case http.MethodDelete:
//				deleteStudent(w, r)
//			default:
//				http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
//			}
//		})
//
//		fmt.Println("Server is running on port 8080")
//		log.Fatal(http.ListenAndServe(":8080", nil))
//	}
package main

import (
	"fmt"
	"github.com/Angel367/MIREA_RSCHIR/api"
	"github.com/Angel367/MIREA_RSCHIR/config"
	"github.com/Angel367/MIREA_RSCHIR/logger"
	"net/http"
)

func main() {
	// Загрузка конфигурации
	cfg, err := config.LoadConfig()
	if err != nil {
		// Обработка ошибки загрузки конфигурации
		return
	}

	// Инициализация логгера
	err = logger.InitLogger()
	if err != nil {
		// Обработка ошибки инициализации логгера
		return
	}

	// Регистрация обработчиков
	http.HandleFunc("/api/linear", api.LinearHandler)
	http.HandleFunc("/api/concurrent", api.ConcurrentHandler)

	// Запуск сервера
	addr := fmt.Sprintf(":%d", cfg.Port)
	if err := http.ListenAndServe(addr, nil); err != nil {
		// Обработка ошибки запуска сервера
		return
	}
}

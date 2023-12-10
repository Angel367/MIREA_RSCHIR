package api

import (
	"MIREA_RSCHIR/cookie"
	"MIREA_RSCHIR/logger"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"
)

func ReadUserIP(r *http.Request) string {
	IPAddress := r.Header.Get("X-Real-Ip")
	if IPAddress == "" {
		IPAddress = r.Header.Get("X-Forwarded-For")
	}
	if IPAddress == "" {
		IPAddress = r.RemoteAddr
	}
	return IPAddress
}

type student struct {
	Age          int     `json:"age"`
	AverageScore float32 `json:"averageScore"`
	Name         string  `json:"name"`
}

var studentData student

func getStudentLinear(w http.ResponseWriter, r *http.Request) {
	logger.Logger.Info("Accepted GET request " + r.RequestURI + " from: " + ReadUserIP(r))
	decryptedData, _ := cookie.GetEncryptedCookie(r)
	if string(decryptedData) == "" {
		w.WriteHeader(http.StatusNotFound)
		_, err := w.Write([]byte("Cookie отсутствует или не валидна"))
		if err != nil {
			return
		}
	}
	w.Header().Set("Content-Type", "application/json")
	_, err := w.Write(decryptedData)
	if err != nil {
		return
	}

}

func createStudentLinear(w http.ResponseWriter, r *http.Request) {
	logger.Logger.Info("Accepted POST request " + r.RequestURI + " from: " + ReadUserIP(r))
	data, _ := cookie.GetEncryptedCookie(r)
	if string(data) != "" {
		w.WriteHeader(http.StatusConflict)
		return
	}

	decoder := json.NewDecoder(r.Body)
	err := decoder.Decode(&studentData)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	b, err := json.Marshal(studentData)

	cookie.SetEncryptedCookie(w, string(b))
	w.WriteHeader(http.StatusCreated)
}

func getStudentConcurrent(w http.ResponseWriter, r *http.Request) {
	logger.Logger.Info("Accepted GET request " + r.RequestURI + " from: " + ReadUserIP(r))
	done := make(chan bool)
	go func() {
		decryptedData, _ := cookie.GetEncryptedCookie(r)
		if string(decryptedData) == "" {
			w.WriteHeader(http.StatusNotFound)
			_, err := w.Write([]byte("Cookie отсутствует или не валидна"))
			if err != nil {
				return
			}
		}

		// Имитация работы в горутине с time.Sleep()
		time.Sleep(1 * time.Second)

		w.Header().Set("Content-Type", "application/json")
		_, err := w.Write(decryptedData)
		if err != nil {
			return
		}
		done <- true
	}()
	<-done
}

func createStudentConcurrent(w http.ResponseWriter, r *http.Request) {
	logger.Logger.Info("Accepted POST request " + r.RequestURI + " from: " + ReadUserIP(r))
	done := make(chan bool)
	go func() {
		data, _ := cookie.GetEncryptedCookie(r)
		if string(data) != "" {
			w.WriteHeader(http.StatusConflict)
			done <- true
			return
		}
		var studentData student
		decoder := json.NewDecoder(r.Body)
		err := decoder.Decode(&studentData)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}
		b, err := json.Marshal(studentData)
		time.Sleep(1 * time.Second)
		cookie.SetEncryptedCookie(w, string(b))
		w.WriteHeader(http.StatusCreated)
		done <- true
	}()
	<-done
}
func StartApi() {
	http.HandleFunc("/api/student/linear", func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodGet:
			getStudentLinear(w, r)
		case http.MethodPost:
			createStudentLinear(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})
	http.HandleFunc("/api/student/concurrent", func(w http.ResponseWriter, r *http.Request) {
		switch r.Method {
		case http.MethodGet:
			getStudentConcurrent(w, r)
		case http.MethodPost:
			createStudentConcurrent(w, r)
		default:
			http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		}
	})
	fmt.Println("Server is running on port 8080")
	log.Fatal(http.ListenAndServe(":8080", nil))
}

package api

import (
	"MIREA_RSCHIR/internal/filestorage"
	"MIREA_RSCHIR/logger"
	"context"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"

	"github.com/gorilla/mux"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

var client *mongo.Client

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

func connectDB() error {
	clientOptions := options.Client().ApplyURI("mongodb://mongodbcont:27017/")
	var err error
	client, err = mongo.Connect(context.TODO(), clientOptions)
	return err
}

func getFilesHandler(w http.ResponseWriter, r *http.Request) {
	logger.Logger.Info("Accepted GET request " + r.RequestURI + " from: " + ReadUserIP(r))
	fileList, err := filestorage.GetListOfFiles(client, "mydatabase")
	if err != nil {
		fmt.Println(err)
		http.Error(w, "Error getting list of files"+fmt.Sprint(err), http.StatusInternalServerError)
		return
	}
	if fileList == nil {
		http.Error(w, "[]", http.StatusNotFound)
		return
	}
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(fileList)
}

func getFileHandler(w http.ResponseWriter, r *http.Request) {
	logger.Logger.Info("Accepted GET request " + r.RequestURI + " from: " + ReadUserIP(r))
	vars := mux.Vars(r)
	fileID := vars["id"]

	fileContent, err := filestorage.GetFileByID(client, "mydatabase", fileID)
	if err != nil {
		http.Error(w, "No file with this ID", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/octet-stream")
	w.WriteHeader(http.StatusOK)
	w.Write(fileContent)
}

func getFileInfoHandler(w http.ResponseWriter, r *http.Request) {
	logger.Logger.Info("Accepted GET request " + r.RequestURI + " from: " + ReadUserIP(r))
	vars := mux.Vars(r)
	fileID := vars["id"]

	fileInfo, err := filestorage.GetFileInfoByID(client, "mydatabase", fileID)
	if err != nil {
		http.Error(w, "No file with this ID", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(fileInfo)
}

func uploadFileHandler(w http.ResponseWriter, r *http.Request) {
	logger.Logger.Info("Accepted POST request " + r.RequestURI + " from: " + ReadUserIP(r))
	file, handler, err := r.FormFile("file")
	if err != nil {
		http.Error(w, "Error reading form file", http.StatusBadRequest)
		return
	}
	defer file.Close()

	fileContent, err := ioutil.ReadAll(file)
	if err != nil {
		http.Error(w, "Error reading file content", http.StatusBadRequest)
		return
	}

	fileName := handler.Filename
	fileId, err := filestorage.UploadFile(client, "mydatabase", fileName, fileContent)
	if err != nil {
		http.Error(w, "Error uploading file", http.StatusInternalServerError)
		return
	}

	w.Write([]byte("File with ID: " + fileId.Hex() + "uploaded successfully!"))
}

func updateFileHandler(w http.ResponseWriter, r *http.Request) {
	logger.Logger.Info("Accepted PUT request " + r.RequestURI + " from: " + ReadUserIP(r))
	vars := mux.Vars(r)
	fileID := vars["id"]

	file, handler, err := r.FormFile("file")
	fileName := handler.Filename
	if err != nil {
		http.Error(w, "Error reading form file", http.StatusBadRequest)
		return
	}
	defer file.Close()

	fileContent, err := ioutil.ReadAll(file)
	if err != nil {
		http.Error(w, "Error reading file content", http.StatusInternalServerError)
		return
	}

	err = filestorage.UpdateFileByID(client, "mydatabase", fileID, fileName, string(fileContent))
	if err != nil {
		http.Error(w, "Error updating file by ID", http.StatusInternalServerError)
		return
	}

	w.Write([]byte("File updated successfully!"))
	w.WriteHeader(http.StatusOK)
}

func deleteFileHandler(w http.ResponseWriter, r *http.Request) {
	logger.Logger.Info("Accepted DELETE request " + r.RequestURI + " from: " + ReadUserIP(r))
	vars := mux.Vars(r)
	fileID := vars["id"]

	err := filestorage.DeleteFileByID(client, "mydatabase", fileID)
	if err != nil {
		http.Error(w, "No file with this ID", http.StatusNotFound)
		return
	}

	w.Write([]byte("File deleted successfully!"))
	w.WriteHeader(http.StatusOK)
}

func Main() {
	err := connectDB()
	if err != nil {
		logger.Logger.Error("Error connecting to MongoDB: " + fmt.Sprint(err))
		return
	}
	defer client.Disconnect(context.TODO())

	r := mux.NewRouter()

	r.HandleFunc("/files", getFilesHandler).Methods("GET")
	r.HandleFunc("/files/{id}", getFileHandler).Methods("GET")
	r.HandleFunc("/files/{id}/info", getFileInfoHandler).Methods("GET")
	r.HandleFunc("/files", uploadFileHandler).Methods("POST")
	r.HandleFunc("/files/{id}", updateFileHandler).Methods("PUT")
	r.HandleFunc("/files/{id}", deleteFileHandler).Methods("DELETE")

	http.Handle("/", r)

	fmt.Println("Server is running on :8080")
	http.ListenAndServe(":8080", nil)
}

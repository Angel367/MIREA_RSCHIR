package filestorage

import (
	"MIREA_RSCHIR/logger"
	"context"
	"fmt"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/gridfs"
	"go.mongodb.org/mongo-driver/mongo/options"
	"io/ioutil"
)

func connectDB() (*mongo.Client, error) {
	clientOptions := options.Client().ApplyURI("mongodb://localhost:27017")
	client, err := mongo.Connect(context.TODO(), clientOptions)
	if err != nil {
		logger.Logger.Error("Error connecting to MongoDB")
		return nil, err
	}

	err = client.Ping(context.TODO(), nil)
	if err != nil {
		logger.Logger.Error("Error connecting to MongoDB")
		return nil, err
	}
	logger.Logger.Info("Connected to database successfully")
	return client, nil
}

type FileInfo struct {
	Name string
	ID   primitive.ObjectID
}

// GetListOfFiles returns a list of filenames and their corresponding file IDs.
func GetListOfFiles(client *mongo.Client, dbName string) ([]FileInfo, error) {
	db := client.Database(dbName)
	bucket, err := gridfs.NewBucket(db)
	if err != nil {
		logger.Logger.Error("Ошибка здесь")
		return nil, err
	}

	cursor, err := bucket.Find(context.TODO(), nil)
	if err != nil {
		logger.Logger.Error("Нет здесь")
		return nil, err
	}
	defer cursor.Close(context.TODO())

	var fileList []FileInfo
	for cursor.Next(context.TODO()) {
		var file gridfs.File
		if err := cursor.Decode(&file); err != nil {
			logger.Logger.Error("Всё-токи здесь")
			return nil, err
		}
		fileID, ok := file.ID.(primitive.ObjectID)
		if !ok {
			logger.Logger.Error("ЗДЕСЬ!!!!!!!!!!")
			return nil, err // handle the error appropriately
		}
		fileList = append(fileList, FileInfo{ID: fileID, Name: file.Name})
	}

	return fileList, nil
}

func GetFileByID(client *mongo.Client, dbName, fileID string) ([]byte, error) {
	db := client.Database(dbName)
	bucket, err := gridfs.NewBucket(db)
	if err != nil {
		return nil, err
	}

	fileIDHex, err := primitive.ObjectIDFromHex(fileID)
	if err != nil {
		return nil, err
	}

	downloadStream, err := bucket.OpenDownloadStream(fileIDHex)
	if err != nil {
		return nil, err
	}
	defer downloadStream.Close()

	fileContent, err := ioutil.ReadAll(downloadStream)
	if err != nil {
		return nil, err
	}

	return fileContent, nil
}

func GetFileInfoByID(client *mongo.Client, dbName, fileID string) (*gridfs.File, error) {
	db := client.Database(dbName)
	bucket, err := gridfs.NewBucket(db)
	if err != nil {
		return nil, err
	}

	fileIDHex, err := primitive.ObjectIDFromHex(fileID)
	if err != nil {
		return nil, err
	}

	downloadStream, err := bucket.OpenDownloadStream(fileIDHex)
	if err != nil {
		return nil, err
	}
	defer downloadStream.Close()

	fileInfo := downloadStream.GetFile()
	return fileInfo, nil
}

func UploadFile(client *mongo.Client, dbName, fileName string, fileContent []byte) (primitive.ObjectID, error) {
	db := client.Database(dbName)
	bucket, err := gridfs.NewBucket(db)
	if err != nil {
		return primitive.NilObjectID, err
	}

	uploadStream, err := bucket.OpenUploadStream(fileName)
	if err != nil {
		return primitive.NilObjectID, err
	}
	defer uploadStream.Close()

	_, err = uploadStream.Write(fileContent)
	if err != nil {
		return primitive.NilObjectID, err
	}

	return uploadStream.FileID.(primitive.ObjectID), nil
}

// UpdateFileByID updates the content of an existing file in MongoDB GridFS.
func UpdateFileByID(client *mongo.Client, dbName, fileID, fileName, newContent string) error {
	db := client.Database(dbName)
	bucket, err := gridfs.NewBucket(db)
	if err != nil {
		return err
	}

	// Convert fileID string to primitive.ObjectID
	objID, err := primitive.ObjectIDFromHex(fileID)
	if err != nil {
		return err
	}
	// Delete the existing file
	if err := bucket.Delete(objID); err != nil {
		return err
	}

	// Open upload stream with specified file ID
	uploadStream, err := bucket.OpenUploadStreamWithID(objID, fileName)
	if err != nil {
		return err
	}
	defer uploadStream.Close()

	// Write the updated content to the upload stream
	_, err = uploadStream.Write([]byte(newContent))
	if err != nil {
		return err
	}

	return nil
}

func DeleteFileByID(client *mongo.Client, dbName, fileID string) error {
	db := client.Database(dbName)
	bucket, err := gridfs.NewBucket(db)
	if err != nil {
		return err
	}

	fileIDHex, err := primitive.ObjectIDFromHex(fileID)
	if err != nil {
		return err
	}

	err = bucket.Delete(fileIDHex)
	if err != nil {
		return err
	}

	return nil
}

func main() {
	client, err := connectDB()
	if err != nil {
		fmt.Println("Error connecting to MongoDB:", err)
		return
	}
	defer client.Disconnect(context.TODO())

}

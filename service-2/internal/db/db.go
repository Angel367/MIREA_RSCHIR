package db

import (
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

func Init() (*gorm.DB, error) {
	dsn := "host=postgres user=postgres password=admin dbname=postgres port=5432 sslmode=disable"

	return gorm.Open(postgres.Open(dsn), &gorm.Config{})
}

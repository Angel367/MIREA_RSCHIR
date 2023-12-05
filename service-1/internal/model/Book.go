package model

import "gorm.io/gorm"

type Book struct {
	gorm.Model
	Author   string
	SellerId int32
	Price    int32
	Name     string
}

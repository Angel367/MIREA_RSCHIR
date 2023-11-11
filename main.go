package main

import (
	"MIREA_RSCHIR/api"
	"MIREA_RSCHIR/logger"
)

func main() {
	err := logger.InitLogger()
	if err != nil {
		return
	}
	api.StartApi()
}

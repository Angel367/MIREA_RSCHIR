package logger

import (
	"go.uber.org/zap"
	"go.uber.org/zap/zapcore"
)

var (
	Logger *zap.Logger
)

func InitLogger() error {
	cfg := zap.NewProductionConfig()
	cfg.EncoderConfig.EncodeLevel = zapcore.CapitalLevelEncoder

	var err error
	Logger, err = cfg.Build()
	if err != nil {
		return err
	}

	defer func(Logger *zap.Logger) {
		err := Logger.Sync()
		if err != nil {

		}
	}(Logger)

	return nil
}

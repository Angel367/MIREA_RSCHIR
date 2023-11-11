package config

import "github.com/spf13/viper"

type AppConfig struct {
	Port      int
	CookieKey string
	// другие параметры конфигурации
}

func LoadConfig() (*AppConfig, error) {
	viper.SetConfigFile(".env")
	viper.SetDefault("PORT", 8080)
	viper.SetDefault("COOKIE_KEY", "supersecret")

	if err := viper.ReadInConfig(); err != nil {
		return nil, err
	}

	config := &AppConfig{
		Port:      viper.GetInt("PORT"),
		CookieKey: viper.GetString("COOKIE_KEY"),
		// другие параметры конфигурации
	}

	return config, nil
}

package cookie

import (
	"MIREA_RSCHIR/encryption"
	"net/http"
)

func SetEncryptedCookie(w http.ResponseWriter, name string, value []byte, key []byte) {
	// Шифруем данные
	encryptedValue, err := encryption.Encrypt(value, key)
	if err != nil {
		// Обработка ошибки
		return
	}

	// Устанавливаем зашифрованную куку
	http.SetCookie(w, &http.Cookie{
		Name:  name,
		Value: string(encryptedValue),
	})
}

func GetEncryptedCookie(r *http.Request, name string, key []byte) ([]byte, error) {
	// Получаем куку из запроса
	cookie, err := r.Cookie(name)
	if err != nil {
		return nil, err
	}

	// Дешифруем данные куки
	decryptedValue, err := encryption.Decrypt([]byte(cookie.Value), key)
	if err != nil {
		return nil, err
	}

	return decryptedValue, nil
}

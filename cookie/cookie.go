package cookie

import (
	"MIREA_RSCHIR/encryption"
	"MIREA_RSCHIR/logger"
	"encoding/hex"
	"net/http"
)

func SetEncryptedCookie(w http.ResponseWriter, value string) {
	cookieName := "student_data"
	logger.Logger.Info("Content to encrypt for cookie: " + value)
	encryptString, err := encryption.EncryptString(value)
	if err != nil {
		return
	}
	cookieContent := hex.EncodeToString(encryptString)
	logger.Logger.Info("Sent encrypted cookie: " + cookieContent)
	http.SetCookie(w, &http.Cookie{
		Name:  cookieName,
		Value: cookieContent,
	})
}

func GetEncryptedCookie(r *http.Request) ([]byte, error) {
	cookieName := "student_data"
	cookie, err := r.Cookie(cookieName)
	if err != nil {
		return nil, err
	}

	decodedCookieValue, _ := hex.DecodeString(cookie.Value)
	decryptedValue, err := encryption.DecryptString(string(decodedCookieValue))
	logger.Logger.Info("Got encrypted cookie: " + cookie.Value)
	logger.Logger.Info("Decrypted data from cookie: " + string(decryptedValue))
	if err != nil {
		return nil, err
	}

	return decryptedValue, nil
}

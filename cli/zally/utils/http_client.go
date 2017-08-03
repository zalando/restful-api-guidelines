package utils

import (
	"net/http"
	"time"
)

const httpTimeout = 5 * time.Second

// DoHTTPRequest makes an HTTP request with timeout
func DoHTTPRequest(request *http.Request) (*http.Response, error) {
	timeout := time.Duration(httpTimeout)
	client := &http.Client{
		Timeout: timeout,
	}
	return client.Do(request)
}

package commands

import (
	"fmt"
	"io"
	"net/http"
)

func buildRequest(httpVerb string, path string, token string, body io.Reader) (request *http.Request) {
	req, err := http.NewRequest(httpVerb, path, body)
	if err != nil {
		panic(err)
	}

	if len(token) > 0 {
		req.Header.Add("Authorization", fmt.Sprintf("Bearer %s", token))
	}
	return req
}

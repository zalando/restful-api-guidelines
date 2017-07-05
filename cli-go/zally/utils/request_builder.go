package utils

import (
	"fmt"
	"io"
	"net/http"
	"net/url"
)

// RequestBuilder builds Zally specific requests
type RequestBuilder struct {
	baseURL string
	token   string
}

// NewRequestBuilder creates an instance of RequestBuilder
func NewRequestBuilder(baseURL string, token string) *RequestBuilder {
	var builder RequestBuilder
	builder.baseURL = baseURL
	builder.token = token
	return &builder
}

// Build creates HTTP request
func (r *RequestBuilder) Build(httpVerb string, uri string, body io.Reader) (*http.Request, error) {
	url, err := r.getAbsoluteURL(uri)
	if err != nil {
		return nil, err
	}

	request, err := http.NewRequest(httpVerb, url, body)
	if err != nil {
		return nil, err
	}

	request.Header.Add("Content-Type", "application/json")

	if len(r.token) > 0 {
		request.Header.Add("Authorization", fmt.Sprintf("Bearer %s", r.token))
	}

	return request, nil
}

func (r *RequestBuilder) getAbsoluteURL(uri string) (string, error) {
	base, err := url.Parse(r.baseURL)
	if err != nil {
		return "", err
	}
	u, err := url.Parse(uri)
	if err != nil {
		return "", err
	}

	return base.ResolveReference(u).String(), nil
}

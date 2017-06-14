package utils

import (
	"bytes"
	"testing"

	"github.com/zalando-incubator/zally/cli-go/zally/tests"
)

func TestNewRequestBuilder(t *testing.T) {
	t.Run("accepts_base_url_and_token", func(t *testing.T) {
		builder := NewRequestBuilder("http://example.com", "some_token")

		tests.AssertEquals(t, "http://example.com", builder.baseURL)
		tests.AssertEquals(t, "some_token", builder.token)
	})
}

func TestRequestBuilderBuild(t *testing.T) {
	t.Run("creates_absolute_url", func(t *testing.T) {
		builder := NewRequestBuilder("http://example.com/", "some_token")
		request, err := builder.Build("GET", "/my-path", nil)

		tests.AssertEquals(t, "http://example.com/my-path", request.URL.String())
		tests.AssertEquals(t, nil, err)
	})

	t.Run("adds_auth_header_when_token_is_specified", func(t *testing.T) {
		builder := NewRequestBuilder("http://example.com/", "some_token")
		request, err := builder.Build("GET", "/my-path", nil)

		tests.AssertEquals(t, "Bearer some_token", request.Header.Get("Authorization"))
		tests.AssertEquals(t, nil, err)
	})

	t.Run("adds_no_auth_header_when_token_is_not_specified", func(t *testing.T) {
		builder := NewRequestBuilder("http://example.com/", "")
		request, err := builder.Build("GET", "/my-path", nil)

		tests.AssertEquals(t, "", request.Header.Get("Authorization"))
		tests.AssertEquals(t, nil, err)
	})

	t.Run("sets_proper_http_method", func(t *testing.T) {
		builder := NewRequestBuilder("http://example.com/", "some_token")
		request, err := builder.Build("HEAD", "/my-path", nil)

		tests.AssertEquals(t, "HEAD", request.Method)
		tests.AssertEquals(t, nil, err)
	})

	t.Run("sets_proper_body", func(t *testing.T) {
		body := []byte("Test body")
		builder := NewRequestBuilder("http://example.com/", "some_token")
		request, err := builder.Build("POST", "/my-path", bytes.NewBuffer(body))

		reader, err := request.GetBody()
		buffer := new(bytes.Buffer)
		buffer.ReadFrom(reader)

		tests.AssertEquals(t, "Test body", buffer.String())
		tests.AssertEquals(t, nil, err)
	})
}

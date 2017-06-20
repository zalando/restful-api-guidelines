package commands

import (
	"io/ioutil"
	"testing"

	"encoding/json"

	"net/http"
	"net/http/httptest"

	"io"

	"fmt"

	"github.com/zalando-incubator/zally/cli-go/zally/domain"
	"github.com/zalando-incubator/zally/cli-go/zally/utils"
)

func TestReadFile(t *testing.T) {
	t.Run("fails_if_file_is_not_found", func(t *testing.T) {
		data, err := readFile("/tmp/non_existing_file")
		utils.AssertEquals(t, json.RawMessage(nil), data)
		utils.AssertEquals(t, "open /tmp/non_existing_file: no such file or directory", err.Error())
	})

	t.Run("returns_contents_when_file_is_found", func(t *testing.T) {
		data, err := readFile("testdata/minimal_swagger.json")
		utils.AssertEquals(t, json.RawMessage("{\n  \"swagger\": \"2.0\"\n}"), data)
		utils.AssertEquals(t, nil, err)
	})
}

func TestDoRequest(t *testing.T) {
	t.Run("returns_violations_when_success", func(t *testing.T) {
		handler := func(w http.ResponseWriter, r *http.Request) {
			fixture, _ := ioutil.ReadFile("testdata/violations_response.json")
			w.Header().Set("Content-Type", "application/json")
			io.WriteString(w, string(fixture))
		}
		testServer := httptest.NewServer(http.HandlerFunc(handler))
		defer testServer.Close()

		requestBuilder := utils.NewRequestBuilder(testServer.URL, "")
		data, _ := readFile("testdata/minimal_swagger.json")

		violations, err := doRequest(requestBuilder, data)

		utils.AssertEquals(t, nil, err)
		utils.AssertEquals(t, "First Violation", violations.Violations[0].Title)
		utils.AssertEquals(t, "Second Violation", violations.Violations[1].Title)
	})

	t.Run("returns_error_if_http_error_occured", func(t *testing.T) {
		handler := func(w http.ResponseWriter, r *http.Request) {
			http.Error(w, "Not Found", 404)
		}
		testServer := httptest.NewServer(http.HandlerFunc(handler))
		defer testServer.Close()

		requestBuilder := utils.NewRequestBuilder(testServer.URL, "")
		data, _ := readFile("testdata/minimal_swagger.json")

		violations, err := doRequest(requestBuilder, data)

		utils.AssertEquals(t, "Cannot submit file for linting. HTTP Status: 404, Response: Not Found\n", err.Error())
		utils.AssertEquals(t, (*domain.Violations)(nil), violations)
	})
}

func TestGetReader(t *testing.T) {
	yamlFixture := []byte("swagger: \"2.0\"")
	jsonFixture := []byte("{\"swagger\": \"2.0\"}")

	t.Run("returns_yaml_reader_when_extension_is_yaml", func(t *testing.T) {
		absolutePath := "/tmp/file.yaml"
		reader := getReader(absolutePath, yamlFixture)

		utils.AssertEquals(t, "*readers.YAMLReader", fmt.Sprintf("%T", reader))
	})

	t.Run("returns_yaml_reader_when_extension_is_yml", func(t *testing.T) {
		absolutePath := "/tmp/file.yml"
		reader := getReader(absolutePath, yamlFixture)

		utils.AssertEquals(t, "*readers.YAMLReader", fmt.Sprintf("%T", reader))
	})

	t.Run("returns_json_reader_when_extension_is_other", func(t *testing.T) {
		absolutePath := "/tmp/file.json"
		reader := getReader(absolutePath, jsonFixture)

		utils.AssertEquals(t, "*readers.JSONReader", fmt.Sprintf("%T", reader))
	})
}

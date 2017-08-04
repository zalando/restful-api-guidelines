package commands

import (
	"io/ioutil"
	"testing"
	"time"

	"encoding/json"

	"net/http"
	"net/http/httptest"

	"io"

	"fmt"

	"github.com/urfave/cli"
	"github.com/zalando-incubator/zally/cli/zally/domain"
	"github.com/zalando-incubator/zally/cli/zally/tests"
	"github.com/zalando-incubator/zally/cli/zally/utils"
)

var app = cli.NewApp()

func TestReadFile(t *testing.T) {
	t.Run("fails_if_local_file_is_not_found", func(t *testing.T) {
		data, err := readFile("/tmp/non_existing_file")
		tests.AssertEquals(t, json.RawMessage(nil), data)
		tests.AssertEquals(t, "open /tmp/non_existing_file: no such file or directory", err.Error())
	})

	t.Run("returns_contents_when_local_file_is_found", func(t *testing.T) {
		data, err := readFile("testdata/minimal_swagger.json")
		tests.AssertEquals(t, json.RawMessage("{\n  \"swagger\": \"2.0\"\n}"), data)
		tests.AssertEquals(t, nil, err)
	})

	t.Run("returns_contents_when_file_is_remote", func(t *testing.T) {
		handler := func(w http.ResponseWriter, r *http.Request) {
			fixture, _ := ioutil.ReadFile("testdata/minimal_swagger.json")
			w.Header().Set("Content-Type", "application/json")
			io.WriteString(w, string(fixture))
		}
		testServer := httptest.NewServer(http.HandlerFunc(handler))
		defer testServer.Close()

		data, err := readFile(testServer.URL)
		fmt.Print(testServer.URL)
		tests.AssertEquals(t, json.RawMessage("{\n  \"swagger\": \"2.0\"\n}"), data)
		tests.AssertEquals(t, nil, err)
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

		requestBuilder := utils.NewRequestBuilder(testServer.URL, "", app)
		data, _ := readFile("testdata/minimal_swagger.json")

		violations, err := doRequest(requestBuilder, data)

		tests.AssertEquals(t, nil, err)
		tests.AssertEquals(t, "First Violation", violations.Violations[0].Title)
		tests.AssertEquals(t, "Second Violation", violations.Violations[1].Title)
	})

	t.Run("returns_error_if_http_error_occured", func(t *testing.T) {
		handler := func(w http.ResponseWriter, r *http.Request) {
			http.Error(w, "Not Found", 404)
		}
		testServer := httptest.NewServer(http.HandlerFunc(handler))
		defer testServer.Close()

		requestBuilder := utils.NewRequestBuilder(testServer.URL, "", app)
		data, _ := readFile("testdata/minimal_swagger.json")

		violations, err := doRequest(requestBuilder, data)

		tests.AssertEquals(t, "Cannot submit file for linting. HTTP Status: 404, Response: Not Found\n", err.Error())
		tests.AssertEquals(t, (*domain.Violations)(nil), violations)
	})

	t.Run("fails when timeout is reached", func(t *testing.T) {
		handler := func(w http.ResponseWriter, r *http.Request) {
			time.Sleep(6 * time.Second)
			w.Header().Set("Content-Type", "application/json")
			io.WriteString(w, "Hello")
		}
		testServer := httptest.NewServer(http.HandlerFunc(handler))
		defer testServer.Close()

		requestBuilder := utils.NewRequestBuilder(testServer.URL, "", app)
		data, _ := readFile("testdata/minimal_swagger.json")

		violations, err := doRequest(requestBuilder, data)

		expectedError := fmt.Sprintf(
			"Post %s/api-violations: net/http: request canceled"+
				" (Client.Timeout exceeded while awaiting headers)",
			testServer.URL,
		)
		tests.AssertEquals(t, expectedError, err.Error())
		tests.AssertEquals(t, (*domain.Violations)(nil), violations)
	})
}

func TestGetReader(t *testing.T) {
	yamlFixture := []byte("swagger: \"2.0\"")
	jsonFixture := []byte("{\"swagger\": \"2.0\"}")

	t.Run("returns_yaml_reader_when_extension_is_yaml", func(t *testing.T) {
		path := "/tmp/file.yaml"
		reader := getReader(path, yamlFixture)

		tests.AssertEquals(t, "*readers.YAMLReader", fmt.Sprintf("%T", reader))
	})

	t.Run("returns_yaml_reader_when_extension_is_yml", func(t *testing.T) {
		path := "/tmp/file.yml"
		reader := getReader(path, yamlFixture)

		tests.AssertEquals(t, "*readers.YAMLReader", fmt.Sprintf("%T", reader))
	})

	t.Run("returns_json_reader_when_extension_is_other", func(t *testing.T) {
		path := "/tmp/file.json"
		reader := getReader(path, jsonFixture)

		tests.AssertEquals(t, "*readers.JSONReader", fmt.Sprintf("%T", reader))
	})
}

func TestLintFile(t *testing.T) {
	t.Run("returns_no_error_when_no_must_violations", func(t *testing.T) {
		handler := func(w http.ResponseWriter, r *http.Request) {
			fixture, _ := ioutil.ReadFile("testdata/violations_response_without_must_violations.json")
			w.Header().Set("Content-Type", "application/json")
			io.WriteString(w, string(fixture))
		}
		testServer := httptest.NewServer(http.HandlerFunc(handler))
		defer testServer.Close()

		requestBuilder := utils.NewRequestBuilder(testServer.URL, "", app)

		err := lintFile("testdata/minimal_swagger.json", requestBuilder)

		tests.AssertEquals(t, nil, err)
	})

	t.Run("returns_with_an_error_when_any_must_violations", func(t *testing.T) {
		handler := func(w http.ResponseWriter, r *http.Request) {
			fixture, _ := ioutil.ReadFile("testdata/violations_response.json")
			w.Header().Set("Content-Type", "application/json")
			io.WriteString(w, string(fixture))
		}
		testServer := httptest.NewServer(http.HandlerFunc(handler))
		defer testServer.Close()

		requestBuilder := utils.NewRequestBuilder(testServer.URL, "", app)

		err := lintFile("testdata/minimal_swagger.json", requestBuilder)

		tests.AssertEquals(t, "Failing because: 1 must violation(s) found", err.Error())
	})
}

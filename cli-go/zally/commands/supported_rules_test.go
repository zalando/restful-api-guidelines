package commands

import (
	"io"
	"io/ioutil"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/zalando-incubator/zally/cli-go/zally/domain"
	"github.com/zalando-incubator/zally/cli-go/zally/tests"
	"github.com/zalando-incubator/zally/cli-go/zally/utils"
)

func TestFetchRules(t *testing.T) {
	t.Run("returns_rules_list_when_success", func(t *testing.T) {
		handler := func(w http.ResponseWriter, r *http.Request) {
			fixture, _ := ioutil.ReadFile("testdata/rules_response.json")
			w.Header().Set("Content-Type", "application/json")
			io.WriteString(w, string(fixture))
		}
		testServer := httptest.NewServer(http.HandlerFunc(handler))
		defer testServer.Close()

		requestBuilder := utils.NewRequestBuilder(testServer.URL, "")
		rules, err := fetchRules(requestBuilder, "")

		tests.AssertEquals(t, nil, err)
		tests.AssertEquals(t, len(rules.Rules), 15)
		tests.AssertEquals(t, "M001", rules.Rules[0].Code)
		tests.AssertEquals(t, "Avoid Link in Header Rule", rules.Rules[0].Title)
		tests.AssertEquals(t, "http://zalando.github.io/restful-api-guidelines/hyper-media/Hypermedia.html#must-do-not-use-link-headers-with-json-entities", rules.Rules[0].URL)
		tests.AssertEquals(t, true, rules.Rules[0].IsActive)
		tests.AssertEquals(t, "MUST", rules.Rules[0].Type)
	})

	t.Run("returns_error_when_status_is_not_200", func(t *testing.T) {
		handler := func(w http.ResponseWriter, r *http.Request) {
			w.WriteHeader(http.StatusBadRequest)
			io.WriteString(w, "Something went wrong")
		}
		testServer := httptest.NewServer(http.HandlerFunc(handler))
		defer testServer.Close()

		requestBuilder := utils.NewRequestBuilder(testServer.URL, "")
		rules, err := fetchRules(requestBuilder, "")

		tests.AssertEquals(t, "Cannot submit file for linting. HTTP Status: 400, Response: Something went wrong", err.Error())
		tests.AssertEquals(t, (*domain.Rules)(nil), rules)
	})

	t.Run("supports_type_filter", func(t *testing.T) {
		handler := func(w http.ResponseWriter, r *http.Request) {
			fixture, _ := ioutil.ReadFile("testdata/rules_response.json")
			w.Header().Set("Content-Type", "application/json")
			io.WriteString(w, string(fixture))
			tests.AssertEquals(t, r.URL.RawQuery, "type=must")
		}
		testServer := httptest.NewServer(http.HandlerFunc(handler))
		defer testServer.Close()

		requestBuilder := utils.NewRequestBuilder(testServer.URL, "")
		fetchRules(requestBuilder, "must")
	})
}

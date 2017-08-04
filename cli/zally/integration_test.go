package main

import (
	"fmt"
	"io/ioutil"
	"os"
	"testing"

	"github.com/stretchr/testify/assert"
)

type callable func() error

// https://stackoverflow.com/questions/10473800/in-go-how-do-i-capture-stdout-of-a-function-into-a-string
func CaptureOutput(callable callable) (string, error) {
	rescueStdout := os.Stdout
	r, w, _ := os.Pipe()
	os.Stdout = w

	err := callable()

	w.Close()
	outRaw, _ := ioutil.ReadAll(r)
	os.Stdout = rescueStdout

	out := string(outRaw[:])
	return out, err
}

func RunAppAndCaptureOutput(params []string) (string, error) {
	return CaptureOutput(func() error {
		return CreateApp().Run(params)
	})
}

func TestIntegrationWithNoParametersShowsUsage(t *testing.T) {
	t.Run("integrationWithNoParametersShowsUsage", func(t *testing.T) {
		out, e := RunAppAndCaptureOutput([]string{""})

		assert.Contains(t, out, "USAGE:")
		assert.Contains(t, out, "zally.test [global options] command [command options] [arguments...]")

		assert.Nil(t, e)
	})
}

func TestIntegrationWithLocalYamlFile(t *testing.T) {
	t.Run("integrationWithLocalYamlFile", func(t *testing.T) {
		out, e := RunAppAndCaptureOutput([]string{"", "lint", "../../server/src/test/resources/fixtures/api_spa.yaml"})

		assert.Contains(t, out, "MUST violations: 6")
		assert.Contains(t, out, "SHOULD violations: 2")
		assert.Contains(t, out, "MAY violations: 0")
		assert.Contains(t, out, "HINT violations: 1")

		assert.NotNil(t, e)
		assert.Equal(t, e.Error(), "Failing because: 6 must violation(s) found")
	})
}

func TestIntegrationWithSomeOtherLocalYamlFile(t *testing.T) {
	t.Run("integrationWithSomeOtherLocalYamlFile", func(t *testing.T) {
		out, e := RunAppAndCaptureOutput([]string{"", "lint", "../../server/src/test/resources/fixtures/api_tinbox.yaml"})

		assert.Contains(t, out, "OpenAPI 2.0 schema")
		assert.Contains(t, out, "MUST violations: 8")
		assert.Contains(t, out, "SHOULD violations: 3")
		assert.Contains(t, out, "MAY violations: 0")
		assert.Contains(t, out, "HINT violations: 1")

		assert.NotNil(t, e)
		assert.Equal(t, e.Error(), "Failing because: 8 must violation(s) found")
	})
}

func TestIntegrationWithLocalJsonFile(t *testing.T) {
	t.Run("integrationWithLocalJsonFile", func(t *testing.T) {
		out, e := RunAppAndCaptureOutput([]string{"", "lint", "../../server/src/test/resources/fixtures/api_spp.json"})

		fmt.Println(out)
		assert.Contains(t, out, "MUST violations: 2")
		assert.Contains(t, out, "SHOULD violations: 2")
		assert.Contains(t, out, "MAY violations: 1")
		assert.Contains(t, out, "HINT violations: 1")

		assert.NotNil(t, e)
		assert.Equal(t, e.Error(), "Failing because: 2 must violation(s) found")
	})
}

func TestIntegrationWithRemoteYamlFile(t *testing.T) {
	t.Run("integrationWithRemoteYamlFile", func(t *testing.T) {
		out, e := RunAppAndCaptureOutput([]string{"", "lint", "https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spa.yaml"})

		assert.Contains(t, out, "MUST violations: 6")
		assert.Contains(t, out, "SHOULD violations: 2")
		assert.Contains(t, out, "MAY violations: 0")
		assert.Contains(t, out, "HINT violations: 1")

		assert.NotNil(t, e)
		assert.Equal(t, e.Error(), "Failing because: 6 must violation(s) found")
	})
}

func TestIntegrationWithRemoteJsonFile(t *testing.T) {
	t.Run("integrationWithRemoteJsonFile", func(t *testing.T) {
		out, e := RunAppAndCaptureOutput([]string{"", "lint", "https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json"})

		assert.Contains(t, out, "MUST violations: 2")
		assert.Contains(t, out, "SHOULD violations: 2")
		assert.Contains(t, out, "MAY violations: 1")
		assert.Contains(t, out, "HINT violations: 1")

		assert.NotNil(t, e)
		assert.Equal(t, e.Error(), "Failing because: 2 must violation(s) found")
	})
}

func TestIntegrationWithNoMustViolations(t *testing.T) {
	t.Run("integrationWithNoMustViolations", func(t *testing.T) {
		out, e := RunAppAndCaptureOutput([]string{"", "lint", "../../server/src/test/resources/fixtures/no_must_violations.yaml"})

		assert.Contains(t, out, "MUST violations: 0")
		assert.Contains(t, out, "SHOULD violations: 2")
		assert.Contains(t, out, "MAY violations: 1")
		assert.Contains(t, out, "HINT violations: 0")

		assert.Nil(t, e)
	})
}

func TestIntegrationWithZallyApiDefinition(t *testing.T) {
	t.Run("integrationWithZallyApiDefinition", func(t *testing.T) {
		out, e := RunAppAndCaptureOutput([]string{"", "lint", "../../server/src/main/resources/api/zally-api.yaml"})

		assert.Contains(t, out, "[33mSHOULD[0m [33mUse Specific HTTP Status Codes[0m")
		assert.Contains(t, out, "MUST violations: 0")
		assert.Contains(t, out, "SHOULD violations: 1")
		assert.Contains(t, out, "MAY violations: 0")
		assert.Contains(t, out, "HINT violations: 0")

		assert.Nil(t, e)
	})
}

func TestIntegrationDisplayRulesList(t *testing.T) {
	t.Run("integrationDisplayRulesList", func(t *testing.T) {
		out, e := RunAppAndCaptureOutput([]string{"", "rules"})

		assert.Contains(t, out, "[31mM001[0m [31mMUST[0m: Avoid Link in Header Rule")
		assert.Contains(t, out, "http://zalando.github.io/restful-api-guidelines/hyper-media/Hypermedia.html#must-do-not-use-link-headers-with-json-entities")

		assert.Nil(t, e)
	})
}

func TestIntegrationNotReceiveDeprecationMessage(t *testing.T) {
	t.Run("notShowDeprecationMessage", func(t *testing.T) {
		out, e := RunAppAndCaptureOutput([]string{"", "rules"})

		assert.NotContains(t, out, "Please update your CLI:")

		assert.Nil(t, e)
	})
}

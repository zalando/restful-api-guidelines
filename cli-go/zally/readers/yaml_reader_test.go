package readers

import (
	"testing"

	"encoding/json"

	"github.com/zalando-incubator/zally/cli-go/zally/tests"
)

func TestYAMLReader(t *testing.T) {
	t.Run("reads_proper_yaml", func(t *testing.T) {
		fixture := "swagger: \"2.0\"\ninfo:\n  title: Partner Service Adapter\n"
		expected := "{\"info\":{\"title\":\"Partner Service Adapter\"},\"swagger\":\"2.0\"}"

		data := []byte(fixture)
		reader := NewYAMLReader(data)

		result, err := reader.Read()

		outputResult, err := result.MarshalJSON()

		tests.AssertEquals(t, nil, err)
		tests.AssertEquals(t, expected, string(outputResult))
	})

	t.Run("raises_error_when_yaml_cannot_be_parsed", func(t *testing.T) {
		data := []byte("swagger:\n2.0")
		reader := NewYAMLReader(data)

		result, err := reader.Read()

		tests.AssertEquals(
			t,
			"yaml: line 2: could not find expected ':'",
			err.Error())
		tests.AssertEquals(t, json.RawMessage(nil), result)
	})
}

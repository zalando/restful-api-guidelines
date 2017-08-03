package readers

import (
	"testing"

	"github.com/zalando-incubator/zally/cli-go/zally/tests"

	"encoding/json"
)

func TestJSONReader(t *testing.T) {
	t.Run("reads_proper_json", func(t *testing.T) {
		fixture := "{\"swagger\": \"2.0\"}"

		data := []byte(fixture)
		reader := NewJSONReader(data)

		result, err := reader.Read()

		outputResult, err := result.MarshalJSON()

		tests.AssertEquals(t, nil, err)
		tests.AssertEquals(t, []byte(fixture), outputResult)
	})

	t.Run("raises_error_when_json_cannot_be_parsed", func(t *testing.T) {
		data := []byte("{\"swagger\": \"2.0")
		reader := NewJSONReader(data)

		result, err := reader.Read()

		tests.AssertEquals(t, "unexpected end of JSON input", err.Error())
		tests.AssertEquals(t, json.RawMessage(nil), result)
	})

	t.Run("raises_error_when_json_is_empty", func(t *testing.T) {
		data := []byte("")
		reader := NewJSONReader(data)

		result, err := reader.Read()

		tests.AssertEquals(t, "unexpected end of JSON input", err.Error())
		tests.AssertEquals(t, json.RawMessage(nil), result)
	})
}

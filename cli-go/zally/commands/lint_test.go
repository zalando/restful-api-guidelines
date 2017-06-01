package commands

import (
	"testing"

	"encoding/json"

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

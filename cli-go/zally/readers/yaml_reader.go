package readers

import (
	"encoding/json"

	"github.com/ghodss/yaml"
)

// YAMLReader reads Swagger definition in YAML format
type YAMLReader struct {
	Data []byte
}

func (j *YAMLReader) Read() (json.RawMessage, error) {
	var jsonData json.RawMessage

	converted, err := yaml.YAMLToJSON(j.Data)

	if err != nil {
		return nil, err
	}

	err = json.Unmarshal(converted, &jsonData)
	if err != nil {
		return nil, err
	}

	return jsonData, nil
}

// NewYAMLReader creates a new YAMLReader instance
func NewYAMLReader(Data []byte) *YAMLReader {
	reader := new(YAMLReader)
	reader.Data = Data
	return reader
}

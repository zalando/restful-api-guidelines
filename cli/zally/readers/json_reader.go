package readers

import (
	"encoding/json"
)

// JSONReader reads Swagger definition in JSON format
type JSONReader struct {
	Data []byte
}

func (j *JSONReader) Read() (json.RawMessage, error) {
	var jsonData json.RawMessage

	var err = json.Unmarshal(j.Data, &jsonData)
	if err != nil {
		return nil, err
	}

	return jsonData, nil
}

// NewJSONReader creates a new JSONReader instance
func NewJSONReader(Data []byte) *JSONReader {
	reader := new(JSONReader)
	reader.Data = Data
	return reader
}

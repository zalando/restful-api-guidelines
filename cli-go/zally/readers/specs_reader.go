package readers

import (
	"encoding/json"
)

// SpecsReader is an interface for all Swagger specification readers
type SpecsReader interface {
	read() (json.RawMessage, error)
}

package domain

import (
	"encoding/json"
)

// APIViolationsRequest is a wrapper around API definition
type APIViolationsRequest struct {
	APIDefinition *json.RawMessage `json:"api_definition"`
}

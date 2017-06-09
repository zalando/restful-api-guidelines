package domain

import "bytes"

// Violations stores list of violations
type Violations struct {
	Violations      []Violation     `json:"violations"`
	ViolationsCount ViolationsCount `json:"violations_count"`
}

// ToString creates string representation of Violation
func (v *Violations) ToString() string {
	var buffer bytes.Buffer

	for _, violation := range v.Violations {
		buffer.WriteString(violation.ToString())
	}
	return buffer.String()
}

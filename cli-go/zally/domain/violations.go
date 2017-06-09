package domain

import (
	"bytes"
	"fmt"
)

// Violations stores list of violations
type Violations struct {
	Violations      []Violation     `json:"violations"`
	ViolationsCount ViolationsCount `json:"violations_count"`
}

// ToString creates string representation of Violation
func (v *Violations) ToString() string {
	var buffer bytes.Buffer

	if len(v.Violations) > 0 {
		fmt.Fprint(&buffer, "Violations:\n===========\n\n")
		for _, violation := range v.Violations {
			fmt.Fprint(&buffer, violation.ToString())
		}

		fmt.Fprint(&buffer, "Summary:\n========\n\n")
		fmt.Fprint(&buffer, v.ViolationsCount.ToString())
	}

	return buffer.String()
}

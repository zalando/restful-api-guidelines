package domain

import (
	"bytes"
	"fmt"
	"strings"
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

// Must returns must violations
func (v *Violations) Must() []Violation {
	return v.filterViolations("MUST")
}

// Should returns should violations
func (v *Violations) Should() []Violation {
	return v.filterViolations("SHOULD")
}

// May returns may violations
func (v *Violations) May() []Violation {
	return v.filterViolations("MAY")
}

// Hint returns hint violations
func (v *Violations) Hint() []Violation {
	return v.filterViolations("HINT")
}

func (v *Violations) filterViolations(violationType string) []Violation {
	result := []Violation{}
	for _, violation := range v.Violations {
		if strings.ToUpper(violation.ViolationType) == strings.ToUpper(violationType) {
			result = append(result, violation)
		}
	}
	return result
}

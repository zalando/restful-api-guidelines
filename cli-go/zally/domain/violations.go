package domain

import (
	"strings"
)

// Violations stores api_violations response
type Violations struct {
	Violations      []Violation     `json:"violations"`
	ViolationsCount ViolationsCount `json:"violations_count"`
	Message         string          `json:"message"`
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

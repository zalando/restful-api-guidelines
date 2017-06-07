package domain

import (
	"bytes"
	"fmt"
)

// Violation keeps information about Zally violations
type Violation struct {
	Title         string   `json:"title"`
	Decription    string   `json:"description"`
	ViolationType string   `json:"violation_type"`
	RuleLink      string   `json:"rule_link"`
	Paths         []string `json:"paths"`
}

// ToString creates string representation of Violation
func (v *Violation) ToString() string {
	var buffer bytes.Buffer

	buffer.WriteString(fmt.Sprintf("%s %s\n", v.ViolationType, v.Title))
	buffer.WriteString(fmt.Sprintf("\t%s\n", v.Decription))
	buffer.WriteString(fmt.Sprintf("\t%s\n", v.RuleLink))

	for _, path := range v.Paths {
		buffer.WriteString(fmt.Sprintf("\t\t%s\n", path))
	}

	buffer.WriteString("\n")

	return buffer.String()
}

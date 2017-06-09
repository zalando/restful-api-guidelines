package domain

import "bytes"
import "fmt"

// ViolationsCount contains violation counters
type ViolationsCount struct {
	Must   int `json:"must"`
	Should int `json:"should"`
	May    int `json:"may"`
	Hint   int `json:"hint"`
}

// ToString creates string representation of Violation
func (v *ViolationsCount) ToString() string {
	var buffer bytes.Buffer
	fmt.Fprintf(&buffer, "MUST violations: %d\n", v.Must)
	fmt.Fprintf(&buffer, "SHOULD violations: %d\n", v.Should)
	fmt.Fprintf(&buffer, "MAY violations: %d\n", v.May)
	fmt.Fprintf(&buffer, "HINT violations: %d\n", v.Hint)
	return buffer.String()
}

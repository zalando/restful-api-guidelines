package domain

import (
	"testing"

	"github.com/zalando-incubator/zally/cli-go/zally/utils"
)

func TestViolations(t *testing.T) {
	t.Run("ToString returns empty string if no violations", func(t *testing.T) {
		var violations Violations
		actualResult := violations.ToString()

		utils.AssertEquals(t, "", actualResult)
	})

	t.Run("ToString returns list of violation strings", func(t *testing.T) {
		var firstViolation Violation
		firstViolation.Title = "First Title"
		firstViolation.RuleLink = "http://example.com/firstViolation"
		firstViolation.ViolationType = "MUST"
		firstViolation.Decription = "First Description"
		firstViolation.Paths = []string{"/path/one", "/path/two"}

		var secondViolation Violation
		firstViolation.Title = "Second Title"
		firstViolation.RuleLink = "http://example.com/firstViolation"
		firstViolation.ViolationType = "SHOULD"
		firstViolation.Decription = "Second Description"
		firstViolation.Paths = []string{"/path/three", "/path/four"}

		var violations Violations
		violations.Violations = []Violation{firstViolation, secondViolation}

		actualResult := violations.ToString()
		expectedResult := firstViolation.ToString() + secondViolation.ToString()

		utils.AssertEquals(t, expectedResult, actualResult)
	})
}

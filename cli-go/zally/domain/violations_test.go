package domain

import (
	"testing"

	"fmt"

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

		var violationsCount ViolationsCount
		violationsCount.Must = 1
		violationsCount.Should = 2
		violationsCount.May = 3
		violationsCount.Hint = 4

		var violations Violations
		violations.Violations = []Violation{firstViolation, secondViolation}
		violations.ViolationsCount = violationsCount

		actualResult := violations.ToString()
		expectedResult := fmt.Sprintf(
			"Violations:\n===========\n\n%s%sSummary:\n========\n\n%s",
			firstViolation.ToString(),
			secondViolation.ToString(),
			violationsCount.ToString())

		utils.AssertEquals(t, expectedResult, actualResult)
	})
}

package domain

import (
	"testing"

	"fmt"

	"github.com/zalando-incubator/zally/cli-go/zally/utils"
)

func TestViolations(t *testing.T) {
	var mustViolation Violation
	mustViolation.Title = "Must Title"
	mustViolation.RuleLink = "http://example.com/mustViolation"
	mustViolation.ViolationType = "MUST"
	mustViolation.Decription = "Must Description"
	mustViolation.Paths = []string{"/path/one", "/path/two"}

	var shouldViolation Violation
	shouldViolation.Title = "Should Title"
	shouldViolation.RuleLink = "http://example.com/shouldViolation"
	shouldViolation.ViolationType = "SHOULD"
	shouldViolation.Decription = "Should Description"
	shouldViolation.Paths = []string{"/path/three", "/path/four"}

	var mayViolation Violation
	mayViolation.Title = "May Title"
	mayViolation.RuleLink = "http://example.com/mayViolation"
	mayViolation.ViolationType = "MAY"
	mayViolation.Decription = "May Description"
	mayViolation.Paths = []string{"/path/five", "/path/six"}

	var hintViolation Violation
	hintViolation.Title = "Hint Title"
	hintViolation.RuleLink = "http://example.com/hintViolation"
	hintViolation.ViolationType = "HINT"
	hintViolation.Decription = "Hint Description"
	hintViolation.Paths = []string{"/path/seven", "/path/eight"}

	var violationsCount ViolationsCount
	violationsCount.Must = 1
	violationsCount.Should = 2
	violationsCount.May = 3
	violationsCount.Hint = 4

	var violations Violations
	violations.Violations = []Violation{mustViolation, shouldViolation, mayViolation, hintViolation}
	violations.ViolationsCount = violationsCount

	t.Run("ToString returns empty string if no violations", func(t *testing.T) {
		var violations Violations
		actualResult := violations.ToString()

		utils.AssertEquals(t, "", actualResult)
	})

	t.Run("ToString returns list of violation strings", func(t *testing.T) {
		actualResult := violations.ToString()
		expectedResult := fmt.Sprintf(
			"Violations:\n===========\n\n%s%s%s%sSummary:\n========\n\n%s",
			mustViolation.ToString(),
			shouldViolation.ToString(),
			mayViolation.ToString(),
			hintViolation.ToString(),
			violationsCount.ToString())

		utils.AssertEquals(t, expectedResult, actualResult)
	})

	t.Run("filterViolations returns filtered list of MUST violations", func(t *testing.T) {
		actualResult := violations.filterViolations("MUST")
		expectedResult := []Violation{mustViolation}

		utils.AssertEquals(t, expectedResult, actualResult)
	})

	t.Run("filterViolations returns filtered list of SHOULD violations", func(t *testing.T) {
		actualResult := violations.filterViolations("SHOULD")
		expectedResult := []Violation{shouldViolation}

		utils.AssertEquals(t, expectedResult, actualResult)
	})

	t.Run("filterViolations uses case-insensitive type", func(t *testing.T) {
		actualResult := violations.filterViolations("Should")
		expectedResult := []Violation{shouldViolation}

		utils.AssertEquals(t, expectedResult, actualResult)
	})

	t.Run("Must filters by MUST violations", func(t *testing.T) {
		actualResult := violations.Must()
		expectedResult := violations.filterViolations("Must")

		utils.AssertEquals(t, expectedResult, actualResult)

	})

	t.Run("Should filters by SHOULD violations", func(t *testing.T) {
		actualResult := violations.Should()
		expectedResult := violations.filterViolations("Should")

		utils.AssertEquals(t, expectedResult, actualResult)

	})

	t.Run("May filters by MAY violations", func(t *testing.T) {
		actualResult := violations.May()
		expectedResult := violations.filterViolations("May")

		utils.AssertEquals(t, expectedResult, actualResult)

	})

	t.Run("Hint filters by HINT violations", func(t *testing.T) {
		actualResult := violations.Hint()
		expectedResult := violations.filterViolations("Hint")

		utils.AssertEquals(t, expectedResult, actualResult)
	})
}

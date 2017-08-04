package domain

import (
	"testing"

	"github.com/zalando-incubator/zally/cli/zally/tests"
)

func TestRules(t *testing.T) {
	var mustRule Rule
	mustRule.Title = "Must Title"
	mustRule.URL = "http://example.com/mustRule"
	mustRule.Type = "MUST"
	mustRule.Code = "M001"
	mustRule.IsActive = true

	var shouldRule Rule
	shouldRule.Title = "Should Title"
	shouldRule.URL = "http://example.com/shouldRule"
	shouldRule.Type = "SHOULD"
	shouldRule.Code = "S001"
	shouldRule.IsActive = true

	var mayRule Rule
	mayRule.Title = "May Title"
	mayRule.URL = "http://example.com/mayRule"
	mayRule.Type = "MAY"
	mayRule.Code = "M001"
	mayRule.IsActive = true

	var hintRule Rule
	hintRule.Title = "Hint Title"
	hintRule.URL = "http://example.com/hintRule"
	hintRule.Type = "HINT"
	hintRule.Code = "M001"
	hintRule.IsActive = true

	var Rules Rules
	Rules.Rules = []Rule{mustRule, shouldRule, mayRule, hintRule}

	t.Run("filterRules returns filtered list of MUST Rules", func(t *testing.T) {
		actualResult := Rules.filterRules("MUST")
		expectedResult := []Rule{mustRule}

		tests.AssertEquals(t, expectedResult, actualResult)
	})

	t.Run("filterRules returns filtered list of SHOULD Rules", func(t *testing.T) {
		actualResult := Rules.filterRules("SHOULD")
		expectedResult := []Rule{shouldRule}

		tests.AssertEquals(t, expectedResult, actualResult)
	})

	t.Run("filterRules uses case-insensitive type", func(t *testing.T) {
		actualResult := Rules.filterRules("Should")
		expectedResult := []Rule{shouldRule}

		tests.AssertEquals(t, expectedResult, actualResult)
	})

	t.Run("Must filters by MUST Rules", func(t *testing.T) {
		actualResult := Rules.Must()
		expectedResult := Rules.filterRules("Must")

		tests.AssertEquals(t, expectedResult, actualResult)

	})

	t.Run("Should filters by SHOULD Rules", func(t *testing.T) {
		actualResult := Rules.Should()
		expectedResult := Rules.filterRules("Should")

		tests.AssertEquals(t, expectedResult, actualResult)

	})

	t.Run("May filters by MAY Rules", func(t *testing.T) {
		actualResult := Rules.May()
		expectedResult := Rules.filterRules("May")

		tests.AssertEquals(t, expectedResult, actualResult)

	})

	t.Run("Hint filters by HINT Rules", func(t *testing.T) {
		actualResult := Rules.Hint()
		expectedResult := Rules.filterRules("Hint")

		tests.AssertEquals(t, expectedResult, actualResult)
	})
}

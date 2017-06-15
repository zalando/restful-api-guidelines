package utils

import (
	"fmt"
	"testing"

	"bytes"

	"github.com/logrusorgru/aurora"
	"github.com/zalando-incubator/zally/cli-go/zally/domain"
	"github.com/zalando-incubator/zally/cli-go/zally/tests"
)

func TestNewResultPrinter(t *testing.T) {
	t.Run("accepts buffer", func(t *testing.T) {
		var buffer *bytes.Buffer

		resultPrinter := NewResultPrinter(buffer)

		tests.AssertEquals(t, buffer, resultPrinter.buffer)
	})
}

func TestColorizeByTypeFunc(t *testing.T) {
	var buffer *bytes.Buffer
	resultPrinter := NewResultPrinter(buffer)

	t.Run("Returns red when type is MUST", func(t *testing.T) {
		result := resultPrinter.colorizeByTypeFunc("MUST")
		tests.AssertEquals(t, aurora.Red("abcde"), result("abcde"))
	})

	t.Run("Returns brown when type is SHOULD", func(t *testing.T) {
		result := resultPrinter.colorizeByTypeFunc("SHOULD")
		tests.AssertEquals(t, aurora.Brown("abcde"), result("abcde"))
	})

	t.Run("Returns green when type is MAY", func(t *testing.T) {
		result := resultPrinter.colorizeByTypeFunc("MAY")
		tests.AssertEquals(t, aurora.Green("abcde"), result("abcde"))
	})

	t.Run("Returns cyan when type is HINT", func(t *testing.T) {
		result := resultPrinter.colorizeByTypeFunc("HINT")
		tests.AssertEquals(t, aurora.Cyan("abcde"), result("abcde"))
	})

	t.Run("Returns gray by default", func(t *testing.T) {
		result := resultPrinter.colorizeByTypeFunc("WHATEVER")
		tests.AssertEquals(t, aurora.Gray("abcde"), result("abcde"))
	})
}

func TestPrintRule(t *testing.T) {
	t.Run("Prints single rule", func(t *testing.T) {
		var buffer bytes.Buffer
		resultPrinter := NewResultPrinter(&buffer)

		var rule domain.Rule
		rule.Title = "Must Rule"
		rule.Type = "MUST"
		rule.Code = "M001"
		rule.IsActive = true
		rule.URL = "https://example.com/rule"

		resultPrinter.printRule(&rule)

		tests.AssertEquals(
			t,
			"\x1b[31mM001\x1b[0m \x1b[31mMUST\x1b[0m: Must Rule\n\thttps://example.com/rule\n\n",
			buffer.String())
	})
}

func TestPrintRules(t *testing.T) {
	t.Run("Prints all rules when found", func(t *testing.T) {
		var buffer bytes.Buffer
		resultPrinter := NewResultPrinter(&buffer)

		var firstRule domain.Rule
		firstRule.Title = "First Rule"
		firstRule.Type = "MUST"
		firstRule.Code = "M001"
		firstRule.IsActive = true
		firstRule.URL = "https://example.com/first-rule"

		var secondRule domain.Rule
		secondRule.Title = "Second Rule"
		secondRule.Type = "SHOULD"
		secondRule.Code = "S001"
		secondRule.IsActive = true
		secondRule.URL = "https://example.com/second-rule"

		var rules domain.Rules
		rules.Rules = []domain.Rule{firstRule, secondRule}

		resultPrinter.PrintRules(&rules)

		tests.AssertEquals(
			t,
			"\x1b[31mM001\x1b[0m \x1b[31mMUST\x1b[0m: First Rule\n\thttps://example.com/first-rule\n\n"+
				"\x1b[33mS001\x1b[0m \x1b[33mSHOULD\x1b[0m: Second Rule\n\thttps://example.com/second-rule\n\n",
			buffer.String())
	})
}

func TestFormatHeader(t *testing.T) {
	var buffer bytes.Buffer
	resultPrinter := NewResultPrinter(&buffer)

	t.Run("formatHeader adds a line", func(t *testing.T) {

		actualResult := resultPrinter.formatHeader("Header")
		expectedResult := "Header\n======\n\n"

		tests.AssertEquals(t, expectedResult, actualResult)
	})

	t.Run("formatHeader returns empty string when no header", func(t *testing.T) {
		result := resultPrinter.formatHeader("")
		tests.AssertEquals(t, "", result)
	})
}

func TestPrintViolations(t *testing.T) {
	var buffer bytes.Buffer
	resultPrinter := NewResultPrinter(&buffer)

	var mustViolation domain.Violation
	mustViolation.Title = "Must Title"
	mustViolation.RuleLink = "http://example.com/mustViolation"
	mustViolation.ViolationType = "MUST"
	mustViolation.Decription = "Must Description"
	mustViolation.Paths = []string{"/path/one", "/path/two"}

	var shouldViolation domain.Violation
	shouldViolation.Title = "Should Title"
	shouldViolation.RuleLink = "http://example.com/shouldViolation"
	shouldViolation.ViolationType = "SHOULD"
	shouldViolation.Decription = "Should Description"
	shouldViolation.Paths = []string{"/path/three", "/path/four"}

	var violationsCount domain.ViolationsCount
	violationsCount.Must = 1
	violationsCount.Should = 2
	violationsCount.May = 3
	violationsCount.Hint = 4

	var violations domain.Violations
	violations.Violations = []domain.Violation{mustViolation, shouldViolation}
	violations.ViolationsCount = violationsCount

	t.Run("printViolations prints violations and header", func(t *testing.T) {
		buffer.Reset()
		resultPrinter.printViolations("MUST", violations.Must())

		actualResult := string(buffer.Bytes())
		expectedResult := fmt.Sprintf("MUST\n====\n\n%s", resultPrinter.formatViolation(&mustViolation))

		tests.AssertEquals(t, expectedResult, actualResult)
	})

	t.Run("printViolations prints nothing when no violations", func(t *testing.T) {
		buffer.Reset()
		resultPrinter.printViolations("MUST", []domain.Violation{})

		result := string(buffer.Bytes())

		tests.AssertEquals(t, "", result)
	})

	t.Run("PrintViolations prints nothing if no violations", func(t *testing.T) {
		buffer.Reset()

		var violations domain.Violations
		resultPrinter.PrintViolations(&violations)

		result := string(buffer.Bytes())
		tests.AssertEquals(t, "", result)
	})

	t.Run("PrintViolations returns list of violation strings", func(t *testing.T) {
		buffer.Reset()

		resultPrinter.PrintViolations(&violations)

		actualResult := string(buffer.Bytes())
		expectedResult := fmt.Sprintf(
			"MUST\n====\n\n%sSHOULD\n======\n\n%sSummary:\n========\n\n%s",
			resultPrinter.formatViolation(&mustViolation),
			resultPrinter.formatViolation(&shouldViolation),
			resultPrinter.formatViolationsCount(&violationsCount))

		tests.AssertEquals(t, expectedResult, actualResult)
	})
}

func TestFormatViolation(t *testing.T) {
	var buffer bytes.Buffer
	resultPrinter := NewResultPrinter(&buffer)

	t.Run("Converts violation to string", func(t *testing.T) {

		var violation domain.Violation
		violation.Title = "Test Title"
		violation.RuleLink = "http://example.com/violation"
		violation.ViolationType = "MUST"
		violation.Decription = "Test Description"
		violation.Paths = []string{"/path/one", "/path/two"}

		actualResult := resultPrinter.formatViolation(&violation)
		expectedResult := "MUST Test Title\n\tTest Description\n\thttp://example.com/violation\n\t\t/path/one\n\t\t/path/two\n\n"

		tests.AssertEquals(t, expectedResult, actualResult)
	})
}

func TestViolationsCount(t *testing.T) {
	t.Run("ToString converts ViolationsCount to string", func(t *testing.T) {
		var buffer bytes.Buffer
		resultPrinter := NewResultPrinter(&buffer)

		var count domain.ViolationsCount
		count.Must = 1
		count.Should = 2
		count.May = 3
		count.Hint = 4

		actualResult := resultPrinter.formatViolationsCount(&count)
		expectedResult := "MUST violations: 1\nSHOULD violations: 2\nMAY violations: 3\nHINT violations: 4\n"

		tests.AssertEquals(t, expectedResult, actualResult)
	})
}

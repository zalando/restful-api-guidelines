package domain

import (
	"testing"

	"github.com/zalando-incubator/zally/cli-go/zally/tests"
)

func TestViolation(t *testing.T) {
	t.Run("ToString converts violation to string", func(t *testing.T) {

		var violation Violation
		violation.Title = "Test Title"
		violation.RuleLink = "http://example.com/violation"
		violation.ViolationType = "MUST"
		violation.Decription = "Test Description"
		violation.Paths = []string{"/path/one", "/path/two"}

		actualResult := violation.ToString()
		expectedResult := "MUST Test Title\n\tTest Description\n\thttp://example.com/violation\n\t\t/path/one\n\t\t/path/two\n\n"

		tests.AssertEquals(t, expectedResult, actualResult)
	})
}

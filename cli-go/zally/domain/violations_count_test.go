package domain

import (
	"testing"

	"github.com/zalando-incubator/zally/cli-go/zally/utils"
)

func TestViolationsCount(t *testing.T) {
	t.Run("ToString converts ViolationsCount to string", func(t *testing.T) {
		var count ViolationsCount
		count.Must = 1
		count.Should = 2
		count.May = 3
		count.Hint = 4

		actualResult := count.ToString()
		expectedResult := "MUST violations: 1\nSHOULD violations: 2\nMAY violations: 3\nHINT violations: 4\n"

		utils.AssertEquals(t, expectedResult, actualResult)
	})
}

package tests

import (
	"github.com/urfave/cli"
	"reflect"
	"testing"
)

// AssertEquals checks that expected and actual values are equal
func AssertEquals(t *testing.T, expected interface{}, actual interface{}) {
	if !reflect.DeepEqual(expected, actual) {
		t.Errorf("Expected %#v, Actual %#v", expected, actual)
	}
}

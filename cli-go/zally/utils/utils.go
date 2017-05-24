package utils

import (
	"reflect"
	"testing"
)

func AssertEquals(t *testing.T, expected interface{}, actual interface{}) {
	if !reflect.DeepEqual(expected, actual) {
		t.Errorf("Expected %#v, Actual %#v", expected, actual)
	}
}

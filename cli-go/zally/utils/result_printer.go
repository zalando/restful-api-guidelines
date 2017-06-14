package utils

import (
	"fmt"
	"io"

	"github.com/logrusorgru/aurora"
	"github.com/zalando-incubator/zally/cli-go/zally/domain"
)

// ResultPrinter helps to print results to the CLI
type ResultPrinter struct {
	buffer io.Writer
}

// NewResultPrinter creates an instance of ResultPrinter
func NewResultPrinter(buffer io.Writer) ResultPrinter {
	var resultPrinter ResultPrinter
	resultPrinter.buffer = buffer
	return resultPrinter
}

// PrintRules prints a list of supported rules
func (r *ResultPrinter) PrintRules(rules *domain.Rules) {
	for _, rule := range rules.Rules {
		r.printRule(&rule)
	}
}

func (r *ResultPrinter) printRule(rule *domain.Rule) {
	colorize := r.colorizeByTypeFunc(rule.Type)
	fmt.Fprintf(
		r.buffer,
		"%s %s: %s\n\t%s\n\n",
		colorize(rule.Code),
		colorize(rule.Type),
		rule.Title,
		rule.URL)
}

func (r *ResultPrinter) colorizeByTypeFunc(ruleType string) func(interface{}) aurora.Value {
	switch ruleType {
	case "MUST":
		return aurora.Red
	case "SHOULD":
		return aurora.Brown
	case "MAY":
		return aurora.Green
	case "HINT":
		return aurora.Cyan
	default:
		return aurora.Gray
	}
}

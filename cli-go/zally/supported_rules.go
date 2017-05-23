package main

import (
	"fmt"
	"net/http"

	"encoding/json"

	"github.com/urfave/cli"
)

// SupportedRulesCommand lists supported rules
var SupportedRulesCommand = cli.Command{
	Name:   "rules",
	Usage:  "List supported rules",
	Action: listRules,
}

func listRules(c *cli.Context) error {
	response, err := http.Get(fmt.Sprintf("%s/supported-rules", c.GlobalString("linter-service")))
	if err != nil {
		return err
	}

	decoder := json.NewDecoder(response.Body)

	var rules Rules
	decoder.Decode(&rules)

	for _, rule := range rules.Rules {
		printRule(&rule)
	}

	return nil
}

func printRule(rule *Rule) {
	fmt.Printf("%s %s: %s\n\t%s\n\n", rule.Type, rule.Code, rule.Title, rule.URL)
}

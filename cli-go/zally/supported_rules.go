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
	oauth2Token := c.GlobalString("token")
	client := &http.Client{}
	req, _ := http.NewRequest("GET", fmt.Sprintf("%s/supported-rules", c.GlobalString("linter-service")), nil)
	if len(oauth2Token) > 0 {
		req.Header.Add("Authorization", fmt.Sprintf("Bearer %s", oauth2Token))
	}

	response, err := client.Do(req)

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

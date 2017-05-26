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
	client := &http.Client{}
	request := buildRequest("GET", fmt.Sprintf("%s/supported-rules", c.GlobalString("linter-service")), c)
	response, err := client.Do(request)

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

func buildRequest(httpVerb string, path string, context *cli.Context) (request *http.Request) {
	req, err := http.NewRequest(httpVerb, path, nil)
	if err != nil {
		panic(err)
	}

	oauth2Token := context.GlobalString("token")
	if len(oauth2Token) > 0 {
		req.Header.Add("Authorization", fmt.Sprintf("Bearer %s", oauth2Token))
	}
	return req
}

func printRule(rule *Rule) {
	fmt.Printf("%s %s: %s\n\t%s\n\n", rule.Type, rule.Code, rule.Title, rule.URL)
}

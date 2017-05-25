package commands

import (
	"fmt"
	"net/http"

	"encoding/json"

	"github.com/logrusorgru/aurora"
	"github.com/urfave/cli"
	"github.com/zalando-incubator/zally/cli-go/zally/domain"
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

	var rules domain.Rules
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

func printRule(rule *domain.Rule) {
	colorize := colorizeByTypeFunc(rule.Type)
	fmt.Printf("%s %s: %s\n\t%s\n\n", colorize(rule.Code), colorize(rule.Type), rule.Title, rule.URL)
}

func colorizeByTypeFunc(ruleType string) func(interface{}) aurora.Value {
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

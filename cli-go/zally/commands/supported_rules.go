package commands

import (
	"bytes"
	"fmt"
	"io/ioutil"
	"net/http"

	"encoding/json"

	"github.com/urfave/cli"
	"github.com/zalando-incubator/zally/cli-go/zally/domain"
	"github.com/zalando-incubator/zally/cli-go/zally/utils"
)

// SupportedRulesCommand lists supported rules
var SupportedRulesCommand = cli.Command{
	Name:   "rules",
	Usage:  "List supported rules",
	Action: listRules,
	Flags: []cli.Flag{
		cli.StringFlag{
			Name:  "type",
			Usage: "Rules Type",
		},
	},
}

func listRules(c *cli.Context) error {
	requestBuilder := utils.NewRequestBuilder(c.GlobalString("linter-service"), c.GlobalString("token"))

	rules, err := fetchRules(requestBuilder, c.String("type"))
	if err != nil {
		return err
	}

	printRules(rules)

	return nil
}

func fetchRules(requestBuilder *utils.RequestBuilder, rulesType string) (*domain.Rules, error) {
	uri := "/supported-rules"
	if rulesType != "" {
		uri += "?type=" + rulesType
	}
	request, err := requestBuilder.Build("GET", uri, nil)
	if err != nil {
		return nil, err
	}

	client := &http.Client{}
	response, err := client.Do(request)
	if err != nil {
		return nil, err
	}

	if response.StatusCode != 200 {
		defer response.Body.Close()
		body, _ := ioutil.ReadAll(response.Body)

		return nil, fmt.Errorf(
			"Cannot submit file for linting. HTTP Status: %d, Response: %s", response.StatusCode, string(body))
	}

	decoder := json.NewDecoder(response.Body)

	var rules domain.Rules
	decoder.Decode(&rules)

	return &rules, nil
}

func printRules(rules *domain.Rules) {
	var buffer bytes.Buffer
	resultPrinter := utils.NewResultPrinter(&buffer)
	resultPrinter.PrintRules(rules)

	fmt.Print(buffer.String())
}

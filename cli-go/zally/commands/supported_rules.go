package commands

import (
	"bytes"
	"fmt"
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
}

func listRules(c *cli.Context) error {
	client := &http.Client{}
	requestBuilder := utils.NewRequestBuilder(c.GlobalString("linter-service"), c.GlobalString("token"))
	request, err := requestBuilder.Build("GET", "/supported-rules", nil)
	if err != nil {
		return err
	}

	response, err := client.Do(request)
	if err != nil {
		return err
	}

	decoder := json.NewDecoder(response.Body)

	var rules domain.Rules
	decoder.Decode(&rules)

	var buffer bytes.Buffer
	resultPrinter := utils.NewResultPrinter(&buffer)
	resultPrinter.PrintRules(&rules)

	fmt.Print(buffer.String())

	return nil
}

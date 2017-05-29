package commands

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"path/filepath"

	"encoding/json"

	"bytes"

	"github.com/urfave/cli"
	"github.com/zalando-incubator/zally/cli-go/zally/domain"
	"github.com/zalando-incubator/zally/cli-go/zally/readers"
)

// LintCommand lints given API definition file
var LintCommand = cli.Command{
	Name:      "lint",
	Usage:     "Lint given `FILE` with API definition",
	Action:    lintFile,
	ArgsUsage: "FILE",
}

func lintFile(c *cli.Context) error {
	if !c.Args().Present() {
		cli.ShowCommandHelp(c, c.Command.Name)
		return fmt.Errorf("Please specify Swagger File")
	}

	data, err := readFile(c.Args().First())
	if err != nil {
		return err
	}

	violations, err := doRequest(c.GlobalString("linter-service"), c.GlobalString("token"), data)

	fmt.Print("Violations:\n===========\n\n")
	fmt.Print(violations.ToString())

	return err
}

func readFile(file string) (json.RawMessage, error) {
	absolutePath, err := filepath.Abs(file)
	if err != nil {
		return nil, err
	}

	contents, err := ioutil.ReadFile(absolutePath)
	if err != nil {
		return nil, err
	}

	reader := readers.NewJSONReader(contents)

	return reader.Read()
}

func doRequest(baseURL string, token string, data json.RawMessage) (*domain.Violations, error) {
	var apiViolationsRequest domain.APIViolationsRequest
	apiViolationsRequest.APIDefinition = &data
	requestBody, err := json.MarshalIndent(apiViolationsRequest, "", "  ")
	if err != nil {
		return nil, err
	}

	client := &http.Client{}
	request := buildRequest("POST", fmt.Sprintf("%s/api-violations", baseURL), token, bytes.NewBuffer(requestBody))
	request.Header.Add("Content-Type", "application/json")
	response, err := client.Do(request)
	if err != nil {
		return nil, err
	}

	decoder := json.NewDecoder(response.Body)
	var violations domain.Violations
	err = decoder.Decode(&violations)

	return &violations, err
}

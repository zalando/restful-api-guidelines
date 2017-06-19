package commands

import (
	"fmt"
	"io/ioutil"
	"net/http"

	"encoding/json"

	"bytes"

	"path/filepath"

	"strings"

	"github.com/urfave/cli"
	"github.com/zalando-incubator/zally/cli-go/zally/domain"
	"github.com/zalando-incubator/zally/cli-go/zally/readers"
	"github.com/zalando-incubator/zally/cli-go/zally/utils"
)

// LintCommand lints given API definition file
var LintCommand = cli.Command{
	Name:      "lint",
	Usage:     "Lint given `FILE` with API definition",
	Action:    lint,
	ArgsUsage: "FILE",
}

func lint(c *cli.Context) error {
	if !c.Args().Present() {
		cli.ShowCommandHelp(c, c.Command.Name)
		return fmt.Errorf("Please specify Swagger File")
	}

	path := c.Args().First()
	requestBuilder := utils.NewRequestBuilder(c.GlobalString("linter-service"), c.GlobalString("token"))

	return lintFile(path, requestBuilder)
}

func lintFile(path string, requestBuilder *utils.RequestBuilder) error {
	data, err := readFile(path)
	if err != nil {
		return err
	}

	violations, err := doRequest(requestBuilder, data)
	if err != nil {
		return err
	}

	fmt.Print(violations.ToString())
	return nil
}

func readFile(path string) (json.RawMessage, error) {
	absolutePath, err := filepath.Abs(path)
	if err != nil {
		return nil, err
	}

	contents, err := ioutil.ReadFile(absolutePath)
	if err != nil {
		return nil, err
	}

	return getReader(absolutePath, contents).Read()
}

func doRequest(requestBuilder *utils.RequestBuilder, data json.RawMessage) (*domain.Violations, error) {
	var apiViolationsRequest domain.APIViolationsRequest
	apiViolationsRequest.APIDefinition = &data
	requestBody, err := json.MarshalIndent(apiViolationsRequest, "", "  ")
	if err != nil {
		return nil, err
	}

	request, err := requestBuilder.Build("POST", "/api-violations", bytes.NewBuffer(requestBody))
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
	var violations domain.Violations
	err = decoder.Decode(&violations)
	if err != nil {
		return nil, err
	}

	return &violations, nil
}

func getReader(path string, contents []byte) readers.SpecsReader {
	extension := strings.ToLower(filepath.Ext(path))
	if extension == ".yml" || extension == ".yaml" {
		return readers.NewYAMLReader(contents)
	}
	return readers.NewJSONReader(contents)
}

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

func lint(c *cli.Context) (bool, error) {
	if !c.Args().Present() {
		cli.ShowCommandHelp(c, c.Command.Name)
		return false, fmt.Errorf("Please specify Swagger File")
	}

	path := c.Args().First()
	requestBuilder := utils.NewRequestBuilder(c.GlobalString("linter-service"), c.GlobalString("token"))

	return lintFile(path, requestBuilder)
}

func lintFile(path string, requestBuilder *utils.RequestBuilder) (bool, error) {
	anyMustViolations := false
	data, err := readFile(path)
	if err != nil {
		return anyMustViolations, err
	}

	violations, err := doRequest(requestBuilder, data)
	if err != nil {
		return anyMustViolations, err
	}

	if len(violations.Must()) > 0 {
		anyMustViolations = true
	}

	var buffer bytes.Buffer
	resultPrinter := utils.NewResultPrinter(&buffer)
	resultPrinter.PrintViolations(violations)

	fmt.Print(buffer.String())

	return anyMustViolations, nil
}

func readFile(path string) (json.RawMessage, error) {
	var contents []byte
	var err error

	if strings.HasPrefix(path, "http://") || strings.HasPrefix(path, "https://") {
		contents, err = readRemoteFile(path)
	} else {
		contents, err = readLocalFile(path)
	}

	if err != nil {
		return nil, err
	}

	return getReader(path, contents).Read()
}

func readLocalFile(path string) ([]byte, error) {
	absolutePath, err := filepath.Abs(path)
	if err != nil {
		return nil, err
	}

	return ioutil.ReadFile(absolutePath)
}

func readRemoteFile(url string) ([]byte, error) {
	response, err := http.Get(url)
	if err != nil {
		return nil, err
	}

	defer response.Body.Close()
	return ioutil.ReadAll(response.Body)
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

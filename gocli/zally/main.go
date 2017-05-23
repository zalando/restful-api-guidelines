package main

import (
	"fmt"
	"os"

	"github.com/urfave/cli"
)

func main() {
	app := createApp()

	err := app.Run(os.Args)
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}
}

func createApp() *cli.App {
	app := cli.NewApp()
	app.Name = "Zally"
	app.Version = "1.1"
	app.Usage = "Zally Command Line Interface"

	app.Commands = []cli.Command{
		SupportedRulesCommand,
	}

	app.Flags = []cli.Flag{
		cli.StringFlag{
			Name:   "linter-service, l",
			Usage:  "Linter service `URL`",
			Value:  "http://localhost:8080",
			EnvVar: "ZALLY_URL",
		},
	}

	return app
}

package main

import "github.com/urfave/cli"

// CreateApp creates CLI application with defined commands
func CreateApp() *cli.App {
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

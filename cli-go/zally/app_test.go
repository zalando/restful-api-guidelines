package main

import (
	"reflect"
	"testing"

	"os"

	"github.com/urfave/cli"
)

func TestApp(t *testing.T) {
	t.Run("rules_command_in_the_list", func(t *testing.T) {
		app := CreateApp()
		assertEquals(t, "rules", app.Commands[0].Name)
	})

	t.Run("has_linter_url_flag", func(t *testing.T) {
		app := CreateApp()
		assertEquals(t, "linter-service, l", app.Flags[0].GetName())
	})

	t.Run("default_linter_service_url", func(t *testing.T) {
		app := CreateApp()
		app.Before = func(c *cli.Context) error {
			assertEquals(t, "http://localhost:8080", c.String("linter-service"))
			return nil
		}
		app.Run([]string{""})
	})

	t.Run("linter_service_url_can_be_set_from_env", func(t *testing.T) {
		app := CreateApp()
		app.Before = func(c *cli.Context) error {
			assertEquals(t, "https://localhost:9090", c.String("linter-service"))
			return nil
		}

		os.Clearenv()
		os.Setenv("ZALLY_URL", "https://localhost:9090")

		app.Run([]string{""})

		os.Clearenv()
	})

	t.Run("linter_service_url_can_be_set_from_cli", func(t *testing.T) {
		app := CreateApp()
		app.Before = func(c *cli.Context) error {
			assertEquals(t, "https://localhost:9091", c.String("linter-service"))
			return nil
		}

		app.Run([]string{"", "-l", "https://localhost:9091"})
		app.Run([]string{"", "--linter-service", "https://localhost:9091"})
	})

	t.Run("has_token_flag", func(t *testing.T) {
		app := CreateApp()
		assertEquals(t, "token, t", app.Flags[1].GetName())
	})

	t.Run("token_can_be_set_from_env", func(t *testing.T) {
		app := CreateApp()
		app.Before = func(c *cli.Context) error {
			assertEquals(t, "some-oauth2-token", c.String("token"))
			return nil
		}

		os.Clearenv()
		os.Setenv("TOKEN", "some-oauth2-token")

		app.Run([]string{""})

		os.Clearenv()
	})

	t.Run("token_can_be_set_from_cli", func(t *testing.T) {
		app := CreateApp()
		app.Before = func(c *cli.Context) error {
			assertEquals(t, "some-oauth2-token", c.String("token"))
			return nil
		}

		app.Run([]string{"", "-t", "some-oauth2-token"})
		app.Run([]string{"", "--token", "some-oauth2-token"})
	})

}

func assertEquals(t *testing.T, expected interface{}, actual interface{}) {
	if !reflect.DeepEqual(expected, actual) {
		t.Errorf("Expected %#v, Actual %#v", expected, actual)
	}
}

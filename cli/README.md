# Zally Command Line Interface

This is Zally's cli tool: it reads a swagger file locally and lints it by
requesting violations check at a given Zally server.


## Building from sources

1. Follow [Go installation instructions](https://golang.org/doc/install)

1. Make sure that `$GOPATH` and `$GOROOT` variables are set

1. Clone the repository:

    ```bash
    git clone git@github.com:zalando-incubator/zally.git $GOPATH/github.com/zalando-incubator/zally
    ``` 
1. Get dependencies:

    ```bash
    cd $GOPATH/github.com/zalando-incubator/zally/cli/zally
    go get -t -v
    ```

1. Run tests:

    ```bash
    cd $GOPATH/github.com/zalando-incubator/zally/cli/zally
    go test -v ./...
    ```

1. Build the binary:

    ```bash
    cd $GOPATH/github.com/zalando-incubator/zally/cli/zally
    go build
    ```


## Usage

### Basic example

To launch `zally-cli`, run the following command:

```bash
zally lint swagger_definition.yml
```

By default `zally-cli` uses API located at http://localhost:8080/ and no security
token. You can provide alternative settings using either environment variables
`ZALLY_URL` and `TOKEN`:

```bash
export ZALLY_URL="http://zally.example.com/"
export TOKEN="f123-4567-890a-bcde"
zally lint swagger_definition.yml
```

or `--linter-service` and `--token` command-line arguments:

```bash
zally swagger_definition.yml --linter-service "http://zally.example.com/" --token "f123-4567-890a-bcde"
```

### Getting help message

To get commands help message, simply type:

```bash
zally --help
```

### Getting the list of rules

To get the list of enabled rules, please run:

```bash
zally rules
```


## Release it

1. Install `goreleaser` tool:

    ```bash
    go get -v github.com/goreleaser/goreleaser
    cd $GOPATH/src/github.com/goreleaser/goreleaser
    go install
    ```

    Alternatively you can download a latest release from [goreleaser Releases Page](https://github.com/goreleaser/goreleaser/releases)

1. Clean up folder `cli/zally/dist` if exists

1. Make sure that the repository state is clean:

    ```bash
    git status
    ```

1. Tag the release:

    ```bash
    git tag v1.1.0
    ```

1. Run `goreleaser`:

    ```bash
    cd cli/zally
    goreleaser --skip-publish v1.1.0
    ```

1. Check builds inside `cli/zally/dist` directory.

1. Publish release tag to GitHub:

    ```bash
    git push origin v1.1.0
    ```

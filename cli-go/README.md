# Zally Command Line Interface (Golang)

This is Zally's cli tool: it reads a swagger file locally and lints it by
requesting violations check at a given Zally server.

## Current status

Not feature-complete, in active development

## Building from sources

1. Follow [Go installation instructions](https://golang.org/doc/install)

1. Make sure that `$GOPATH` and `$GOROOT` variables are set

1. Clone the repository:

    ```bash
    git clone git@github.com:zalando-incubator/zally.git $GOPATH/github.com/zalando-incubator/zally
    ``` 

1. Run tests:

    ```bash
    cd $GOPATH/github.com/zalando-incubator/zally/cli-go/zally
    go test -v
    ```

1. Build the binary:

    ```bash
    cd $GOPATH/github.com/zalando-incubator/zally/cli-go/zally
    go build
    ```

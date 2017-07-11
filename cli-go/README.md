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
    go test -v ./...
    ```

1. Build the binary:

    ```bash
    cd $GOPATH/github.com/zalando-incubator/zally/cli-go/zally
    go build
    ```

## Release it

1. Install `goreleaser` tool:

    ```bash
    go get -v github.com/goreleaser/goreleaser
    cd $GOPATH/src/github.com/goreleaser/goreleaser
    go install
    ```

    Alternatively you can download a latest release from [goreleaser Releases Page](https://github.com/goreleaser/goreleaser/releases)

1. Clean up folder `cli-go/zally/dist` if exists

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
    cd cli-go/zally
    goreleaser --skip-publish v1.1.0
    ```

1. Check builds inside `cli-go/zally/dist` directory.

1. Publish release tag to GitHub:

    ```bash
    git push origin v1.1.0
    ```

# Zally Command Line Interface

This is Zally's cli tool: it reads a swagger file locally and lints it by
requesting violations check at a given Zally server.

## Building from Sources

1. Clone Zally repository
    ```bash
    git clone git@github.com:zalando-incubator/zally.git zally
    ```

2. Switch to `cli` folder:
	```bash
	cd zally/cli
	```

3. Build JAR package:
	```bash
	./gradlew clean build
	```

4. Check that `zally` command is working
	```bash
	./bin/zally
	```

5. Add `./bin` directory to `PATH`


## Installing from Binaries

1. Download latest zally-cli JAR file from the [releases page](https://github.com/zalando-incubator/zally/releases)

2. Create a `zally` start script in your `/usr/local/bin`:

    ```bash
    #!/usr/bin/env bash
    function zally {
        java -Done-jar.silent=true -jar /PATH/TO/zally-1.0.0.jar "$@"
    }
    zally $@
    ```

## Usage

### Basic example

To launch `zally-cli`, run the following command:

```bash
zally swagger_definition.yml
```

By default `zally-cli` uses API located at http://localhost:8080/ and no security
token. You can provide alternative settings using either environment variables
`ZALLY_URL` and `TOKEN`:

```bash
export ZALLY_URL="http://zally.example.com/"
export TOKEN="f123-4567-890a-bcde"
zally swagger_definition.yml
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

## Building next release

1. Make sure that the current state of the Git repository is clean:
    
    ```bash
    git stash -a
    ```

2. Create a release:

    ```bash
    ./gradlew clean build -Pversion=1.0
    ```

3. Unstash the changes:

    ```bash
    git stash pop
    ```

4. You can find the release JAR file in `releases` folder.


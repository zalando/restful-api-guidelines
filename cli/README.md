# Zally Command Line Interface

This is Zally's cli tool: it reads a swagger file locally and lints it by
requesting violations check at a given Zally server.

## Installation

1. Clone Zally repository

2. Switch to `cli` folder:
	```bash
	cd cli
	```

3. Build `zally.jar` package:
	```bash
	./gradlew build
	```

4. Run it using
	```bash
	./bin/zally
	```

## Usage

### Basic example

To launch `zally-cli`, run the following command:

```bash
./bin/zally swagger_definition.yml
```

By default `zally-cli` uses API located at http://localhost:8080/ and no security
token. You can provide alternative settings using either environment variables
`ZALLY_URL` and `TOKEN`:

```bash
export ZALLY_URL="http://zally.example.com/"
export TOKEN="f123-4567-890a-bcde"
./bin/zally swagger_definition.yml
```

or `--linter-service` and `--token` command-line arguments:

```bash
./bin/zally swagger_definition.yml --linter-service "http://zally.example.com/" --token "f123-4567-890a-bcde"
```

### Getting help message

To get commands help message, simply type:

```bash
./bin/zally --help
```

### Building release

To build a release:

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


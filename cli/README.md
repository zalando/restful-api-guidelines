# Zally Command Line Interface

## Installation

1. Clone Zally repository

2. Switch to `cli` folder:
	```bash
	cd cli
	```

3. Make sure that Gradle is installed

4. Build `zally.jar` package:
	```bash
	gradle bin
	```

5. Run it using
	```bash
	./bin/zally
	```

## Usage

### Basic example

To launch `zally`, run the following command:

```bash
./bin/zally swagger_definition.yml
```

By default `zally` uses API located at http://localhost:8080/ and no security
token. You can provide alternative settings using either environment variables
`ZALLY_URL` and `TOKEN`:

```bash
export ZALLY_URL="http://zally.example.com/"
export TOKEN="f123-4567-890a-bcde"
./bin/zally swagger_definition.yml
```

or `--url` and `--token` command-line arguments:

```bash
./bin/zally swagger_definition.yml --url "http://zally.example.com/" --token "f123-4567-890a-bcde"
```

### Getting help message

To get commands help message, simply type:

```bash
./bin/zally --help
```

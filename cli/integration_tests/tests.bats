#!/usr/bin/env bats

@test "CLI requires filename or URL to be specified" {
	run cli/bin/zally
	[ "$status" -eq 1 ]
	[[ "$output" =~ "Please provide a swagger file path or URL" ]]
	[[ "$output" =~ "Validates the given swagger specification using Zally service" ]]
}

@test "CLI works with local .yaml file" {
	run cli/bin/zally -l http://localhost:8080 server/src/test/resources/fixtures/api_spa.yaml
	[ "$status" -eq 1 ]
	[[ "$output" =~ "[0mMUST violations: 6" ]]
	[[ "$output" =~ "SHOULD violations: 2" ]]
	[[ "$output" =~ "MAY violations: 0" ]]
	[[ "$output" =~ "HINT violations: 1" ]]
}

@test "CLI works with local .json file" {
	run cli/bin/zally -l http://localhost:8080 server/src/test/resources/fixtures/api_spp.json
	[ "$status" -eq 1 ]
	[[ "$output" =~ "[0mMUST violations: 2" ]]
	[[ "$output" =~ "SHOULD violations: 2" ]]
	[[ "$output" =~ "MAY violations: 1" ]]
	[[ "$output" =~ "HINT violations: 1" ]]
}

@test "CLI works with local .json file" {
	run cli/bin/zally -l http://localhost:8080 server/src/test/resources/fixtures/api_tinbox.yaml
	[ "$status" -eq 1 ]
	[[ "$output" =~ "OpenAPI 2.0 schema" ]]
	[[ "$output" =~ "[0mMUST violations: 8" ]]
	[[ "$output" =~ "SHOULD violations: 3" ]]
	[[ "$output" =~ "MAY violations: 0" ]]
	[[ "$output" =~ "HINT violations: 1" ]]
}

@test "CLI works with remote .yaml file" {
	run cli/bin/zally -l http://localhost:8080 https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spa.yaml
	[ "$status" -eq 1 ]
	[[ "$output" =~ "[0mMUST violations: 6" ]]
	[[ "$output" =~ "SHOULD violations: 2" ]]
	[[ "$output" =~ "MAY violations: 0" ]]
	[[ "$output" =~ "HINT violations: 1" ]]
}

@test "CLI works with remote .json file" {
	run cli/bin/zally -l http://localhost:8080 https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json
	[ "$status" -eq 1 ]
	[[ "$output" =~ "[0mMUST violations: 2" ]]
	[[ "$output" =~ "SHOULD violations: 2" ]]
	[[ "$output" =~ "MAY violations: 1" ]]
	[[ "$output" =~ "HINT violations: 1" ]]
}

@test "Displays rule list" {
	run cli/bin/zally -l http://localhost:8080 -r
	[ "$status" -eq 0 ]
	[[ "$output" =~ "Supported Rules" ]]
	[[ "$output" =~ "===============" ]]
	[[ "$output" =~ "[0m[31mS001[0m SHOULD Avoid reserved Javascript keywords" ]]
	[[ "$output" =~ "[32mM001[0m MUST Avoid Link in Header Rule" ]]
}

@test "CLI 1.0 works with recent API" {
	run curl -sL https://github.com/zalando-incubator/zally/releases/download/v1.0.0/zally-1.0.0.jar -o zally-1.0.0.jar
	run java -jar zally-1.0.0.jar -l http://localhost:8080 https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json
	[ "$status" -eq 1 ]
	[[ "$output" =~ "[0mMUST violations: 2" ]]
	[[ "$output" =~ "SHOULD violations: 2" ]]
	[[ "$output" =~ "COULD violations: 0" ]]
	[[ "$output" =~ "HINT violations: 1" ]]
	run rm zally-1.0.0.jar
}

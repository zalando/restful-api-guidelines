#!/usr/bin/env bats

@test "CLI requires filename or URL to be specified" {
	run cli/bin/zally
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "Please provide a swagger file path or URL" ]
	[ "${lines[1]}" = "Validates the given swagger specification using Zally service" ]
}

@test "CLI works with local .yaml file" {
	run cli/bin/zally -l http://localhost:8080 server/src/test/resources/fixtures/api_spa.yaml
	[ "$status" -eq 1 ]
	[ "${lines[67]}" = "[0mMUST violations: 6" ]
	[ "${lines[68]}" = "SHOULD violations: 2" ]
	[ "${lines[69]}" = "MAY violations: 0" ]
	[ "${lines[70]}" = "HINT violations: 1" ]
	[ "${#lines[@]}" -eq 71 ]
}

@test "CLI works with local .json file" {
	run cli/bin/zally -l http://localhost:8080 server/src/test/resources/fixtures/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[78]}" = "[0mMUST violations: 2" ]
	[ "${lines[79]}" = "SHOULD violations: 2" ]
	[ "${lines[80]}" = "MAY violations: 1" ]
	[ "${lines[81]}" = "HINT violations: 1" ]
	[ "${#lines[@]}" -eq 82 ]
}

@test "CLI works with remote .yaml file" {
	run cli/bin/zally -l http://localhost:8080 https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spa.yaml
	[ "$status" -eq 1 ]
	[ "${lines[67]}" = "[0mMUST violations: 6" ]
	[ "${lines[68]}" = "SHOULD violations: 2" ]
	[ "${lines[69]}" = "MAY violations: 0" ]
	[ "${lines[70]}" = "HINT violations: 1" ]
	[ "${#lines[@]}" -eq 71 ]
}

@test "CLI works with remote .json file" {
	run cli/bin/zally -l http://localhost:8080 https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[78]}" = "[0mMUST violations: 2" ]
	[ "${lines[79]}" = "SHOULD violations: 2" ]
	[ "${lines[80]}" = "MAY violations: 1" ]
	[ "${lines[81]}" = "HINT violations: 1" ]
	[ "${#lines[@]}" -eq 82 ]
}

@test "Displays rule list" {
	run cli/bin/zally -l http://localhost:8080 -r
	[ "$status" -eq 0 ]
	[ "${lines[1]}" = "Supported Rules" ]
	[ "${lines[2]}" = "===============" ]
	[ "${lines[3]}" = "[0m[31mS001[0m SHOULD Avoid reserved Javascript keywords" ]
	[ "${lines[5]}" = "[32mM001[0m MUST Avoid Link in Header Rule" ]
}

@test "CLI 1.0 works with recent API" {
	run curl -sL https://github.com/zalando-incubator/zally/releases/download/v1.0.0/zally-1.0.0.jar -o zally-1.0.0.jar
	run java -jar zally-1.0.0.jar -l http://localhost:8080 https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[70]}" = "[0mMUST violations: 2" ]
	[ "${lines[71]}" = "SHOULD violations: 2" ]
	[ "${lines[72]}" = "COULD violations: 0" ]
	[ "${lines[73]}" = "HINT violations: 1" ]
	[ "${#lines[@]}" -eq 74 ]
	run rm zally-1.0.0.jar
}

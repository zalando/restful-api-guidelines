#!/usr/bin/env bats

@test "CLI requires filename or URL to be specified" {
	run cli/bin/zally
	[ "$status" -eq 1 ]
	[ "${lines[0]}" = "Please provide a swagger file path or URL" ]
	[ "${lines[1]}" = "Validates the given swagger specification using Zally service" ]
}

@test "CLI works with local .yaml file" {
	run cli/bin/zally server/src/test/resources/fixtures/api_spa.yaml
	[ "$status" -eq 1 ]
	[ "${lines[44]}" = "[0mMUST violations: 6" ]
	[ "${lines[45]}" = "SHOULD violations: 0" ]
	[ "${lines[46]}" = "MAY violations: 0" ]
	[ "${lines[47]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 48 ]
}

@test "CLI works with local .json file" {
	run cli/bin/zally server/src/test/resources/fixtures/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[60]}" = "[0mMUST violations: 2" ]
	[ "${lines[61]}" = "SHOULD violations: 1" ]
	[ "${lines[62]}" = "MAY violations: 1" ]
	[ "${lines[63]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 64 ]
}

@test "CLI works with remote .yaml file" {
	run cli/bin/zally https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spa.yaml
	[ "$status" -eq 1 ]
	[ "${lines[44]}" = "[0mMUST violations: 6" ]
	[ "${lines[45]}" = "SHOULD violations: 0" ]
	[ "${lines[46]}" = "MAY violations: 0" ]
	[ "${lines[47]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 48 ]
}

@test "CLI works with remote .json file" {
	run cli/bin/zally https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[60]}" = "[0mMUST violations: 2" ]
	[ "${lines[61]}" = "SHOULD violations: 1" ]
	[ "${lines[62]}" = "MAY violations: 1" ]
	[ "${lines[63]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 64 ]
}

@test "Displays rule list" {
	run cli/bin/zally -r
	[ "$status" -eq 0 ]
	[ "${lines[1]}" = "Supported Rules" ]
	[ "${lines[2]}" = "===============" ]
	[ "${lines[3]}" = "[0m[31mS001[0m SHOULD Avoid reserved Javascript keywords" ]
	[ "${lines[5]}" = "[32mM001[0m MUST Avoid Link in Header Rule" ]
}

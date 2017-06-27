#!/usr/bin/env bats

@test "CLI displays usage when no parameters specified" {
	run cli-go/zally/zally
	[ "$status" -eq 0 ]
	[ "${lines[2]}" = "USAGE:" ]
	[ "${lines[3]}" = "   zally [global options] command [command options] [arguments...]" ]
}

@test "CLI works with local .yaml file" {
	run cli-go/zally/zally lint server/src/test/resources/fixtures/api_spa.yaml
	[ "$status" -eq 1 ]
	[ "${lines[42]}" = "MUST violations: 6" ]
	[ "${lines[43]}" = "SHOULD violations: 1" ]
	[ "${lines[44]}" = "MAY violations: 0" ]
	[ "${lines[45]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 47 ]
}

@test "CLI works with local .json file" {
	run cli-go/zally/zally lint server/src/test/resources/fixtures/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[59]}" = "MUST violations: 2" ]
	[ "${lines[60]}" = "SHOULD violations: 2" ]
	[ "${lines[61]}" = "MAY violations: 1" ]
	[ "${lines[62]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 64 ]
}

@test "CLI works with remote .yaml file" {
	run cli-go/zally/zally lint https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spa.yaml
	[ "$status" -eq 1 ]
	[ "${lines[42]}" = "MUST violations: 6" ]
	[ "${lines[43]}" = "SHOULD violations: 1" ]
	[ "${lines[44]}" = "MAY violations: 0" ]
	[ "${lines[45]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 47 ]
}

@test "CLI works with remote .json file" {
	run cli-go/zally/zally lint https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json
	[ "$status" -eq 1 ]
	[ "${lines[59]}" = "MUST violations: 2" ]
	[ "${lines[60]}" = "SHOULD violations: 2" ]
	[ "${lines[61]}" = "MAY violations: 1" ]
	[ "${lines[62]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 64 ]
}

@test "CLI should fail when any MUST violations" {
	run cli-go/zally/zally lint server/src/test/resources/fixtures/api_spp.json
	[ "${lines[63]}" = "Failing because: 2 must violation(s) found" ]
	[ "$status" -eq 1 ]
}

@test "CLI should not fail when no MUST violations" {
	run cli-go/zally/zally lint server/src/test/resources/fixtures/no_must_violations.yaml
	[ "$status" -eq 0 ]
}

@test "Displays rule list" {
	run cli-go/zally/zally rules
	[ "$status" -eq 0 ]
	[ "${lines[0]}" = "[33mS001[0m [33mSHOULD[0m: Avoid reserved Javascript keywords" ]
	[ "${lines[1]}" = "	http://zalando.github.io/restful-api-guidelines/naming/Naming.html#should-reserved-javascript-keywords-should-be-avoided" ]
}

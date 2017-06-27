#!/usr/bin/env bats

@test "CLI displays usage when no parameters specified" {
	run cli-go/zally/zally
	[ "$status" -eq 0 ]
	[ "${lines[2]}" = "USAGE:" ]
	[ "${lines[3]}" = "   zally [global options] command [command options] [arguments...]" ]
}

@test "CLI works with local .yaml file" {
	run cli-go/zally/zally lint server/src/test/resources/fixtures/api_spa.yaml
	[ "$status" -eq 0 ]
	[ "${lines[42]}" = "MUST violations: 6" ]
	[ "${lines[43]}" = "SHOULD violations: 1" ]
	[ "${lines[44]}" = "MAY violations: 0" ]
	[ "${lines[45]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 46 ]
}

@test "CLI works with local .json file" {
	run cli-go/zally/zally lint server/src/test/resources/fixtures/api_spp.json
	[ "$status" -eq 0 ]
	[ "${lines[59]}" = "MUST violations: 2" ]
	[ "${lines[60]}" = "SHOULD violations: 2" ]
	[ "${lines[61]}" = "MAY violations: 1" ]
	[ "${lines[62]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 63 ]
}

@test "CLI works with local .yaml file" {
	run cli-go/zally/zally lint https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spa.yaml
	[ "$status" -eq 0 ]
	[ "${lines[42]}" = "MUST violations: 6" ]
	[ "${lines[43]}" = "SHOULD violations: 1" ]
	[ "${lines[44]}" = "MAY violations: 0" ]
	[ "${lines[45]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 46 ]
}

@test "CLI works with local .json file" {
	run cli-go/zally/zally lint https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json
	[ "$status" -eq 0 ]
	[ "${lines[59]}" = "MUST violations: 2" ]
	[ "${lines[60]}" = "SHOULD violations: 2" ]
	[ "${lines[61]}" = "MAY violations: 1" ]
	[ "${lines[62]}" = "HINT violations: 0" ]
	[ "${#lines[@]}" -eq 63 ]
}

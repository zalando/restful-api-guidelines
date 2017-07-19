#!/usr/bin/env bats

@test "CLI displays usage when no parameters specified" {
	run cli-go/zally/zally
	[ "$status" -eq 0 ]
	[[ "$output" =~ "USAGE:" ]]
	[[ "$output" =~ "   zally [global options] command [command options] [arguments...]" ]]
}

@test "CLI works with local .yaml file" {
	run cli-go/zally/zally lint server/src/test/resources/fixtures/api_spa.yaml
	[ "$status" -eq 1 ]
	[[ "$output" =~ "MUST violations: 6" ]]
	[[ "$output" =~ "SHOULD violations: 2" ]]
	[[ "$output" =~ "MAY violations: 0" ]]
	[[ "$output" =~ "HINT violations: 0" ]]
}

@test "CLI works with local .json file" {
	run cli-go/zally/zally lint server/src/test/resources/fixtures/api_spp.json
	[ "$status" -eq 1 ]
	[[ "$output" =~ "MUST violations: 2" ]]
	[[ "$output" =~ "SHOULD violations: 2" ]]
	[[ "$output" =~ "MAY violations: 1" ]]
	[[ "$output" =~ "HINT violations: 0" ]]
}

@test "CLI works with remote .yaml file" {
	run cli-go/zally/zally lint https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spa.yaml
	[ "$status" -eq 1 ]
	[[ "$output" =~ "MUST violations: 6" ]]
	[[ "$output" =~ "SHOULD violations: 2" ]]
	[[ "$output" =~ "MAY violations: 0" ]]
	[[ "$output" =~ "HINT violations: 0" ]]
}

@test "CLI works with remote .json file" {
	run cli-go/zally/zally lint https://raw.githubusercontent.com/zalando-incubator/zally/e542a2d6e8f7f37f4adf2242343e453961537a08/server/src/test/resources/api_spp.json
	[ "$status" -eq 1 ]
	[[ "$output" =~ "MUST violations: 2" ]]
	[[ "$output" =~ "SHOULD violations: 2" ]]
	[[ "$output" =~ "MAY violations: 1" ]]
	[[ "$output" =~ "HINT violations: 0" ]]
}

@test "CLI should fail when any MUST violations" {
	run cli-go/zally/zally lint server/src/test/resources/fixtures/api_spp.json
	[[ "$output" =~ "Failing because: 2 must violation(s) found" ]]
	[ "$status" -eq 1 ]
}

@test "CLI should not fail when no MUST violations" {
	run cli-go/zally/zally lint server/src/test/resources/fixtures/no_must_violations.yaml
	[ "$status" -eq 0 ]
}

@test "CLI should validate zally API definition" {
	run cli-go/zally/zally lint server/src/main/resources/api/zally-api.yaml
	[ "$status" -eq 0 ]
	[[ "$output" =~ "[33mSHOULD[0m [33mUse Specific HTTP Status Codes[0m" ]]
	[[ "$output" =~ "MUST violations: 0" ]]
	[[ "$output" =~ "SHOULD violations: 1" ]]
	[[ "$output" =~ "MAY violations: 0" ]]
	[[ "$output" =~ "HINT violations: 0" ]]
}

@test "Displays rule list" {
	run cli-go/zally/zally rules
	[ "$status" -eq 0 ]
	[[ "$output" =~ "[31mM001[0m [31mMUST[0m: Avoid Link in Header Rule" ]]
	[[ "$output" =~ "	http://zalando.github.io/restful-api-guidelines/hyper-media/Hypermedia.html#must-do-not-use-link-headers-with-json-entities" ]]
}
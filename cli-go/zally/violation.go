package main

// Violation keeps information about Zally violations
type Violation struct {
	Title         string   `json:"title"`
	Decription    string   `json:"description"`
	ViolationType string   `json:"violation_type"`
	RuleLink      string   `json:"rule_link"`
	Paths         []string `json:"paths"`
}

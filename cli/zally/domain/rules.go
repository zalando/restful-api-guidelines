package domain

import "strings"

// Rules stores list of rules
type Rules struct {
	Rules []Rule `json:"rules"`
}

// Must returns must rules
func (r *Rules) Must() []Rule {
	return r.filterRules("MUST")
}

// Should returns should rules
func (r *Rules) Should() []Rule {
	return r.filterRules("SHOULD")
}

// May returns may rules
func (r *Rules) May() []Rule {
	return r.filterRules("MAY")
}

// Hint returns hint rules
func (r *Rules) Hint() []Rule {
	return r.filterRules("HINT")
}

func (r *Rules) filterRules(ruleType string) []Rule {
	result := []Rule{}
	for _, rule := range r.Rules {
		if strings.ToUpper(rule.Type) == strings.ToUpper(ruleType) {
			result = append(result, rule)
		}
	}
	return result
}

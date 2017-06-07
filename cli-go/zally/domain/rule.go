package domain

// Rule keeps information about API rules
type Rule struct {
	Title    string `json:"title"`
	Code     string `json:"code"`
	Type     string `json:"type"`
	URL      string `json:"url"`
	IsActive bool   `json:"is_active"`
}

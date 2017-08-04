package domain

// ViolationsCount contains violation counters
type ViolationsCount struct {
	Must   int `json:"must"`
	Should int `json:"should"`
	May    int `json:"may"`
	Hint   int `json:"hint"`
}

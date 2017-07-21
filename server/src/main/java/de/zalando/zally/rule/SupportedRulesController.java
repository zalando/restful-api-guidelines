package de.zalando.zally.rule;

import de.zalando.zally.dto.RuleDTO;
import de.zalando.zally.dto.RulesListDTO;
import de.zalando.zally.dto.ViolationType;
import de.zalando.zally.dto.ViolationTypeBinder;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
public class SupportedRulesController {

    private final List<Rule> rules;
    private final RulesPolicy rulesPolicy;

    @Autowired
    public SupportedRulesController(List<Rule> rules, RulesPolicy rulesPolicy) {
        this.rules = rules;
        this.rulesPolicy = rulesPolicy;
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(ViolationType.class, new ViolationTypeBinder());
    }

    @ResponseBody
    @GetMapping("/supported-rules")
    public RulesListDTO listSupportedRules(
        @RequestParam(value = "type", required = false) ViolationType typeFilter,
        @RequestParam(value = "is_active", required = false) Boolean isActiveFilter) {

        List<RuleDTO> filteredRules = rules
            .stream()
            .filter(r -> filterByIsActive(r, isActiveFilter))
            .filter(r -> filterByType(r, typeFilter))
            .map(this::toDto)
            .collect(toList());

        return new RulesListDTO(filteredRules);
    }

    private boolean filterByIsActive(Rule rule, Boolean isActiveFilter) {
        boolean isActive = rulesPolicy.accepts(rule);
        return isActiveFilter == null || isActive == isActiveFilter;
    }

    private boolean filterByType(Rule rule, ViolationType typeFilter) {
        return typeFilter == null || rule.getViolationType().equals(typeFilter);
    }

    private RuleDTO toDto(Rule rule) {
        return new RuleDTO(rule, rulesPolicy.accepts(rule));
    }
}

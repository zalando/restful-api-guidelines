package de.zalando.zally.apireview;

import com.fasterxml.jackson.databind.JsonNode;
import de.zalando.zally.violation.Violation;
import de.zalando.zally.violation.ViolationType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class ApiReview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false)
    private String jsonPayload;

    private String apiDefinition;

    @Column(nullable = false)
    private boolean successfulProcessed;

    @Column(nullable = false)
    private LocalDate day;

    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentOffsetDateTime",
        parameters = {@Parameter(name = "javaZone", value = "UTC")})
    private OffsetDateTime created;

    private int numberOfEndpoints;
    private int mustViolations;
    private int shouldViolations;
    private int mayViolations;
    private int hintViolations;

    @OneToMany(mappedBy = "apiReview", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RuleViolation> ruleViolations;

    /**
     * for Hibernate
     */
    protected ApiReview() {
        super();
    }

    public ApiReview(JsonNode jsonPayload) {
        this(jsonPayload, null, Collections.emptyList());
    }

    public ApiReview(JsonNode jsonPayload, String apiDefinition, List<Violation> violations) {
        this.jsonPayload = jsonPayload.toString();
        this.apiDefinition = apiDefinition;
        this.successfulProcessed = StringUtils.isNotBlank(apiDefinition);
        this.created = Instant.now().atOffset(ZoneOffset.UTC);
        this.day = created.toLocalDate();

        this.name = ApiNameParser.extractApiName(apiDefinition);
        this.ruleViolations = violations.stream()
            .map(v -> new RuleViolation(this, v.getRule().getName(), v.getViolationType(), v.getPaths().size()))
            .collect(Collectors.toList());

        this.numberOfEndpoints = EndpointCounter.count(apiDefinition);
        this.mustViolations = (int) ruleViolations.stream().filter(r -> r.getType() == ViolationType.MUST).count();
        this.shouldViolations = (int) ruleViolations.stream().filter(r -> r.getType() == ViolationType.SHOULD).count();
        this.mayViolations = (int) ruleViolations.stream().filter(r -> r.getType() == ViolationType.MAY).count();
        this.hintViolations = (int) ruleViolations.stream().filter(r -> r.getType() == ViolationType.HINT).count();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonPayload() {
        return jsonPayload;
    }

    public void setJsonPayload(String jsonPayload) {
        this.jsonPayload = jsonPayload;
    }

    public String getApiDefinition() {
        return apiDefinition;
    }

    public void setApiDefinition(String apiDefinition) {
        this.apiDefinition = apiDefinition;
    }

    public boolean isSuccessfulProcessed() {
        return successfulProcessed;
    }

    public void setSuccessfulProcessed(boolean successfulProcessed) {
        this.successfulProcessed = successfulProcessed;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public int getNumberOfEndpoints() {
        return numberOfEndpoints;
    }

    public void setNumberOfEndpoints(int numberOfEndpoints) {
        this.numberOfEndpoints = numberOfEndpoints;
    }

    public int getMustViolations() {
        return mustViolations;
    }

    public void setMustViolations(int mustViolations) {
        this.mustViolations = mustViolations;
    }

    public int getShouldViolations() {
        return shouldViolations;
    }

    public void setShouldViolations(int shouldViolations) {
        this.shouldViolations = shouldViolations;
    }

    public int getMayViolations() {
        return mayViolations;
    }

    public void setMayViolations(int mayViolations) {
        this.mayViolations = mayViolations;
    }

    public int getHintViolations() {
        return hintViolations;
    }

    public void setHintViolations(int hintViolations) {
        this.hintViolations = hintViolations;
    }

    public List<RuleViolation> getRuleViolations() {
        return ruleViolations;
    }

    public void setRuleViolations(List<RuleViolation> ruleViolations) {
        this.ruleViolations = ruleViolations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiReview that = (ApiReview) o;
        return Objects.equals(id, that.id)
            && Objects.equals(name, that.name)
            && Objects.equals(jsonPayload, that.jsonPayload)
            && Objects.equals(apiDefinition, that.apiDefinition)
            && Objects.equals(successfulProcessed, that.successfulProcessed)
            && Objects.equals(day, that.day)
            && Objects.equals(created, that.created)
            && Objects.equals(numberOfEndpoints, that.numberOfEndpoints)
            && Objects.equals(mustViolations, that.mustViolations)
            && Objects.equals(shouldViolations, that.shouldViolations)
            && Objects.equals(mayViolations, that.mayViolations)
            && Objects.equals(hintViolations, that.hintViolations)
            && Objects.equals(ruleViolations, that.ruleViolations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, jsonPayload, apiDefinition, successfulProcessed,
            day, created, numberOfEndpoints, mustViolations, shouldViolations, mayViolations,
            hintViolations, ruleViolations);
    }
}

package de.zalando.zally;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Entity
public class ApiReviewRequest implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String jsonPayload;

    private String apiDefinition;

    @Column(nullable = false)
    private boolean successfulProcessed;

    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentOffsetDateTime",
        parameters = {@Parameter(name = "javaZone", value = "UTC")})
    private OffsetDateTime created;

    /**
     * for Hibernate
     */
    protected ApiReviewRequest() {
        super();
    }

    public ApiReviewRequest(JsonNode jsonPayload) {
        this(jsonPayload, null);
    }

    public ApiReviewRequest(JsonNode jsonPayload, String apiDefinition) {
        this.jsonPayload = jsonPayload.toString();
        this.apiDefinition = apiDefinition;
        this.successfulProcessed = StringUtils.isNotBlank(apiDefinition);
        this.created = Instant.now().atOffset(ZoneOffset.UTC);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiReviewRequest that = (ApiReviewRequest) o;
        return Objects.equals(id, that.id)
            && Objects.equals(jsonPayload, that.jsonPayload)
            && Objects.equals(apiDefinition, that.apiDefinition)
            && Objects.equals(successfulProcessed, that.successfulProcessed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jsonPayload, apiDefinition, successfulProcessed);
    }
}

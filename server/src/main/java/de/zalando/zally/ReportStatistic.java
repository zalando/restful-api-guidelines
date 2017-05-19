package de.zalando.zally;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;

@Entity
public class ReportStatistic implements Serializable {

    @Id
    private String name;

    @Column(nullable = false)
    private int value;

    @Column(nullable = false)
    private LocalDate day;

    protected ReportStatistic() {
    }

    public ReportStatistic(String name, int value) {
        this.name = name;
        this.value = value;
        day = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportStatistic that = (ReportStatistic) o;
        return Objects.equals(name, that.name)
            && Objects.equals(value, that.value)
            && Objects.equals(day, that.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, day);
    }
}

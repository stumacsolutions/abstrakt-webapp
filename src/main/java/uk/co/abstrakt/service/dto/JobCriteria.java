package uk.co.abstrakt.service.dto;

import java.io.Serializable;
import java.util.Objects;
import uk.co.abstrakt.domain.enumeration.Week;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Job entity. This class is used in JobResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /jobs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class JobCriteria implements Serializable {
    /**
     * Class for filtering Week
     */
    public static class WeekFilter extends Filter<Week> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private WeekFilter week;

    private BooleanFilter complete;

    private BooleanFilter paid;

    private LongFilter customerId;

    public JobCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public WeekFilter getWeek() {
        return week;
    }

    public void setWeek(WeekFilter week) {
        this.week = week;
    }

    public BooleanFilter getComplete() {
        return complete;
    }

    public void setComplete(BooleanFilter complete) {
        this.complete = complete;
    }

    public BooleanFilter getPaid() {
        return paid;
    }

    public void setPaid(BooleanFilter paid) {
        this.paid = paid;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JobCriteria that = (JobCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(week, that.week) &&
            Objects.equals(complete, that.complete) &&
            Objects.equals(paid, that.paid) &&
            Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        week,
        complete,
        paid,
        customerId
        );
    }

    @Override
    public String toString() {
        return "JobCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (week != null ? "week=" + week + ", " : "") +
                (complete != null ? "complete=" + complete + ", " : "") +
                (paid != null ? "paid=" + paid + ", " : "") +
                (customerId != null ? "customerId=" + customerId + ", " : "") +
            "}";
    }

}

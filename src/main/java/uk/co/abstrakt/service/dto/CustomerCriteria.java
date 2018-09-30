package uk.co.abstrakt.service.dto;

import java.io.Serializable;
import java.util.Objects;
import uk.co.abstrakt.domain.enumeration.Frequency;
import uk.co.abstrakt.domain.enumeration.PaymentMethod;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Customer entity. This class is used in CustomerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /customers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerCriteria implements Serializable {
    /**
     * Class for filtering Frequency
     */
    public static class FrequencyFilter extends Filter<Frequency> {
    }
    /**
     * Class for filtering PaymentMethod
     */
    public static class PaymentMethodFilter extends Filter<PaymentMethod> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter email;

    private StringFilter phone;

    private FrequencyFilter frequency;

    private PaymentMethodFilter paymentMethod;

    private StringFilter flatPosition;

    private StringFilter number;

    private StringFilter street;

    private LongFilter jobsId;

    private LongFilter areaId;

    public CustomerCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public FrequencyFilter getFrequency() {
        return frequency;
    }

    public void setFrequency(FrequencyFilter frequency) {
        this.frequency = frequency;
    }

    public PaymentMethodFilter getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public StringFilter getFlatPosition() {
        return flatPosition;
    }

    public void setFlatPosition(StringFilter flatPosition) {
        this.flatPosition = flatPosition;
    }

    public StringFilter getNumber() {
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public StringFilter getStreet() {
        return street;
    }

    public void setStreet(StringFilter street) {
        this.street = street;
    }

    public LongFilter getJobsId() {
        return jobsId;
    }

    public void setJobsId(LongFilter jobsId) {
        this.jobsId = jobsId;
    }

    public LongFilter getAreaId() {
        return areaId;
    }

    public void setAreaId(LongFilter areaId) {
        this.areaId = areaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerCriteria that = (CustomerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(frequency, that.frequency) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(flatPosition, that.flatPosition) &&
            Objects.equals(number, that.number) &&
            Objects.equals(street, that.street) &&
            Objects.equals(jobsId, that.jobsId) &&
            Objects.equals(areaId, that.areaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        email,
        phone,
        frequency,
        paymentMethod,
        flatPosition,
        number,
        street,
        jobsId,
        areaId
        );
    }

    @Override
    public String toString() {
        return "CustomerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (frequency != null ? "frequency=" + frequency + ", " : "") +
                (paymentMethod != null ? "paymentMethod=" + paymentMethod + ", " : "") +
                (flatPosition != null ? "flatPosition=" + flatPosition + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (street != null ? "street=" + street + ", " : "") +
                (jobsId != null ? "jobsId=" + jobsId + ", " : "") +
                (areaId != null ? "areaId=" + areaId + ", " : "") +
            "}";
    }

}

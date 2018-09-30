package uk.co.abstrakt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import uk.co.abstrakt.domain.enumeration.Frequency;

import uk.co.abstrakt.domain.enumeration.PaymentMethod;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    private Frequency frequency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "flat_position")
    private String flatPosition;

    @NotNull
    @Column(name = "jhi_number", nullable = false)
    private String number;

    @NotNull
    @Column(name = "street", nullable = false)
    private String street;

    @OneToMany(mappedBy = "customer")
    private Set<Job> jobs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("customers")
    private Area area;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Customer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Customer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Customer phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public Customer frequency(Frequency frequency) {
        this.frequency = frequency;
        return this;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Customer paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getFlatPosition() {
        return flatPosition;
    }

    public Customer flatPosition(String flatPosition) {
        this.flatPosition = flatPosition;
        return this;
    }

    public void setFlatPosition(String flatPosition) {
        this.flatPosition = flatPosition;
    }

    public String getNumber() {
        return number;
    }

    public Customer number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public Customer street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public Customer jobs(Set<Job> jobs) {
        this.jobs = jobs;
        return this;
    }

    public Customer addJobs(Job job) {
        this.jobs.add(job);
        job.setCustomer(this);
        return this;
    }

    public Customer removeJobs(Job job) {
        this.jobs.remove(job);
        job.setCustomer(null);
        return this;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    public Area getArea() {
        return area;
    }

    public Customer area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", flatPosition='" + getFlatPosition() + "'" +
            ", number='" + getNumber() + "'" +
            ", street='" + getStreet() + "'" +
            "}";
    }
}

package com.kone.emailservice.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A SubscriptionInfo.
 */
@Entity
@Table(name = "subscription_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SubscriptionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subscription_status")
    private Boolean subscriptionStatus = true;

    @Lob
    @Column(name = "token")
    private byte[] token;

    @Column(name = "token_content_type")
    private String tokenContentType;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate = LocalDate.now();

    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDate;

    @ManyToOne
    @JsonIgnoreProperties("subscriptionInfos")
    private OrgUserEmailInfo orguseremailinfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSubscriptionStatus() {
        return subscriptionStatus;
    }

    public SubscriptionInfo subscriptionStatus(Boolean subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
        return this;
    }

    public void setSubscriptionStatus(Boolean subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public byte[] getToken() {
        return token;
    }

    public SubscriptionInfo token(byte[] token) {
        this.token = token;
        return this;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }

    public String getTokenContentType() {
        return tokenContentType;
    }

    public SubscriptionInfo tokenContentType(String tokenContentType) {
        this.tokenContentType = tokenContentType;
        return this;
    }

    public void setTokenContentType(String tokenContentType) {
        this.tokenContentType = tokenContentType;
    }

    public String getReason() {
        return reason;
    }

    public SubscriptionInfo reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public SubscriptionInfo createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public SubscriptionInfo lastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public OrgUserEmailInfo getOrguseremailinfo() {
        return orguseremailinfo;
    }

    public SubscriptionInfo orguseremailinfo(OrgUserEmailInfo orgUserEmailInfo) {
        this.orguseremailinfo = orgUserEmailInfo;
        return this;
    }

    public void setOrguseremailinfo(OrgUserEmailInfo orgUserEmailInfo) {
        this.orguseremailinfo = orgUserEmailInfo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionInfo)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SubscriptionInfo{" +
            "id=" + getId() +
            ", subscriptionStatus='" + isSubscriptionStatus() + "'" +
            ", token='" + getToken() + "'" +
            ", tokenContentType='" + getTokenContentType() + "'" +
            ", reason='" + getReason() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}

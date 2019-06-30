package com.kone.emailservice.domain;
import io.swagger.annotations.ApiModel;
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
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "org_user_email_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrgUserEmailInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "org_member_email_id")
    private String orgMemberEmailId;

    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate = LocalDate.now();

    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public OrgUserEmailInfo organizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrgMemberEmailId() {
        return orgMemberEmailId;
    }

    public OrgUserEmailInfo orgMemberEmailId(String orgMemberEmailId) {
        this.orgMemberEmailId = orgMemberEmailId;
        return this;
    }

    public void setOrgMemberEmailId(String orgMemberEmailId) {
        this.orgMemberEmailId = orgMemberEmailId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public OrgUserEmailInfo createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public OrgUserEmailInfo lastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrgUserEmailInfo)) {
            return false;
        }
        return id != null && id.equals(((OrgUserEmailInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrgUserEmailInfo{" +
            "id=" + getId() +
            ", organizationName='" + getOrganizationName() + "'" +
            ", orgMemberEmailId='" + getOrgMemberEmailId() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}

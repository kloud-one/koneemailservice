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
 * A UserEmailLog.
 */
@Entity
@Table(name = "user_email_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserEmailLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mail_send_status")
    private Boolean mailSendStatus;

    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate = LocalDate.now();

    @Column(name = "subject")
    private String subject;

    @Column(name = "msg_content")
    private String msgContent;

    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDate;

    @ManyToOne
    @JsonIgnoreProperties("userEmailLogs")
    private OrgUserEmailInfo orguseremailinfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isMailSendStatus() {
        return mailSendStatus;
    }

    public UserEmailLog mailSendStatus(Boolean mailSendStatus) {
        this.mailSendStatus = mailSendStatus;
        return this;
    }

    public void setMailSendStatus(Boolean mailSendStatus) {
        this.mailSendStatus = mailSendStatus;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public UserEmailLog createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getSubject() {
        return subject;
    }

    public UserEmailLog subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public UserEmailLog msgContent(String msgContent) {
        this.msgContent = msgContent;
        return this;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public UserEmailLog lastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public OrgUserEmailInfo getOrguseremailinfo() {
        return orguseremailinfo;
    }

    public UserEmailLog orguseremailinfo(OrgUserEmailInfo orgUserEmailInfo) {
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
        if (!(o instanceof UserEmailLog)) {
            return false;
        }
        return id != null && id.equals(((UserEmailLog) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserEmailLog{" +
            "id=" + getId() +
            ", mailSendStatus='" + isMailSendStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", subject='" + getSubject() + "'" +
            ", msgContent='" + getMsgContent() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}

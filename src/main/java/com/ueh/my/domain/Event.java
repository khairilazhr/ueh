package com.ueh.my.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Lob
    @Column(name = "event_desc", nullable = false)
    private String eventDesc;

    @NotNull
    @Column(name = "event_org", nullable = false)
    private String eventOrg;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private ZonedDateTime eventDate;

    @Column(name = "event_location")
    private String eventLocation;

    @Lob
    @Column(name = "event_poster")
    private byte[] eventPoster;

    @Column(name = "event_poster_content_type")
    private String eventPosterContentType;

    @Column(name = "entered_by")
    private String enteredBy;

    @Column(name = "entered_date")
    private ZonedDateTime enteredDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @Column(name = "event_status")
    private String eventStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Event id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return this.eventName;
    }

    public Event eventName(String eventName) {
        this.setEventName(eventName);
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDesc() {
        return this.eventDesc;
    }

    public Event eventDesc(String eventDesc) {
        this.setEventDesc(eventDesc);
        return this;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventOrg() {
        return this.eventOrg;
    }

    public Event eventOrg(String eventOrg) {
        this.setEventOrg(eventOrg);
        return this;
    }

    public void setEventOrg(String eventOrg) {
        this.eventOrg = eventOrg;
    }

    public ZonedDateTime getEventDate() {
        return this.eventDate;
    }

    public Event eventDate(ZonedDateTime eventDate) {
        this.setEventDate(eventDate);
        return this;
    }

    public void setEventDate(ZonedDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return this.eventLocation;
    }

    public Event eventLocation(String eventLocation) {
        this.setEventLocation(eventLocation);
        return this;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public byte[] getEventPoster() {
        return this.eventPoster;
    }

    public Event eventPoster(byte[] eventPoster) {
        this.setEventPoster(eventPoster);
        return this;
    }

    public void setEventPoster(byte[] eventPoster) {
        this.eventPoster = eventPoster;
    }

    public String getEventPosterContentType() {
        return this.eventPosterContentType;
    }

    public Event eventPosterContentType(String eventPosterContentType) {
        this.eventPosterContentType = eventPosterContentType;
        return this;
    }

    public void setEventPosterContentType(String eventPosterContentType) {
        this.eventPosterContentType = eventPosterContentType;
    }

    public String getEnteredBy() {
        return this.enteredBy;
    }

    public Event enteredBy(String enteredBy) {
        this.setEnteredBy(enteredBy);
        return this;
    }

    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    public ZonedDateTime getEnteredDate() {
        return this.enteredDate;
    }

    public Event enteredDate(ZonedDateTime enteredDate) {
        this.setEnteredDate(enteredDate);
        return this;
    }

    public void setEnteredDate(ZonedDateTime enteredDate) {
        this.enteredDate = enteredDate;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public Event modifiedBy(String modifiedBy) {
        this.setModifiedBy(modifiedBy);
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public Event modifiedDate(ZonedDateTime modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getEventStatus() {
        return this.eventStatus;
    }

    public Event eventStatus(String eventStatus) {
        this.setEventStatus(eventStatus);
        return this;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return getId() != null && getId().equals(((Event) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventDesc='" + getEventDesc() + "'" +
            ", eventOrg='" + getEventOrg() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", eventLocation='" + getEventLocation() + "'" +
            ", eventPoster='" + getEventPoster() + "'" +
            ", eventPosterContentType='" + getEventPosterContentType() + "'" +
            ", enteredBy='" + getEnteredBy() + "'" +
            ", enteredDate='" + getEnteredDate() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", eventStatus='" + getEventStatus() + "'" +
            "}";
    }
}

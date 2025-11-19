package com.ueh.my.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.ueh.my.domain.Event} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventDTO implements Serializable {

    private Long id;

    @NotNull
    private String eventName;

    @Lob
    private String eventDesc;

    @NotNull
    private String eventOrg;

    @NotNull
    private ZonedDateTime eventDate;

    private String eventLocation;

    @Lob
    private byte[] eventPoster;

    private String eventPosterContentType;

    private String enteredBy;

    private ZonedDateTime enteredDate;

    private String modifiedBy;

    private ZonedDateTime modifiedDate;

    private String eventStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventOrg() {
        return eventOrg;
    }

    public void setEventOrg(String eventOrg) {
        this.eventOrg = eventOrg;
    }

    public ZonedDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(ZonedDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public byte[] getEventPoster() {
        return eventPoster;
    }

    public void setEventPoster(byte[] eventPoster) {
        this.eventPoster = eventPoster;
    }

    public String getEventPosterContentType() {
        return eventPosterContentType;
    }

    public void setEventPosterContentType(String eventPosterContentType) {
        this.eventPosterContentType = eventPosterContentType;
    }

    public String getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(String enteredBy) {
        this.enteredBy = enteredBy;
    }

    public ZonedDateTime getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(ZonedDateTime enteredDate) {
        this.enteredDate = enteredDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventDTO)) {
            return false;
        }

        EventDTO eventDTO = (EventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventDesc='" + getEventDesc() + "'" +
            ", eventOrg='" + getEventOrg() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", eventLocation='" + getEventLocation() + "'" +
            ", eventPoster='" + getEventPoster() + "'" +
            ", enteredBy='" + getEnteredBy() + "'" +
            ", enteredDate='" + getEnteredDate() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", eventStatus='" + getEventStatus() + "'" +
            "}";
    }
}

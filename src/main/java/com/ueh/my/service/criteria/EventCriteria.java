package com.ueh.my.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ueh.my.domain.Event} entity. This class is used
 * in {@link com.ueh.my.web.rest.EventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventName;

    private StringFilter eventOrg;

    private ZonedDateTimeFilter eventDate;

    private StringFilter eventLocation;

    private StringFilter enteredBy;

    private ZonedDateTimeFilter enteredDate;

    private StringFilter modifiedBy;

    private ZonedDateTimeFilter modifiedDate;

    private StringFilter eventStatus;

    private Boolean distinct;

    public EventCriteria() {}

    public EventCriteria(EventCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.eventName = other.optionalEventName().map(StringFilter::copy).orElse(null);
        this.eventOrg = other.optionalEventOrg().map(StringFilter::copy).orElse(null);
        this.eventDate = other.optionalEventDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.eventLocation = other.optionalEventLocation().map(StringFilter::copy).orElse(null);
        this.enteredBy = other.optionalEnteredBy().map(StringFilter::copy).orElse(null);
        this.enteredDate = other.optionalEnteredDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.modifiedBy = other.optionalModifiedBy().map(StringFilter::copy).orElse(null);
        this.modifiedDate = other.optionalModifiedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.eventStatus = other.optionalEventStatus().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EventCriteria copy() {
        return new EventCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEventName() {
        return eventName;
    }

    public Optional<StringFilter> optionalEventName() {
        return Optional.ofNullable(eventName);
    }

    public StringFilter eventName() {
        if (eventName == null) {
            setEventName(new StringFilter());
        }
        return eventName;
    }

    public void setEventName(StringFilter eventName) {
        this.eventName = eventName;
    }

    public StringFilter getEventOrg() {
        return eventOrg;
    }

    public Optional<StringFilter> optionalEventOrg() {
        return Optional.ofNullable(eventOrg);
    }

    public StringFilter eventOrg() {
        if (eventOrg == null) {
            setEventOrg(new StringFilter());
        }
        return eventOrg;
    }

    public void setEventOrg(StringFilter eventOrg) {
        this.eventOrg = eventOrg;
    }

    public ZonedDateTimeFilter getEventDate() {
        return eventDate;
    }

    public Optional<ZonedDateTimeFilter> optionalEventDate() {
        return Optional.ofNullable(eventDate);
    }

    public ZonedDateTimeFilter eventDate() {
        if (eventDate == null) {
            setEventDate(new ZonedDateTimeFilter());
        }
        return eventDate;
    }

    public void setEventDate(ZonedDateTimeFilter eventDate) {
        this.eventDate = eventDate;
    }

    public StringFilter getEventLocation() {
        return eventLocation;
    }

    public Optional<StringFilter> optionalEventLocation() {
        return Optional.ofNullable(eventLocation);
    }

    public StringFilter eventLocation() {
        if (eventLocation == null) {
            setEventLocation(new StringFilter());
        }
        return eventLocation;
    }

    public void setEventLocation(StringFilter eventLocation) {
        this.eventLocation = eventLocation;
    }

    public StringFilter getEnteredBy() {
        return enteredBy;
    }

    public Optional<StringFilter> optionalEnteredBy() {
        return Optional.ofNullable(enteredBy);
    }

    public StringFilter enteredBy() {
        if (enteredBy == null) {
            setEnteredBy(new StringFilter());
        }
        return enteredBy;
    }

    public void setEnteredBy(StringFilter enteredBy) {
        this.enteredBy = enteredBy;
    }

    public ZonedDateTimeFilter getEnteredDate() {
        return enteredDate;
    }

    public Optional<ZonedDateTimeFilter> optionalEnteredDate() {
        return Optional.ofNullable(enteredDate);
    }

    public ZonedDateTimeFilter enteredDate() {
        if (enteredDate == null) {
            setEnteredDate(new ZonedDateTimeFilter());
        }
        return enteredDate;
    }

    public void setEnteredDate(ZonedDateTimeFilter enteredDate) {
        this.enteredDate = enteredDate;
    }

    public StringFilter getModifiedBy() {
        return modifiedBy;
    }

    public Optional<StringFilter> optionalModifiedBy() {
        return Optional.ofNullable(modifiedBy);
    }

    public StringFilter modifiedBy() {
        if (modifiedBy == null) {
            setModifiedBy(new StringFilter());
        }
        return modifiedBy;
    }

    public void setModifiedBy(StringFilter modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ZonedDateTimeFilter getModifiedDate() {
        return modifiedDate;
    }

    public Optional<ZonedDateTimeFilter> optionalModifiedDate() {
        return Optional.ofNullable(modifiedDate);
    }

    public ZonedDateTimeFilter modifiedDate() {
        if (modifiedDate == null) {
            setModifiedDate(new ZonedDateTimeFilter());
        }
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTimeFilter modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public StringFilter getEventStatus() {
        return eventStatus;
    }

    public Optional<StringFilter> optionalEventStatus() {
        return Optional.ofNullable(eventStatus);
    }

    public StringFilter eventStatus() {
        if (eventStatus == null) {
            setEventStatus(new StringFilter());
        }
        return eventStatus;
    }

    public void setEventStatus(StringFilter eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventCriteria that = (EventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(eventName, that.eventName) &&
            Objects.equals(eventOrg, that.eventOrg) &&
            Objects.equals(eventDate, that.eventDate) &&
            Objects.equals(eventLocation, that.eventLocation) &&
            Objects.equals(enteredBy, that.enteredBy) &&
            Objects.equals(enteredDate, that.enteredDate) &&
            Objects.equals(modifiedBy, that.modifiedBy) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(eventStatus, that.eventStatus) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            eventName,
            eventOrg,
            eventDate,
            eventLocation,
            enteredBy,
            enteredDate,
            modifiedBy,
            modifiedDate,
            eventStatus,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEventName().map(f -> "eventName=" + f + ", ").orElse("") +
            optionalEventOrg().map(f -> "eventOrg=" + f + ", ").orElse("") +
            optionalEventDate().map(f -> "eventDate=" + f + ", ").orElse("") +
            optionalEventLocation().map(f -> "eventLocation=" + f + ", ").orElse("") +
            optionalEnteredBy().map(f -> "enteredBy=" + f + ", ").orElse("") +
            optionalEnteredDate().map(f -> "enteredDate=" + f + ", ").orElse("") +
            optionalModifiedBy().map(f -> "modifiedBy=" + f + ", ").orElse("") +
            optionalModifiedDate().map(f -> "modifiedDate=" + f + ", ").orElse("") +
            optionalEventStatus().map(f -> "eventStatus=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

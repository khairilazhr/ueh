package com.ueh.my.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EventCriteriaTest {

    @Test
    void newEventCriteriaHasAllFiltersNullTest() {
        var eventCriteria = new EventCriteria();
        assertThat(eventCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void eventCriteriaFluentMethodsCreatesFiltersTest() {
        var eventCriteria = new EventCriteria();

        setAllFilters(eventCriteria);

        assertThat(eventCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void eventCriteriaCopyCreatesNullFilterTest() {
        var eventCriteria = new EventCriteria();
        var copy = eventCriteria.copy();

        assertThat(eventCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(eventCriteria)
        );
    }

    @Test
    void eventCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var eventCriteria = new EventCriteria();
        setAllFilters(eventCriteria);

        var copy = eventCriteria.copy();

        assertThat(eventCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(eventCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var eventCriteria = new EventCriteria();

        assertThat(eventCriteria).hasToString("EventCriteria{}");
    }

    private static void setAllFilters(EventCriteria eventCriteria) {
        eventCriteria.id();
        eventCriteria.eventName();
        eventCriteria.eventDesc();
        eventCriteria.eventOrg();
        eventCriteria.eventDate();
        eventCriteria.eventLocation();
        eventCriteria.enteredBy();
        eventCriteria.enteredDate();
        eventCriteria.modifiedBy();
        eventCriteria.modifiedDate();
        eventCriteria.eventStatus();
        eventCriteria.distinct();
    }

    private static Condition<EventCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEventName()) &&
                condition.apply(criteria.getEventDesc()) &&
                condition.apply(criteria.getEventOrg()) &&
                condition.apply(criteria.getEventDate()) &&
                condition.apply(criteria.getEventLocation()) &&
                condition.apply(criteria.getEnteredBy()) &&
                condition.apply(criteria.getEnteredDate()) &&
                condition.apply(criteria.getModifiedBy()) &&
                condition.apply(criteria.getModifiedDate()) &&
                condition.apply(criteria.getEventStatus()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EventCriteria> copyFiltersAre(EventCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEventName(), copy.getEventName()) &&
                condition.apply(criteria.getEventDesc(), copy.getEventDesc()) &&
                condition.apply(criteria.getEventOrg(), copy.getEventOrg()) &&
                condition.apply(criteria.getEventDate(), copy.getEventDate()) &&
                condition.apply(criteria.getEventLocation(), copy.getEventLocation()) &&
                condition.apply(criteria.getEnteredBy(), copy.getEnteredBy()) &&
                condition.apply(criteria.getEnteredDate(), copy.getEnteredDate()) &&
                condition.apply(criteria.getModifiedBy(), copy.getModifiedBy()) &&
                condition.apply(criteria.getModifiedDate(), copy.getModifiedDate()) &&
                condition.apply(criteria.getEventStatus(), copy.getEventStatus()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

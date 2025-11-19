<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="event">
        <h2 class="jh-entity-heading" data-cy="eventDetailsHeading"><span>Event</span> {{ event.id }}</h2>
        <dl class="row jh-entity-details">
          <dt>
            <span>Event Name</span>
          </dt>
          <dd>
            <span>{{ event.eventName }}</span>
          </dd>
          <dt>
            <span>Event Desc</span>
          </dt>
          <dd>
            <span>{{ event.eventDesc }}</span>
          </dd>
          <dt>
            <span>Event Org</span>
          </dt>
          <dd>
            <span>{{ event.eventOrg }}</span>
          </dd>
          <dt>
            <span>Event Date</span>
          </dt>
          <dd>
            <span v-if="event.eventDate">{{ formatDateLong(event.eventDate) }}</span>
          </dd>
          <dt>
            <span>Event Location</span>
          </dt>
          <dd>
            <span>{{ event.eventLocation }}</span>
          </dd>
          <dt>
            <span>Event Poster</span>
          </dt>
          <dd>
            <div v-if="event.eventPoster">
              <a @click="openFile(event.eventPosterContentType, event.eventPoster)">Open</a>
              {{ event.eventPosterContentType }}, {{ byteSize(event.eventPoster) }}
            </div>
          </dd>
          <dt>
            <span>Entered By</span>
          </dt>
          <dd>
            <span>{{ event.enteredBy }}</span>
          </dd>
          <dt>
            <span>Entered Date</span>
          </dt>
          <dd>
            <span v-if="event.enteredDate">{{ formatDateLong(event.enteredDate) }}</span>
          </dd>
          <dt>
            <span>Modified By</span>
          </dt>
          <dd>
            <span>{{ event.modifiedBy }}</span>
          </dd>
          <dt>
            <span>Modified Date</span>
          </dt>
          <dd>
            <span v-if="event.modifiedDate">{{ formatDateLong(event.modifiedDate) }}</span>
          </dd>
          <dt>
            <span>Event Status</span>
          </dt>
          <dd>
            <span>{{ event.eventStatus }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span>Back</span>
        </button>
        <router-link v-if="event.id" :to="{ name: 'EventEdit', params: { eventId: event.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span>Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./event-details.component.ts"></script>

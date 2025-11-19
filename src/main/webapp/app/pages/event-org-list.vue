<template>
  <div>
    <h2 id="page-heading" data-cy="EventHeading">
      <span id="event-heading">My Events</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh list</span>
        </button>
        <router-link :to="{ name: 'EventOrgCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-event"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span>Create a new Event</span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && events && events.length === 0">
      <span>No Events found</span>
    </div>
    <div class="table" v-if="events && events.length > 0">
      <table class="table table-bordered" aria-describedby="events">
        <thead>
          <tr>
            <!-- <th scope="row" @click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th> -->
            <th scope="row" @click="changeOrder('eventName')">
              <span>Event Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'eventName'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('eventDesc')" width="40%">
              <span>Event Desc</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'eventDesc'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('eventOrg')" width="10%">
              <span>Event Org</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'eventOrg'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('eventDate')" width="10%">
              <span>Event Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'eventDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('eventLocation')" width="10%">
              <span>Event Location</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'eventLocation'"></jhi-sort-indicator>
            </th>
            <!-- <th scope="row" @click="changeOrder('eventPoster')">
              <span>Event Poster</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'eventPoster'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('enteredBy')">
              <span>Entered By</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'enteredBy'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('enteredDate')">
              <span>Entered Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'enteredDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('modifiedBy')">
              <span>Modified By</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'modifiedBy'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('modifiedDate')">
              <span>Modified Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'modifiedDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('eventStatus')">
              <span>Event Status</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'eventStatus'"></jhi-sort-indicator>
            </th> -->
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="event in events" :key="event.id" data-cy="entityTable">
            <!-- <td>
              <router-link :to="{ name: 'EventView', params: { eventId: event.id } }">{{ event.id }}</router-link>
            </td> -->
            <td>{{ event.eventName }}</td>
            <td>{{ event.eventDesc }}</td>
            <td>{{ event.eventOrg }}</td>
            <td>{{ formatDateShort(event.eventDate) || '' }}</td>
            <td>{{ event.eventLocation }}</td>
            <!-- <td>
              <a v-if="event.eventPoster" @click="openFile(event.eventPosterContentType, event.eventPoster)">Open</a>
              <span v-if="event.eventPoster">{{ event.eventPosterContentType }}, {{ byteSize(event.eventPoster) }}</span>
            </td>
            <td>{{ event.enteredBy }}</td>
            <td>{{ formatDateShort(event.enteredDate) || '' }}</td>
            <td>{{ event.modifiedBy }}</td>
            <td>{{ formatDateShort(event.modifiedDate) || '' }}</td>
            <td>{{ event.eventStatus }}</td> -->
            <td class="text-right" width="10%">
              <div class="btn-group">
                <router-link :to="{ name: 'EventOrgDetails', params: { eventId: event.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-sm-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'EventOrgEdit', params: { eventId: event.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-sm-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(event)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-sm-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="uehApp.event.delete.question" data-cy="eventDeleteDialogHeading">Confirm delete operation</span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-event-heading">Are you sure you want to delete Event {{ removeId }}?</p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" @click="closeDialog()">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-event"
            data-cy="entityConfirmDeleteButton"
            @click="removeEvent()"
          >
            Delete
          </button>
        </div>
      </template>
    </b-modal>
    <div v-show="events && events.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./event-org-list.component.ts"></script>

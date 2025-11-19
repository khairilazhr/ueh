<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="uehApp.event.home.createOrEditLabel" data-cy="EventCreateUpdateHeading">Create or edit a Event</h2>
        <div>
          <div class="form-group" v-if="event.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="event.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-eventName">Event Name</label>
            <input
              type="text"
              class="form-control"
              name="eventName"
              id="event-eventName"
              data-cy="eventName"
              :class="{ valid: !v$.eventName.$invalid, invalid: v$.eventName.$invalid }"
              v-model="v$.eventName.$model"
              required
            />
            <div v-if="v$.eventName.$anyDirty && v$.eventName.$invalid">
              <small class="form-text text-danger" v-for="error of v$.eventName.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-eventDesc">Event Desc</label>
            <textarea
              class="form-control"
              name="eventDesc"
              id="event-eventDesc"
              data-cy="eventDesc"
              :class="{ valid: !v$.eventDesc.$invalid, invalid: v$.eventDesc.$invalid }"
              v-model="v$.eventDesc.$model"
              required
            ></textarea>
            <div v-if="v$.eventDesc.$anyDirty && v$.eventDesc.$invalid">
              <small class="form-text text-danger" v-for="error of v$.eventDesc.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-eventOrg">Event Org</label>
            <input
              type="text"
              class="form-control"
              name="eventOrg"
              id="event-eventOrg"
              data-cy="eventOrg"
              :class="{ valid: !v$.eventOrg.$invalid, invalid: v$.eventOrg.$invalid }"
              v-model="v$.eventOrg.$model"
              required
            />
            <div v-if="v$.eventOrg.$anyDirty && v$.eventOrg.$invalid">
              <small class="form-text text-danger" v-for="error of v$.eventOrg.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-eventDate">Event Date</label>
            <div class="d-flex">
              <input
                id="event-eventDate"
                data-cy="eventDate"
                type="datetime-local"
                class="form-control"
                name="eventDate"
                :class="{ valid: !v$.eventDate.$invalid, invalid: v$.eventDate.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.eventDate.$model)"
                @change="updateZonedDateTimeField('eventDate', $event)"
              />
            </div>
            <div v-if="v$.eventDate.$anyDirty && v$.eventDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.eventDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-eventLocation">Event Location</label>
            <input
              type="text"
              class="form-control"
              name="eventLocation"
              id="event-eventLocation"
              data-cy="eventLocation"
              :class="{ valid: !v$.eventLocation.$invalid, invalid: v$.eventLocation.$invalid }"
              v-model="v$.eventLocation.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-eventPoster">Event Poster</label>
            <div>
              <div v-if="event.eventPoster" class="form-text text-danger clearfix">
                <a class="pull-left" @click="openFile(event.eventPosterContentType, event.eventPoster)">Open</a><br />
                <span class="pull-left">{{ event.eventPosterContentType }}, {{ byteSize(event.eventPoster) }}</span>
                <button
                  type="button"
                  @click="
                    event.eventPoster = null;
                    event.eventPosterContentType = null;
                  "
                  class="btn btn-secondary btn-xs pull-right"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </button>
              </div>
              <label for="file_eventPoster" class="btn btn-primary pull-right">Add blob</label>
              <input
                type="file"
                ref="file_eventPoster"
                id="file_eventPoster"
                style="display: none"
                data-cy="eventPoster"
                @change="setFileData($event, event, 'eventPoster', false)"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="eventPoster"
              id="event-eventPoster"
              data-cy="eventPoster"
              :class="{ valid: !v$.eventPoster.$invalid, invalid: v$.eventPoster.$invalid }"
              v-model="v$.eventPoster.$model"
            />
            <input
              type="hidden"
              class="form-control"
              name="eventPosterContentType"
              id="event-eventPosterContentType"
              v-model="event.eventPosterContentType"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-enteredBy">Entered By</label>
            <input
              type="text"
              class="form-control"
              name="enteredBy"
              id="event-enteredBy"
              data-cy="enteredBy"
              :class="{ valid: !v$.enteredBy.$invalid, invalid: v$.enteredBy.$invalid }"
              v-model="v$.enteredBy.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-enteredDate">Entered Date</label>
            <div class="d-flex">
              <input
                id="event-enteredDate"
                data-cy="enteredDate"
                type="datetime-local"
                class="form-control"
                name="enteredDate"
                :class="{ valid: !v$.enteredDate.$invalid, invalid: v$.enteredDate.$invalid }"
                :value="convertDateTimeFromServer(v$.enteredDate.$model)"
                @change="updateZonedDateTimeField('enteredDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-modifiedBy">Modified By</label>
            <input
              type="text"
              class="form-control"
              name="modifiedBy"
              id="event-modifiedBy"
              data-cy="modifiedBy"
              :class="{ valid: !v$.modifiedBy.$invalid, invalid: v$.modifiedBy.$invalid }"
              v-model="v$.modifiedBy.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-modifiedDate">Modified Date</label>
            <div class="d-flex">
              <input
                id="event-modifiedDate"
                data-cy="modifiedDate"
                type="datetime-local"
                class="form-control"
                name="modifiedDate"
                :class="{ valid: !v$.modifiedDate.$invalid, invalid: v$.modifiedDate.$invalid }"
                :value="convertDateTimeFromServer(v$.modifiedDate.$model)"
                @change="updateZonedDateTimeField('modifiedDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" for="event-eventStatus">Event Status</label>
            <input
              type="text"
              class="form-control"
              name="eventStatus"
              id="event-eventStatus"
              data-cy="eventStatus"
              :class="{ valid: !v$.eventStatus.$invalid, invalid: v$.eventStatus.$invalid }"
              v-model="v$.eventStatus.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./event-update.component.ts"></script>

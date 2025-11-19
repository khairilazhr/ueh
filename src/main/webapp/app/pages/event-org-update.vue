<template>
  <div class="container-fluid py-5">
    <div class="row justify-content-center">
      <div class="col-lg-10 col-12">
        <div class="card shadow-lg border-0 rounded-4 p-4 p-md-5">
          <div class="card-body">
            <h2
              class="card-title text-center mb-5 fw-bold text-primary"
              id="uehApp.event.home.createOrEditLabel"
              data-cy="EventCreateUpdateHeading"
            >
              {{ event.id ? 'Edit Event Details' : 'Create New Event' }}
            </h2>

            <form name="editForm" novalidate @submit.prevent="save()">
              <div class="mb-4" v-if="event.id">
                <label for="id" class="form-label text-secondary fw-semibold small">Event ID</label>
                <input
                  type="text"
                  class="form-control rounded-3 bg-light border-0"
                  id="id"
                  name="id"
                  v-model="event.id"
                  readonly
                  aria-label="Event ID"
                />
              </div>

              <div class="mb-4">
                <label class="form-label fw-semibold" for="event-eventName">Event Name <span class="text-danger">*</span></label>
                <input
                  type="text"
                  class="form-control rounded-3 border"
                  name="eventName"
                  id="event-eventName"
                  data-cy="eventName"
                  :class="{
                    'is-valid': !v$.eventName.$invalid && v$.eventName.$dirty,
                    'is-invalid': v$.eventName.$invalid && v$.eventName.$dirty,
                  }"
                  v-model="v$.eventName.$model"
                  required
                  placeholder="Enter a descriptive event name"
                />
                <div v-if="v$.eventName.$anyDirty && v$.eventName.$invalid" class="invalid-feedback d-block small mt-1">
                  <div v-for="error of v$.eventName.$errors" :key="error.$uid">{{ error.$message }}</div>
                </div>
              </div>

              <div class="mb-4">
                <label class="form-label fw-semibold" for="event-eventDesc">Description <span class="text-danger">*</span></label>
                <textarea
                  class="form-control rounded-3 border"
                  name="eventDesc"
                  id="event-eventDesc"
                  data-cy="eventDesc"
                  :class="{
                    'is-valid': !v$.eventDesc.$invalid && v$.eventDesc.$dirty,
                    'is-invalid': v$.eventDesc.$invalid && v$.eventDesc.$dirty,
                  }"
                  v-model="v$.eventDesc.$model"
                  required
                  rows="4"
                  placeholder="Provide a detailed description of the event"
                ></textarea>
                <div v-if="v$.eventDesc.$anyDirty && v$.eventDesc.$invalid" class="invalid-feedback d-block small mt-1">
                  <div v-for="error of v$.eventDesc.$errors" :key="error.$uid">{{ error.$message }}</div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 mb-4">
                  <label class="form-label fw-semibold" for="event-eventOrg">Organization <span class="text-danger">*</span></label>
                  <input
                    type="text"
                    class="form-control rounded-3 border"
                    name="eventOrg"
                    id="event-eventOrg"
                    data-cy="eventOrg"
                    :class="{
                      'is-valid': !v$.eventOrg.$invalid && v$.eventOrg.$dirty,
                      'is-invalid': v$.eventOrg.$invalid && v$.eventOrg.$dirty,
                    }"
                    v-model="v$.eventOrg.$model"
                    required
                    placeholder="Organizing body or department"
                  />
                  <div v-if="v$.eventOrg.$anyDirty && v$.eventOrg.$invalid" class="invalid-feedback d-block small mt-1">
                    <div v-for="error of v$.eventOrg.$errors" :key="error.$uid">{{ error.$message }}</div>
                  </div>
                </div>

                <div class="col-md-6 mb-4">
                  <label class="form-label fw-semibold" for="event-eventDate">Date & Time <span class="text-danger">*</span></label>
                  <input
                    id="event-eventDate"
                    data-cy="eventDate"
                    type="datetime-local"
                    class="form-control rounded-3 border"
                    name="eventDate"
                    :class="{
                      'is-valid': !v$.eventDate.$invalid && v$.eventDate.$dirty,
                      'is-invalid': v$.eventDate.$invalid && v$.eventDate.$dirty,
                    }"
                    required
                    :value="convertDateTimeFromServer(v$.eventDate.$model)"
                    @change="updateZonedDateTimeField('eventDate', $event)"
                  />
                  <div v-if="v$.eventDate.$anyDirty && v$.eventDate.$invalid" class="invalid-feedback d-block small mt-1">
                    <div v-for="error of v$.eventDate.$errors" :key="error.$uid">{{ error.$message }}</div>
                  </div>
                </div>
              </div>

              <div class="mb-4">
                <label class="form-label fw-semibold" for="event-eventLocation">Location</label>
                <input
                  type="text"
                  class="form-control rounded-3 border"
                  name="eventLocation"
                  id="event-eventLocation"
                  data-cy="eventLocation"
                  :class="{
                    'is-valid': !v$.eventLocation.$invalid && v$.eventLocation.$dirty,
                    'is-invalid': v$.eventLocation.$invalid && v$.eventLocation.$dirty,
                  }"
                  v-model="v$.eventLocation.$model"
                  placeholder="Venue or online meeting link"
                />
              </div>

              <div class="mb-5">
                <label class="form-label fw-semibold" for="event-eventPoster">Event Poster (Image/File)</label>
                <div
                  class="border border-2 rounded-3 p-3 bg-light d-flex flex-column align-items-center justify-content-center text-center"
                >
                  <div
                    v-if="event.eventPoster"
                    class="w-100 mb-3 p-2 bg-white rounded-3 shadow-sm d-flex justify-content-between align-items-center"
                  >
                    <div>
                      <a
                        class="text-info text-decoration-none small fw-bold"
                        @click="openFile(event.eventPosterContentType, event.eventPoster)"
                      >
                        <font-awesome-icon icon="eye" class="me-1"></font-awesome-icon> View File
                      </a>
                      <span class="text-muted small ms-3">({{ event.eventPosterContentType }} / {{ byteSize(event.eventPoster) }})</span>
                    </div>
                    <button
                      type="button"
                      @click="
                        event.eventPoster = null;
                        event.eventPosterContentType = null;
                      "
                      class="btn btn-sm btn-outline-danger border-0"
                      aria-label="Remove poster"
                    >
                      <font-awesome-icon icon="times"></font-awesome-icon>
                    </button>
                  </div>

                  <label for="file_eventPoster" class="btn btn-outline-primary rounded-pill w-75 p-2 mt-2">
                    <font-awesome-icon icon="upload" class="me-2"></font-awesome-icon>
                    {{ event.eventPoster ? 'Change Poster' : 'Upload Poster' }}
                  </label>

                  <input
                    type="file"
                    ref="file_eventPoster"
                    id="file_eventPoster"
                    style="display: none"
                    data-cy="eventPoster"
                    @change="setFileData($event, event, 'eventPoster', false)"
                  />
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
              </div>

              <div class="d-flex gap-3 justify-content-end pt-3">
                <button
                  type="button"
                  id="cancel-save"
                  data-cy="entityCreateCancelButton"
                  class="btn btn-outline-secondary rounded-pill px-4 shadow-sm"
                  @click="previousState()"
                >
                  <font-awesome-icon icon="arrow-left" class="me-2"></font-awesome-icon> Back
                </button>
                <button
                  type="submit"
                  id="save-entity"
                  data-cy="entityCreateSaveButton"
                  :disabled="v$.$invalid || isSaving"
                  class="btn btn-primary rounded-pill px-4 shadow-sm"
                >
                  <font-awesome-icon :icon="isSaving ? 'spinner' : 'save'" :spin="isSaving" class="me-2"></font-awesome-icon>
                  {{ isSaving ? 'Saving...' : 'Save Event' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" src="./event-org-update.component.ts"></script>

<template>
  <!-- Main container (full width) -->
  <div class="container-fluid py-5">
    <div class="row justify-content-center">
      <!-- Card container with modern styling and shadow -->
      <div class="col-lg-10 col-12">
        <div v-if="event" class="card shadow-lg border-0 rounded-4 p-4 p-md-5">
          <div class="card-body">
            <!-- Header with ID and prominent title -->
            <div class="d-flex justify-content-between align-items-baseline mb-5">
              <h2 class="card-title fw-bold text-primary" data-cy="eventDetailsHeading">Event Details</h2>
              <span class="text-secondary small fw-semibold">ID: {{ event.id }}</span>
            </div>

            <!-- Event Name -->
            <div class="mb-4">
              <label class="form-label fw-semibold">Event Name</label>
              <div class="p-2 border rounded-3 bg-light text-break">
                {{ event.eventName }}
              </div>
            </div>

            <!-- Description -->
            <div class="mb-4">
              <label class="form-label fw-semibold">Description</label>
              <div class="p-2 border rounded-3 bg-light text-break" style="min-height: 100px">
                {{ event.eventDesc }}
              </div>
            </div>

            <!-- Organization and Date/Time - Side by Side -->
            <div class="row">
              <!-- Organization -->
              <div class="col-md-6 mb-4">
                <label class="form-label fw-semibold">Organization</label>
                <div class="p-2 border rounded-3 bg-light">
                  {{ event.eventOrg }}
                </div>
              </div>

              <!-- Date & Time -->
              <div class="col-md-6 mb-4">
                <label class="form-label fw-semibold">Date & Time</label>
                <div class="p-2 border rounded-3 bg-light">
                  <span v-if="event.eventDate">{{ formatDateLong(event.eventDate) }}</span>
                  <span v-else class="text-muted">N/A</span>
                </div>
              </div>
            </div>

            <!-- Location -->
            <div class="mb-4">
              <label class="form-label fw-semibold">Location</label>
              <div class="p-2 border rounded-3 bg-light">
                {{ event.eventLocation || 'Online/TBD' }}
              </div>
            </div>

            <!-- Poster Display -->
            <div class="mb-5">
              <label class="form-label fw-semibold">Event Poster</label>
              <div class="border border-2 rounded-3 p-3 bg-light d-flex flex-column align-items-start">
                <div v-if="event.eventPoster" class="w-100 d-flex justify-content-between align-items-center">
                  <div>
                    <a
                      @click="openFile(event.eventPosterContentType, event.eventPoster)"
                      class="text-info text-decoration-none small fw-bold me-3"
                    >
                      <font-awesome-icon icon="eye" class="me-1"></font-awesome-icon> View File
                    </a>
                    <span class="text-muted small">({{ event.eventPosterContentType }} / {{ byteSize(event.eventPoster) }})</span>
                  </div>
                </div>
                <div v-else class="text-muted small py-1">No event poster uploaded.</div>
              </div>
            </div>

            <!-- Action Buttons -->
            <div class="d-flex gap-3 justify-content-start pt-3 border-top mt-4">
              <!-- Back Button -->
              <button
                type="submit"
                @click.prevent="previousState()"
                class="btn btn-outline-secondary rounded-pill px-4 shadow-sm"
                data-cy="entityDetailsBackButton"
              >
                <font-awesome-icon icon="arrow-left" class="me-2"></font-awesome-icon><span>Back</span>
              </button>

              <!-- Edit Button -->
              <router-link v-if="event.id" :to="{ name: 'EventEdit', params: { eventId: event.id } }" custom v-slot="{ navigate }">
                <button @click="navigate" class="btn btn-primary rounded-pill px-4 shadow-sm">
                  <font-awesome-icon icon="pencil-alt" class="me-2"></font-awesome-icon><span>Edit Event</span>
                </button>
              </router-link>
            </div>
          </div>
        </div>
        <div v-else class="alert alert-warning text-center">No event data available.</div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./event-org-details.component.ts"></script>

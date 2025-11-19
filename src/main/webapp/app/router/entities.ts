import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const Event = () => import('@/entities/event/event.vue');
const EventUpdate = () => import('@/entities/event/event-update.vue');
const EventDetails = () => import('@/entities/event/event-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'event',
      name: 'Event',
      component: Event,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'event/new',
      name: 'EventCreate',
      component: EventUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'event/:eventId/edit',
      name: 'EventEdit',
      component: EventUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'event/:eventId/view',
      name: 'EventView',
      component: EventDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};

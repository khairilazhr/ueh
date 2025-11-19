import { Authority } from '@/shared/security/authority';

// Import your page components
const EventList = () => import('@/pages/event-list.vue');

export default [
  {
    path: '/events',
    name: 'EventList',
    component: EventList,
    meta: { authorities: [Authority.USER] },
  },
  // Add more pages here
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];

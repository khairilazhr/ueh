import { Authority } from '@/shared/security/authority';

// Import your page components
const EventOrgList = () => import('@/pages/event-org-list.vue');
const EventOrgUpdate = () => import('@/pages/event-org-update.vue');
const EventOrgDetails = () => import('@/pages/event-org-details.vue');

export default [
  {
    path: '/event-org-list',
    name: 'EventOrgList',
    component: EventOrgList,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/event-org-update/new',
    name: 'EventOrgCreate',
    component: EventOrgUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/event-org-update/:eventId?/edit',
    name: 'EventOrgEdit',
    component: EventOrgUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/event-org-details/:eventId?/View',
    name: 'EventOrgDetails',
    component: EventOrgDetails,
    meta: { authorities: [Authority.USER] },
  },
  // Add more pages here
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];

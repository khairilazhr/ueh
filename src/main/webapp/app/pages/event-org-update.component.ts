import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import EventService from '@/service/event.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';
import { useAccountStore } from '@/shared/config/store/account-store';

import { Event, type IEvent } from '@/shared/model/event.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'EventUpdate',
  setup() {
    const eventService = inject('eventService', () => new EventService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const accountStore = useAccountStore();

    const event: Ref<IEvent> = ref(new Event());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveEvent = async eventId => {
      try {
        const res = await eventService().find(eventId);
        res.eventDate = new Date(res.eventDate);
        res.enteredDate = new Date(res.enteredDate);
        res.modifiedDate = new Date(res.modifiedDate);
        event.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.eventId) {
      retrieveEvent(route.params.eventId);
    }

    const dataUtils = useDataUtils();

    const validations = useValidation();
    const validationRules = {
      eventName: {
        required: validations.required('This field is required.'),
      },
      eventDesc: {
        required: validations.required('This field is required.'),
      },
      eventOrg: {
        required: validations.required('This field is required.'),
      },
      eventDate: {
        required: validations.required('This field is required.'),
      },
      eventLocation: {},
      eventPoster: {},
      enteredBy: {},
      enteredDate: {},
      modifiedBy: {},
      modifiedDate: {},
      eventStatus: {},
    };
    const v$ = useVuelidate(validationRules, event as any);
    v$.value.$validate();

    return {
      eventService,
      alertService,
      event,
      previousState,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: event }),
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;

      if (!this.event.id) {
        const accountStore = useAccountStore();
        this.event.enteredBy = accountStore.account?.login || null;
        this.event.enteredDate = new Date();
        this.event.eventStatus = 'A';
      }

      if (this.event.id) {
        this.eventService()
          .update(this.event)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(`A Event is updated with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.eventService()
          .create(this.event)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(`A Event is created with identifier ${param.id}`);
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});

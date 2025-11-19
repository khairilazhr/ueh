import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import EventService from './event.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Event } from '@/shared/model/event.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Event Service', () => {
    let service: EventService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new EventService();
      currentDate = new Date();
      elemDefault = new Event(
        123,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          eventDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          enteredDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          modifiedDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Event', async () => {
        const returnedFromService = {
          id: 123,
          eventDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          enteredDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          modifiedDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { eventDate: currentDate, enteredDate: currentDate, modifiedDate: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Event', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Event', async () => {
        const returnedFromService = {
          eventName: 'BBBBBB',
          eventDesc: 'BBBBBB',
          eventOrg: 'BBBBBB',
          eventDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          eventLocation: 'BBBBBB',
          eventPoster: 'BBBBBB',
          enteredBy: 'BBBBBB',
          enteredDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          modifiedBy: 'BBBBBB',
          modifiedDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          eventStatus: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { eventDate: currentDate, enteredDate: currentDate, modifiedDate: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Event', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Event', async () => {
        const patchObject = {
          eventOrg: 'BBBBBB',
          eventDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          eventLocation: 'BBBBBB',
          enteredDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          modifiedBy: 'BBBBBB',
          ...new Event(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { eventDate: currentDate, enteredDate: currentDate, modifiedDate: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Event', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Event', async () => {
        const returnedFromService = {
          eventName: 'BBBBBB',
          eventDesc: 'BBBBBB',
          eventOrg: 'BBBBBB',
          eventDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          eventLocation: 'BBBBBB',
          eventPoster: 'BBBBBB',
          enteredBy: 'BBBBBB',
          enteredDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          modifiedBy: 'BBBBBB',
          modifiedDate: dayjs(currentDate).format(DATE_TIME_FORMAT),
          eventStatus: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { eventDate: currentDate, enteredDate: currentDate, modifiedDate: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Event', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Event', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Event', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});

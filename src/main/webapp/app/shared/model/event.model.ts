export interface IEvent {
  id?: number;
  eventName?: string;
  eventDesc?: string;
  eventOrg?: string;
  eventDate?: Date;
  eventLocation?: string | null;
  eventPosterContentType?: string | null;
  eventPoster?: string | null;
  enteredBy?: string | null;
  enteredDate?: Date | null;
  modifiedBy?: string | null;
  modifiedDate?: Date | null;
  eventStatus?: string | null;
}

export class Event implements IEvent {
  constructor(
    public id?: number,
    public eventName?: string,
    public eventDesc?: string,
    public eventOrg?: string,
    public eventDate?: Date,
    public eventLocation?: string | null,
    public eventPosterContentType?: string | null,
    public eventPoster?: string | null,
    public enteredBy?: string | null,
    public enteredDate?: Date | null,
    public modifiedBy?: string | null,
    public modifiedDate?: Date | null,
    public eventStatus?: string | null,
  ) {}
}

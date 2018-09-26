import { IArea } from 'app/shared/model//area.model';
import { IJob } from 'app/shared/model//job.model';

export const enum Frequency {
  WEEKLY = 'WEEKLY',
  FORTNIGHTLY = 'FORTNIGHTLY',
  MONTHLY = 'MONTHLY'
}

export const enum PaymentMethod {
  BANK = 'BANK',
  CASH = 'CASH',
  ONLINE = 'ONLINE'
}

export interface ICustomer {
  id?: number;
  name?: string;
  email?: string;
  phone?: string;
  frequency?: Frequency;
  paymentMethod?: PaymentMethod;
  flatPosition?: string;
  number?: string;
  street?: string;
  area?: IArea;
  jobs?: IJob[];
}

export const defaultValue: Readonly<ICustomer> = {};

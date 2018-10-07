import { IJob } from 'app/shared/model//job.model';
import { IArea } from 'app/shared/model//area.model';

export const enum Frequency {
  MONTHLY = 'MONTHLY',
  TWO_MONTHLY = 'TWO_MONTHLY'
}

export const enum PaymentMethod {
  BANK = 'BANK',
  CASH = 'CASH',
  DIRECT_DEBIT = 'DIRECT_DEBIT',
  ONLINE = 'ONLINE'
}

export interface ICustomer {
  id?: number;
  name?: string;
  email?: string;
  phone?: string;
  frequency?: Frequency;
  paymentAmount?: number;
  paymentMethod?: PaymentMethod;
  flatPosition?: string;
  number?: string;
  street?: string;
  jobs?: IJob[];
  area?: IArea;
}

export const defaultValue: Readonly<ICustomer> = {};

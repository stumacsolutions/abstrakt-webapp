import { ICustomer } from 'app/shared/model//customer.model';

export const enum Week {
  ONE = 'ONE',
  TWO = 'TWO',
  THREE = 'THREE',
  FOUR = 'FOUR'
}

export interface IJob {
  id?: number;
  week?: Week;
  complete?: boolean;
  paid?: boolean;
  customer?: ICustomer;
}

export const defaultValue: Readonly<IJob> = {
  complete: false,
  paid: false
};

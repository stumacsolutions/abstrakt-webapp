import { ICustomer } from 'app/shared/model//customer.model';

export interface IArea {
  id?: number;
  name?: string;
  customers?: ICustomer[];
}

export const defaultValue: Readonly<IArea> = {};

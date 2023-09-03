import { IAccess } from './accesses';

export interface IWorkshop {
  workshopId?: string;
  workshopName: string;
  accesses: IAccess[];
}

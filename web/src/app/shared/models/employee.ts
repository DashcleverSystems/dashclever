export interface IEmployee {
  id: string | undefined;
  firstName: string;
  lastName: string | null;
  workplace: Workplace
}

export enum Workplace {
  LABOUR = "LABOUR", SUPERVISOR = "SUPERVISOR"
}

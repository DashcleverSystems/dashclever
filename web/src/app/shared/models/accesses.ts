export interface IAccess {
  isOwnerAccess: boolean;
  default: boolean;
  employeeId: string | null;
  employeeFirstName: string | null;
  authorities: string[];
}

import {Injectable} from '@angular/core';
import {ComponentStore} from '@ngrx/component-store';
import {IEmployee} from "@shared/models/employee";

interface ManageStaffStoreState {
    employees: IEmployee[];
}

@Injectable()
export class ManageStaffStore extends ComponentStore<ManageStaffStoreState> {
    constructor() {
        super({employees: []});
    }

    loadEmployees = this.updater((state, employees: IEmployee[]) => ({
        ...state,
        employees: employees,
    }));

    addEmployee = this.updater((state, employee: IEmployee) => {
        const newEmployees = [...state.employees];
        newEmployees.push(employee);
        console.log({newEmployees, state: state.employees});
        return {
            ...state,
            employees: newEmployees
        }
    });

    employees$ = this.select((state) => state.employees);
}

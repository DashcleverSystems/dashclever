import {Component, OnInit} from '@angular/core';

import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {IEmployee, Workplace} from "@shared/models/employee";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {DictionaryDTO, enumToDictionary} from "@shared/utils/dictionary";
import {HttpClient} from "@angular/common/http";
import {select, Store} from "@ngrx/store";
import {getSelectedWorkshop} from "@core/store/core-store.selectors";
import {EMPTY, Subject, switchMap, take} from "rxjs";

@Component({
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.scss'],
})
export class EmployeeFormComponent implements OnInit {

  constructor(
    public ref: DynamicDialogRef,
    private conf: DynamicDialogConfig,
    private fb: FormBuilder,
    private http: HttpClient,
    private store: Store,
  ) {}

  form: FormGroup<IEmployeeForm> = this.createEmployeeForm();
  isCreatingNewEmployee = this.conf.data.employee === undefined || this.conf.data.employee === null
  loadingSpinner = false;
  dictionaries: Dictionaries = {
    workplaces: enumToDictionary(Workplace, 'enum.Workplace')
  };
  private destroy$ = new Subject<void>();

  ngOnInit(): void {}

  createEmployeeForm(): FormGroup<IEmployeeForm> {
    const employee: IEmployee | null = this.conf.data.employee
    return this.fb.group({
      firstName: this.fb.control<string>(
          employee?.firstName ?? "",
          Validators.required
      ),
      lastName: this.fb.control<string | null>(
          employee?.lastName ?? null
      ),
      workplace: this.fb.control<Workplace>(
          employee?.workplace ?? Workplace.LABOUR,
          Validators.required
      ),
    });
  }

  submit() {
    if (this.form.invalid) {
      return;
    }
    this.store.pipe(
        select(getSelectedWorkshop),
        take(1),
        switchMap((workshop) => {
          console.log(workshop);
          if (!workshop) {
            return EMPTY;
          }
          const employee: IEmployee = {
            ...this.form.getRawValue(),
            id: undefined,
            workshopId: workshop.workshopId,
            firstName: this.form.controls.firstName.value ?? "",
            workplace: this.form.controls.workplace.value ?? Workplace.LABOUR
           }
          return this.http.post(`/api/employee`, employee)
        }),
    ).subscribe(res => this.ref.close())
  }

  onDestroy(): void {
    this.destroy$.next()
    this.destroy$.complete()
  }
}

export interface IEmployeeForm {
  firstName: FormControl<string | null>;
  lastName: FormControl<string | null>;
  workplace: FormControl<Workplace | null>;
}

interface Dictionaries {
  workplaces: DictionaryDTO<Workplace, string>[];
}

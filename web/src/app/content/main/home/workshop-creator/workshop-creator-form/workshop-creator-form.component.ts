import { Component } from '@angular/core';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { AccountRestApiService } from '@api/services/accountRestApi.service';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { CreateWorkshopReq } from '@api/models/createWorkshopReq';
import { ToastService } from '@shared/services/toast.service';

@Component({
  selector: 'app-workshop-creator-form',
  templateUrl: './workshop-creator-form.component.html',
  styleUrl: './workshop-creator-form.component.scss',
})
export class WorkshopCreatorFormComponent {
  constructor(
    public ref: DynamicDialogRef,
    private accountApi: AccountRestApiService,
    private fb: FormBuilder,
    private toast: ToastService,
  ) {}

  form: FormGroup<ICreateWorkshopForm> = this.createForm();

  submit(): void {
    const req: CreateWorkshopReq = {
      displayName: this.form.controls.workshopName.value,
    };
    this.accountApi.createWorkshop(req).subscribe({
      next: () => this.ref.close('SUCCESS'),
      error: () =>
        this.toast.error({
          title: 'toast.unknown',
          message: 'error.unknown',
          translate: true,
        }),
    });
  }

  private createForm(): FormGroup<ICreateWorkshopForm> {
    return this.fb.group({
      workshopName: this.fb.control<string>('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(25),
      ]),
    });
  }
}

interface ICreateWorkshopForm {
  workshopName: FormControl<string | undefined>;
}

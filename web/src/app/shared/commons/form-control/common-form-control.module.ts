import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { FormControlErrorOutput } from '@shared/commons/form-control/error-output/form-control-error-output.component';

@NgModule({
  declarations: [FormControlErrorOutput],
  imports: [ReactiveFormsModule, CommonModule],
  exports: [FormControlErrorOutput],
})
export class CommonFormControlModule {}

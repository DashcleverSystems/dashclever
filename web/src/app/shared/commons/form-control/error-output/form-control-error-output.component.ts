import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { UntypedFormControl, ValidationErrors } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { ErrorNameWithDescription } from '@shared/commons/form-control/error-output/error-explanation-provider.interface';
import { ErrorExplanationService } from '@shared/commons/form-control/error-output/error-explanation.service';

@Component({
  selector: 'app-form-control-error-output',
  template: ` <small [hidden]="!control.touched">{{ errorOutput }}</small> `,
  providers: [ErrorExplanationService],
  styles: [
    `
      .small {
        height: 100%;
        width: 100%;
        justify-content: left;
      }
    `,
  ],
})
export class FormControlErrorOutput implements OnInit, OnDestroy {
  @Input({ required: true })
  control!: UntypedFormControl;
  errorOutput?: string;

  private destroy$ = new Subject<void>();

  constructor(
    private readonly errorExplanationService: ErrorExplanationService,
  ) {}

  ngOnInit(): void {
    this.populateErrorOutput();
    this.control.statusChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.populateErrorOutput());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private populateErrorOutput() {
    if (this.control.invalid) {
      const error: ErrorNameWithDescription = this.getFirstError(
        this.control.errors,
      );
      this.errorOutput = this.errorExplanationService.provideExplanation(error);
    } else {
      this.errorOutput = null;
    }
  }

  private getFirstError(errors: ValidationErrors): ErrorNameWithDescription {
    const errorNames: string[] = Object.keys(errors);
    const firstErrorName: string = errorNames[0];
    const firstErrorDescription: any = errors[firstErrorName];
    return { name: firstErrorName, description: firstErrorDescription };
  }
}

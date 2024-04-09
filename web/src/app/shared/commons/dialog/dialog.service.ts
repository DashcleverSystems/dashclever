import { Injectable, Type } from '@angular/core';
import { Router } from '@angular/router';
import {
  DialogService,
  DynamicDialogConfig,
  DynamicDialogRef,
} from 'primeng/dynamicdialog';
import { DialogExtension } from './dialog-extension';
import { first } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AppDialogService {
  constructor(
    private router: Router,
    private dialogService: DialogService,
  ) {}

  open(
    component: Type<any>,
    configuration: DynamicDialogConfig = {},
  ): DynamicDialogRef {
    const dialogRef = this.dialogService.open(component, {
      showHeader: false,
      contentStyle: {
        'min-height': '100px',
        'max-height': '96vh',
        'overflow-y': 'auto',
        padding: 0,
      },
      baseZIndex: 10000,
      ...configuration,
      styleClass: configuration.styleClass
        ? `app-modal ${configuration.styleClass}`
        : 'app-modal',
    });

    DialogExtension.openModal();

    this.router.events.pipe(first()).subscribe(() => dialogRef.close());
    dialogRef.onClose
      .pipe(first())
      .subscribe(() => DialogExtension.closeModal());

    return dialogRef;
  }
}

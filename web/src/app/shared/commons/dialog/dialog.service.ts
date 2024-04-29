import { inject, Injectable, Type } from '@angular/core';
import { Router } from '@angular/router';
import {
  DialogService,
  DynamicDialogConfig,
  DynamicDialogRef,
} from 'primeng/dynamicdialog';
import { DialogExtension } from './dialog-extension';
import { first } from 'rxjs';
import CoreStore from '@app/core/store/core-store';

@Injectable({
  providedIn: 'root',
})
export class AppDialogService {
  private coreStore = inject(CoreStore);

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
        'max-height': '98svh',
        'overflow-y': 'auto',
        'overflow-x': 'hidden',
        padding: 0,
        width: this.coreStore.mobile() ? '95svw' : 'auto',
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

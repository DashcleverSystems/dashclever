import {
  Component,
  EventEmitter,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { WorkshopCreatorFormComponent } from '@shared/commons/workshop/workshop-creator/workshop-creator-form/workshop-creator-form.component';
import { WorkshopCreatedNotifier } from '@shared/commons/workshop/workshop-creator/workshop-creator.service';
import { Store } from '@ngrx/store';
import { result } from 'lodash';
import { getWorkshops } from '@core/store/core-store.selectors';
import { IWorkshop } from '@shared/models/workshop';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-workshop-creator',
  templateUrl: './workshop-creator.component.html',
  styleUrls: ['./workshop-creator.component.scss'],
})
export class WorkshopCreatorComponent implements OnInit, OnDestroy {
  constructor(
    private dialogService: DialogService,
    private workshopCreatedNotifier: WorkshopCreatedNotifier,
    private store: Store,
  ) {}

  canCreateWorkshop: boolean = false;
  private destroy$: Subject<void> = new Subject<void>();

  openWorkshopCreateForm() {
    const ref: DynamicDialogRef = this.dialogService.open(
      WorkshopCreatorFormComponent,
      {
        showHeader: false,
        closable: false,
        modal: true,
      },
    );
    ref.onClose.subscribe((result: string) => {
      if (result == 'SUCCESS') {
        this.workshopCreatedNotifier.notify();
      }
    });
  }

  ngOnInit(): void {
    this.store
      .select(getWorkshops)
      .pipe(takeUntil(this.destroy$))
      .subscribe((workshops: IWorkshop[]) => {
        if (workshops.length < 2) {
          this.canCreateWorkshop = true;
        } else {
          this.canCreateWorkshop = true;
        }
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

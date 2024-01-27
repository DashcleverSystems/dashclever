import { Component, OnDestroy, OnInit } from '@angular/core';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { WorkshopCreatorFormComponent } from '@app/content/main/home/workshop-creator/workshop-creator-form/workshop-creator-form.component';
import { WorkshopCreatedNotifier } from '@app/content/main/home/workshop-creator/workshop-creator.service';
import { IWorkshop } from '@shared/models/workshop';
import { Subject, takeUntil } from 'rxjs';
import { AccessesSelectorComponentStore } from '@app/content/main/home/accesses-selector/access-selector.store';

@Component({
  selector: 'app-workshop-creator',
  templateUrl: './workshop-creator.component.html',
  styleUrls: ['./workshop-creator.component.scss'],
})
export class WorkshopCreatorComponent implements OnInit, OnDestroy {
  constructor(
    private dialogService: DialogService,
    private workshopCreatedNotifier: WorkshopCreatedNotifier,
    private accessesStore: AccessesSelectorComponentStore,
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
    this.accessesStore.workshops$
      .pipe(takeUntil(this.accessesStore.destroy$))
      .subscribe((workshops: IWorkshop[]) => {
        this.canCreateWorkshop = workshops.length < 2;
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

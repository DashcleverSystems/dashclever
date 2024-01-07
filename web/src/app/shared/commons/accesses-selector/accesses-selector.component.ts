import { Component, OnDestroy, OnInit } from '@angular/core';
import { AccessesSelectorComponentStore } from '@shared/commons/accesses-selector/access-selector.store';
import { provideComponentStore } from '@ngrx/component-store';
import { WorkshopCreatedNotifier } from '@shared/commons/workshop/workshop-creator/workshop-creator.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-accesses-selector',
  templateUrl: './accesses-selector.component.html',
  styleUrls: ['./accesses-selector.component.scss'],
  providers: [provideComponentStore(AccessesSelectorComponentStore)],
})
export class AccessesSelectorComponent implements OnInit, OnDestroy {
  constructor(
    private readonly accessesStore: AccessesSelectorComponentStore,
    private readonly workshopCreatedNotifier: WorkshopCreatedNotifier,
  ) {}

  private workshopCreationSub: Subscription | null;

  ngOnInit(): void {
    this.workshopCreationSub =
      this.workshopCreatedNotifier.workshopCreatedListener.subscribe(() =>
        this.accessesStore
          .loadAccesses()
          .subscribe(() => this.accessesStore.loadCurrentAccess()),
      );
  }

  ngOnDestroy(): void {
    this.workshopCreationSub.unsubscribe();
  }

  onClick(item: any): void {}
}

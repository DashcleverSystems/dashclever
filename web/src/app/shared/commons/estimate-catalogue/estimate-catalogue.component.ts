import { Component, OnDestroy, OnInit } from '@angular/core';
import { DialogService } from 'primeng/dynamicdialog';
import { EstimateFormComponent } from './estimate-form/estimate-form.component';
import { Store } from '@ngrx/store';
import { isMobile } from '../../../core/store/core-store.selectors';
import { Subject, distinctUntilChanged, takeUntil } from 'rxjs';

@Component({
  selector: 'app-estimate-catalogue',
  templateUrl: './estimate-catalogue.component.html',
  styleUrls: ['./estimate-catalogue.component.scss'],
})
export class EstimateCatalogueComponent implements OnInit, OnDestroy {
  isMobile: boolean = false;

  private destroy$ = new Subject<void>();

  constructor(private dialogService: DialogService, private store: Store) {}

  ngOnInit(): void {
    this.store
      .select(isMobile)
      .pipe(takeUntil(this.destroy$), distinctUntilChanged())
      .subscribe((mobile) => (this.isMobile = mobile));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  createCatalogue(type: 'CREATE' | 'GENERATE'): void {
    if (type === 'CREATE') {
      this.dialogService.open(EstimateFormComponent, {
        data: {
          type,
        },
        closable: false,
        showHeader: false,
        modal: true,
      });
    }

    if (type === 'GENERATE') {
      this.dialogService.open(EstimateFormComponent, {
        data: {
          type,
          data: dataMock,
        },
        showHeader: false,
        closable: false,
        modal: true,
      });
    }
  }
}

const dataMock = {
  estimateId: '28/2022wk',
  vehicleInfo: {
    registration: 'ZKO8GG2',
    brand: 'peugeot',
    model: '307',
  },
  paintInfo: {
    baseColorWithCode: 'blue',
    varnishingPaintInfo: '2 layers pearl',
  },
  jobs: [
    {
      name: 'mount/unmount bumpers',
      manMinutes: 240,
      worth: {
        denomination: 240,
        currency: 'PLN',
      },
      jobType: 'LABOUR',
    },
    {
      name: 'mount/unmount bumpers',
      manMinutes: 500,
      worth: {
        denomination: 240,
        currency: 'EUR',
      },
      jobType: 'PAINTER',
    },
  ],
};

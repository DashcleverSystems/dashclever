import { Component } from '@angular/core';
import { EstimateCreateNotifier } from '@shared/commons/estimate-catalogue/estimate-create/estimate-create.notifier';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-estimate-catalogue',
  templateUrl: './estimate-catalogue.component.html',
  styleUrls: ['./estimate-catalogue.component.scss'],
  providers: [],
})
export class EstimateCatalogueComponent {
  constructor(
    private readonly estimateCreateNotifier: EstimateCreateNotifier,
  ) {}

  get estimateCreated$(): Observable<void> {
    return this.estimateCreateNotifier.estimateCreated$.asObservable();
  }
}

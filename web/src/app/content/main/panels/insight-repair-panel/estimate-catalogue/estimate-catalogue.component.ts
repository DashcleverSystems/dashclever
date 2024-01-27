import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { EstimateCreateNotifier } from '@content/main/panels/insight-repair-panel/estimate-catalogue/estimate-create/estimate-create.notifier';

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

import { inject, Inject, Injectable } from '@angular/core';
import CoreStore from './core-store';
import { DOCUMENT } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class CoreService {
  private coreStore = inject(CoreStore);

  private resizeObserver: ResizeObserver = new ResizeObserver(() =>
    Promise.resolve().then(() => this.checkMobile()),
  );

  constructor(@Inject(DOCUMENT) private document: Document) {
    this.initObserver();
  }

  private initObserver(): void {
    this.resizeObserver.observe(this.document.body);
  }

  private checkMobile(): void {
    const { clientWidth: width } = this.document.body;

    if (width >= 700) {
      this.coreStore.changeMobile(false);
    } else {
      this.coreStore.changeMobile(true);
    }
  }
}

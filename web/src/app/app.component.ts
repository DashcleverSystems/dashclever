import { Component, OnDestroy, OnInit } from '@angular/core';
import { CoreTranslateService } from './core/translate/core-translate.service';
import { environment } from '@env/environments';
import { Language } from './shared/enums/languages';
import { Store } from '@ngrx/store';
import { isAuthorized } from './core/store/core-store.selectors';
import { map, switchMap, take } from 'rxjs';
import { coreStoreActions } from './core/store/core-store.actions';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, OnDestroy {
  constructor(
    private coreTranslateService: CoreTranslateService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.coreTranslateService.init(
      environment.defaultLanguage as Language,
      environment.supportedLanguages as Language[]
    );
  }

  ngOnDestroy(): void {
    this.coreTranslateService.destroy();
  }
}

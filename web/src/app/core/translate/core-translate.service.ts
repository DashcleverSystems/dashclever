import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { TranslateService } from '@ngx-translate/core';
import { CoreSelectors } from '../store';

// @ts-ignore
// @ts-nocheck
import pl from '../../../assets/translations/pl.json';
// @ts-ignore
// @ts-nocheck
import en from '../../../assets/translations/en.json';

import { Language } from '@app/shared/enums/languages';
import { Subscription, last, skip } from 'rxjs';
import { coreStoreActions } from '../store/core-store.actions';

const languageKey = 'language';

@Injectable({
  providedIn: 'root',
})
export class CoreTranslateService {
  defaultLanguage!: Language;
  supportedLanguages!: Language[];

  private langChangeSubscription!: Subscription;

  constructor(
    private translateService: TranslateService,
    private store: Store
  ) {
    translateService.setTranslation('pl', pl);
    translateService.setTranslation('en', en);
  }

  init(defaultLanguage: Language, supportedLanguages: Language[]) {
    this.defaultLanguage = defaultLanguage;
    this.supportedLanguages = supportedLanguages;

    this.langChangeSubscription = this.store
      .select(CoreSelectors.selectedLanguage)
      .pipe(skip(1))
      .subscribe((event: Language) => {
        localStorage.setItem(languageKey, event);
      });

    const lastUsedLanguage = localStorage.getItem(languageKey);
    if (lastUsedLanguage) {
      this.translateService.use(lastUsedLanguage);
      this.language = lastUsedLanguage as Language;
    } else this.translateService.use(this.defaultLanguage);
  }

  destroy() {
    if (this.langChangeSubscription) {
      this.langChangeSubscription.unsubscribe();
    }
  }

  set language(language: Language) {
    let newLanguage =
      language ||
      localStorage.getItem(languageKey) ||
      this.translateService.getBrowserCultureLang() ||
      '';

    const isSupportedLanguage = this.supportedLanguages.includes(newLanguage);
    if (!newLanguage || !isSupportedLanguage) {
      newLanguage = this.defaultLanguage;
    }

    language = newLanguage;

    this.translateService.use(language);
    this.store.dispatch(coreStoreActions.changeLanguage({ lang: language }));
  }

  get language(): Language {
    return this.translateService.currentLang as Language;
  }
}

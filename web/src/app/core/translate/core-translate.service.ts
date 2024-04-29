import { inject, Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import pl from 'src/assets/translations/pl.json';
import en from 'src/assets/translations/en.json';

import { effect } from '@angular/core';
import { Language } from '@app/shared/enums/languages';
import CoreStore from '../store/core-store';

const languageKey = 'language';

@Injectable({
  providedIn: 'root',
})
export class CoreTranslateService {
  defaultLanguage!: Language;
  supportedLanguages!: Language[];

  private coreStore = inject(CoreStore);

  constructor(private translateService: TranslateService) {
    translateService.setTranslation('pl', pl);
    translateService.setTranslation('en', en);

    effect(() => {
      const lang = this.coreStore.lang();

      localStorage.setItem(languageKey, lang);
    });
  }

  init(defaultLanguage: Language, supportedLanguages: Language[]) {
    this.defaultLanguage = defaultLanguage;
    this.supportedLanguages = supportedLanguages;

    const lastUsedLanguage = localStorage.getItem(languageKey);
    if (lastUsedLanguage) {
      this.translateService.use(lastUsedLanguage);
      this.language = lastUsedLanguage as Language;
    } else this.translateService.use(this.defaultLanguage);
  }

  set language(language: Language) {
    let newLanguage =
      language ||
      localStorage.getItem(languageKey) ||
      this.translateService.getBrowserCultureLang() ||
      '';

    const isSupportedLanguage = this.supportedLanguages.includes(
      newLanguage as Language,
    );
    if (!newLanguage || !isSupportedLanguage) {
      newLanguage = this.defaultLanguage;
    }

    language = newLanguage as Language;

    this.translateService.use(language);
    this.coreStore.changeLanguage(language);
  }

  get language(): Language {
    return this.translateService.currentLang as Language;
  }
}

import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { Language } from 'src/app/shared/enums/languages';
import { CoreTranslateService } from '@app/core/translate/core-translate.service';

interface ILangSelect {
  code: Language;
  label: string;
}

@Component({
  selector: 'app-language-selector',
  templateUrl: './language-selector.component.html',
  styleUrls: ['./language-selector.component.scss'],
})
export class LanguageSelector {
  availableLanguages: ILangSelect[] = [
    { code: Language.EN, label: 'English' },
    { code: Language.PL, label: 'Polski' },
  ];

  constructor(
    private coreTranslate: CoreTranslateService,
    private store: Store
  ) {}

  changeLanguage(localeCode: Language): void {
    const selectedLanguage = this.availableLanguages.find(
      (lang) => lang.code === localeCode
    );

    if (selectedLanguage) {
      this.coreTranslate.language = selectedLanguage.code;
    }
  }

  get currentLanguage(): Language {
    return this.coreTranslate.language;
  }
}

import { Component, OnDestroy, OnInit } from '@angular/core';
import { Language } from 'src/app/shared/enums/languages';
import { CoreTranslateService } from '@app/core/translate/core-translate.service';
import { FormControl } from '@angular/forms';
import { distinctUntilChanged, Subject, takeUntil } from 'rxjs';
import { isEqual } from 'lodash';

interface ILangSelect {
  code: Language;
  label: string;
}

@Component({
  selector: 'app-language-selector',
  templateUrl: './language-selector.component.html',
  styleUrls: ['./language-selector.component.scss'],
})
export class LanguageSelector implements OnInit, OnDestroy {
  availableLanguages: ILangSelect[] = [
    { code: Language.EN, label: 'English' },
    { code: Language.PL, label: 'Polish' },
  ];

  languageControl: FormControl<Language> = new FormControl<Language>({
    value: this.currentLanguage,
    disabled: false,
  });

  get currentLanguage(): Language {
    return this.coreTranslate.language;
  }

  private destroy$ = new Subject<void>();

  constructor(private coreTranslate: CoreTranslateService) {}

  ngOnInit() {
    this.subscribeLanguageChanged();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private subscribeLanguageChanged(): void {
    this.languageControl.valueChanges
      .pipe(takeUntil(this.destroy$), distinctUntilChanged(isEqual))
      .subscribe((lang: Language) => {
        this.changeLanguage(lang);
      });
  }
  private changeLanguage(localeCode: Language): void {
    const selectedLanguage = this.availableLanguages.find(
      (lang) => lang.code === localeCode,
    );

    if (selectedLanguage) {
      this.coreTranslate.language = selectedLanguage.code;
    }
  }
}

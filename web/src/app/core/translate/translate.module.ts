import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import {
  TranslateModule,
  TranslateLoader,
  TranslateStore,
  TranslateService,
} from '@ngx-translate/core';
import { Store } from '@ngrx/store';
import { CoreSelectors } from '../store';
import { distinctUntilChanged } from 'rxjs';
import { Language } from 'src/app/shared/enums/languages';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    HttpClientModule,
    TranslateModule.forRoot({
      defaultLanguage: 'en',
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
    }),
  ],
  exports: [TranslateModule],
  providers: [TranslateStore, TranslateService],
})
export class NgxTranslateModule {
  constructor(private store: Store, private translate: TranslateService) {
    this.translate.defaultLang = Language.PL;

    this.store
      .select(CoreSelectors.selectedLanguage)
      .pipe()
      .subscribe((lang) => {
        console.log('LangChanged', lang);
        this.translate.use(lang);
      });
  }
}

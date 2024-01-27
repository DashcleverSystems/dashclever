import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import {
  TranslateLoader,
  TranslateModule,
  TranslateService,
  TranslateStore,
} from '@ngx-translate/core';
import { Store } from '@ngrx/store';
import { CoreSelectors } from '../store';
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
  constructor(
    private store: Store,
    private translate: TranslateService,
  ) {
    this.translate.defaultLang = Language.PL;

    this.store
      .select(CoreSelectors.selectedLanguage)
      .pipe()
      .subscribe((lang) => {
        this.translate.use(lang);
      });
  }
}

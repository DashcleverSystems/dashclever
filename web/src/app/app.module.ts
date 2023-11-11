import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import {CoreModule} from '@core/core.module';
import {ContentModule} from '@content/content.module';
import { AppRoutingModule } from './app-routing.module';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { TranslateModule } from '@ngx-translate/core';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import {ErrorHandlerInterceptor} from '@core/interceptors/error-handler.interceptor';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '@env/environments';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { DialogService } from 'primeng/dynamicdialog';
import {ApiModule, Configuration, ConfigurationParameters} from 'generated/openapi';

export function apiConfigFactory(): Configuration {
  const params: ConfigurationParameters = {
    basePath: "",
  }
  return new Configuration(params);
}


@NgModule({
  declarations: [AppComponent],
  imports: [
    ApiModule.forRoot(apiConfigFactory),
    BrowserModule,
    BrowserAnimationsModule,
    CoreModule,
    ContentModule,
    AppRoutingModule,
    HttpClientModule,
    ToastModule,
    TranslateModule.forRoot(),
    StoreDevtoolsModule.instrument({ maxAge: 25, logOnly: !isDevMode() }),
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: environment.production,
    }),
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlerInterceptor,
      multi: true,
    },
    MessageService,
    DialogService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}

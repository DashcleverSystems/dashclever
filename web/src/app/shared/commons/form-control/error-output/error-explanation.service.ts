import {
  ErrorNameWithDescription,
  IErrorExplanationProvider,
} from '@shared/commons/form-control/error-output/error-explanation-provider.interface';
import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MinMaxLengthErrorExplanationProvider } from '@shared/commons/form-control/error-output/min-max-length-error-explanation-provider';
import { DefaultErrorExplanationProvider } from '@shared/commons/form-control/error-output/default-error-explanation-provider';
import { NoParamErrorExplanationProvider } from '@shared/commons/form-control/error-output/no-param-error-explanation-provider';

@Injectable({
  providedIn: 'root',
})
export class ErrorExplanationService {
  constructor(private readonly translations: TranslateService) {}

  private readonly providers: IErrorExplanationProvider[] = [
    new MinMaxLengthErrorExplanationProvider(this.translations),
    new NoParamErrorExplanationProvider(this.translations),
  ];

  private readonly defaultProvider = new DefaultErrorExplanationProvider(
    this.translations,
  );

  provideExplanation(error: ErrorNameWithDescription): string {
    const appropriateProvider = this.findAppropriateProvider(error);
    return appropriateProvider.provideExplanation(error);
  }

  private findAppropriateProvider(
    error: ErrorNameWithDescription,
  ): IErrorExplanationProvider {
    const appropriateProvider = this.providers.find(
      (provider: IErrorExplanationProvider) => provider.canHandle(error),
    );
    if (appropriateProvider != null) {
      return appropriateProvider;
    }
    return this.defaultProvider;
  }
}

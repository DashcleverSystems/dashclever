import {
  ErrorNameWithDescription,
  IErrorExplanationProvider,
} from '@shared/commons/form-control/error-output/error-explanation-provider.interface';
import { TranslateService } from '@ngx-translate/core';

export class NoParamErrorExplanationProvider
  implements IErrorExplanationProvider
{
  constructor(private readonly translations: TranslateService) {}

  canHandle(error: ErrorNameWithDescription): boolean {
    return error.description == true;
  }

  provideExplanation(error: ErrorNameWithDescription): string {
    try {
      return this.translations.instant(`error.form.input.${error.name}`);
    } catch (_) {
      return this.translations.instant('error.form.input.default');
    }
  }
}

import {
  ErrorNameWithDescription,
  IErrorExplanationProvider,
} from '@shared/commons/form-control/error-output/error-explanation-provider.interface';
import { TranslateService } from '@ngx-translate/core';

export class DefaultErrorExplanationProvider
  implements IErrorExplanationProvider
{
  constructor(private readonly translations: TranslateService) {}

  canHandle(error: ErrorNameWithDescription): boolean {
    return true;
  }

  provideExplanation(error: ErrorNameWithDescription): string {
    return this.translations.instant('error.form.input.default');
  }
}

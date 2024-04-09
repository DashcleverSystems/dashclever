import {
  ErrorNameWithDescription,
  IErrorExplanationProvider,
} from '@shared/commons/form-control/error-output/error-explanation-provider.interface';
import { TranslateService } from '@ngx-translate/core';

export class MinMaxLengthErrorExplanationProvider
  implements IErrorExplanationProvider
{
  constructor(private readonly translations: TranslateService) {}

  canHandle(error: ErrorNameWithDescription): boolean {
    return error.name == 'minlength' || error.name == 'maxlength';
  }

  provideExplanation(error: ErrorNameWithDescription): string {
    const errorText = this.translations.instant(
      `error.form.input.${error.name}`,
    );
    const errorParam = error.description.requiredLength;
    return `${errorText} ${errorParam}`;
  }
}

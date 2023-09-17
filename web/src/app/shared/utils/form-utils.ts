import { FormControl, FormGroup } from '@angular/forms';

export type typedForm<T> = FormGroup<{
  [K in keyof T]: FormControl<T[K] | null>;
}>;

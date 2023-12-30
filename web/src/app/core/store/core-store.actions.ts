import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Language } from 'src/app/shared/enums/languages';

export const coreStoreActions = createActionGroup({
  source: 'Core',
  events: {
    'Change Language': props<{ lang: Language }>(),
    'Change App View': props<{ mobile: boolean }>(),
    'Change permissions': props<{ permissions: string[] }>(),
    fetchAccesses: emptyProps(),
    'Clear Core State': emptyProps(),
  },
});

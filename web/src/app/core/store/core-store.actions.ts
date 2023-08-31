import { createActionGroup, props } from '@ngrx/store';
import { Language } from 'src/app/shared/enums/languages';

export const coreStoreActions = createActionGroup({
  source: 'Core',
  events: {
    'Change Language': props<{ lang: Language }>(),
    'Change permissions': props<{ permissions: string[] }>(),
  },
});

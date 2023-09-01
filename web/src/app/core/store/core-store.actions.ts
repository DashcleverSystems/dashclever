import { ICredentials } from '@app/shared/models/user';
import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Language } from 'src/app/shared/enums/languages';

export const coreStoreActions = createActionGroup({
  source: 'Core',
  events: {
    'Change Language': props<{ lang: Language }>(),
    'Change permissions': props<{ permissions: string[] }>(),
    Login: props<{ credentials: ICredentials }>(),
    'Login successfully': props<{ credentials: ICredentials }>(),
    'Login fail': emptyProps(),
  },
});

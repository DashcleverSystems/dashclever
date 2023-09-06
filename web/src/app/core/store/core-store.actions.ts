import { IAccess } from '@app/shared/models/accesses';
import { ICredentials } from '@app/shared/models/user';
import { IWorkshop } from '@app/shared/models/workshop';
import { createActionGroup, emptyProps, props } from '@ngrx/store';
import { Language } from 'src/app/shared/enums/languages';

export const coreStoreActions = createActionGroup({
  source: 'Core',
  events: {
    'Change Language': props<{ lang: Language }>(),
    'Change permissions': props<{ permissions: string[] }>(),
    Login: props<{ credentials: ICredentials }>(),
    'Login successfully': props<{ logged: boolean }>(),
    'Login fail': emptyProps(),
    Logout: emptyProps(),
    'Logout successfully': emptyProps(),
    Unauthorized: emptyProps(),
    'Get workshops': emptyProps(),
    'Change Workshops': props<{ workshops: IWorkshop[] }>(),
    'Select Workshop': props<{workshop: IWorkshop | undefined}>(),
    'Select Access': props<{access: IAccess | undefined}>()
  },
});

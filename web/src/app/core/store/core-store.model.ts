import { MUser } from '@app/shared/models/user';
import { Language } from '@shared/enums/languages';

export interface ICoreStore {
  lang: Language;
  permissions: string[];
  user?: MUser;
}

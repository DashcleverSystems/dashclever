import { IUser } from '@app/shared/types/user';
import { Language } from '@shared/enums/languages';

export interface ICoreStore {
  lang: Language;
  permissions: string[];
  user?: IUser;
}

import { IAccess } from '@app/shared/models/accesses';
import { MUser } from '@app/shared/models/user';
import { IWorkshop } from '@app/shared/models/workshop';
import { Language } from '@shared/enums/languages';

export interface ICoreStore {
  lang: Language;
  permissions: string[];
  logged: boolean;
  workshops: IWorkshop[];
  selectedWorkshop: IWorkshop | undefined;
  selectedAccess: IAccess | undefined;
}

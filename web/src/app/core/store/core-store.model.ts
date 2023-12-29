import { IAccess } from '@app/shared/models/accesses';
import { IWorkshop } from '@app/shared/models/workshop';
import { Language } from '@shared/enums/languages';

export interface ICoreStore {
  lang: Language;
  mobile: boolean;
  workshops: IWorkshop[];
  selectedWorkshop: IWorkshop | undefined;
  selectedAccess: IAccess | undefined;
}

import { Type } from '@angular/core';
import { HomeComponent } from 'src/app/content/main/home/home.component';
import { InsightRepairPanelComponent } from 'src/app/content/main/panels/insight-repair-panel/insight-repair-panel.component';
import { ManagePanelComponent } from 'src/app/content/main/panels/manage-panel/manage-panel.component';
import { RepairProcessPanelComponent } from 'src/app/content/main/panels/repair-process-panel/repair-process-panel.component';
import { Panel } from 'src/app/shared/enums/panels';

export interface IShellRoute {
  url: string;
  name: string;
  permissions: string[];
  component?: Type<any>;
  iconName?: string;
  data?: any;
}

export const ShellRoutes: IShellRoute[] = [
  {
    url: 'home',
    name: 'Home',
    component: HomeComponent,
    iconName: 'home',
    permissions: [],
  },
  {
    url: 'manage',
    name: 'enum.Panel.' + Panel.MANAGE_STAFF,
    component: ManagePanelComponent,
    iconName: 'manage',
    permissions: [Panel.MANAGE_STAFF],
  },
  {
    url: 'insight-repair',
    name: 'enum.Panel.' + Panel.INSIGHT_REPAIR,
    component: InsightRepairPanelComponent,
    iconName: 'repair',
    permissions: [Panel.INSIGHT_REPAIR],
  },
  {
    url: 'repair-process',
    name: 'enum.Panel.' + Panel.REPAIR_PROCESS,
    component: RepairProcessPanelComponent,
    iconName: 'repair_process',
    permissions: [Panel.REPAIR_PROCESS],
  },
];

import { Type } from '@angular/core';
import { Panel } from 'src/app/shared/enums/panels';
import { AuthorizedGuard, PermissionGuard } from '../auth/permission.service';

export interface IShellRoute {
  url: string;
  name: string;
  permissions: string[];
  component?: Type<any>;
  iconName?: string;
  data?: any;
  canActivate?: any[];
  loadChildren?: () => any;
}

export const ShellRoutes: IShellRoute[] = [
  {
    url: 'home',
    name: 'Home',
    loadChildren: () =>
      import('@content/main/home/home.module').then((m) => m.HomeModule),
    iconName: 'home',
    permissions: [],
    canActivate: [AuthorizedGuard],
  },
  {
    url: 'insight-repair',
    name: 'enum.Panel.' + Panel.INSIGHT_REPAIR,
    loadChildren: () =>
      import(
        '@content/main/panels/insight-repair-panel/insight-repair-panel.module'
      ).then((m) => m.InsightRepairPanelModule),
    iconName: 'repair',
    permissions: [Panel.INSIGHT_REPAIR],
    canActivate: [AuthorizedGuard, PermissionGuard],
  },
  {
    url: 'repair-process',
    name: 'enum.Panel.' + Panel.REPAIR_PROCESS,
    loadChildren: () =>
      import(
        '@content/main/panels/repair-process-panel/repair-process-panel.module'
      ).then((m) => m.RepairProcessPanelModule),
    iconName: 'repair_process',
    permissions: [Panel.REPAIR_PROCESS],
    canActivate: [AuthorizedGuard, PermissionGuard],
  },
  {
    url: 'manage',
    name: 'enum.Panel.' + Panel.MANAGE_STAFF,
    loadChildren: () =>
      import('@content/main/panels/manage-panel/manage-panel.module').then(
        (m) => m.ManagePanelModule,
      ),
    iconName: 'manage',
    permissions: [Panel.MANAGE_STAFF],
    canActivate: [AuthorizedGuard, PermissionGuard],
  },
];

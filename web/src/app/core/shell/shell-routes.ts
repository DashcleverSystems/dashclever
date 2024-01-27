import { NgModule, Type } from '@angular/core';
import { HomeComponent } from 'src/app/content/main/home/home.component';
import { InsightRepairPanelComponent } from 'src/app/content/main/panels/insight-repair-panel/insight-repair-panel.component';
import { ManagePanelComponent } from 'src/app/content/main/panels/manage-panel/manage-panel.component';
import { RepairProcessPanelComponent } from 'src/app/content/main/panels/repair-process-panel/repair-process-panel.component';
import { Panel } from 'src/app/shared/enums/panels';
import { AuthorizedGuard, PermissionGuard } from '../auth/permission.service';

export interface IShellRoute {
  url: string;
  name: string;
  permissions: string[];
  moduleSupplier?: () => Promise<any>;
  iconName?: string;
  data?: any;
  canActivate?: any[];
}

export const ShellRoutes: IShellRoute[] = [
  {
    url: 'home',
    name: 'Home',
    moduleSupplier: () =>
      import('../../content/main/home/home.module').then((m) => m.HomeModule),
    iconName: 'home',
    permissions: [],
    canActivate: [AuthorizedGuard],
  },
  {
    url: 'manage',
    name: 'enum.Panel.' + Panel.MANAGE_STAFF,
    moduleSupplier: () =>
      import('../../content/main/panels/manage-panel/manage-panel.module').then(
        (m) => m.ManagePanelModule,
      ),
    iconName: 'manage',
    permissions: [Panel.MANAGE_STAFF],
    canActivate: [AuthorizedGuard, PermissionGuard],
  },
  {
    url: 'insight-repair',
    name: 'enum.Panel.' + Panel.INSIGHT_REPAIR,
    moduleSupplier: () =>
      import('../../content/main/home/home.module').then((m) => m.HomeModule),
    iconName: 'repair',
    permissions: [Panel.INSIGHT_REPAIR],
    canActivate: [AuthorizedGuard, PermissionGuard],
  },
  {
    url: 'repair-process',
    name: 'enum.Panel.' + Panel.REPAIR_PROCESS,
    moduleSupplier: () =>
      import('../../content/main/home/home.module').then((m) => m.HomeModule),
    iconName: 'repair_process',
    permissions: [Panel.REPAIR_PROCESS],
    canActivate: [AuthorizedGuard, PermissionGuard],
  },
];

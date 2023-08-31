import { Component, ComponentRef, Type } from '@angular/core';
import { HomeComponent } from 'src/app/content/main/home/home.component';
import { InsightRepairPanelComponent } from 'src/app/content/main/panels/insight-repair-panel/insight-repair-panel.component';
import { ManagePanelComponent } from 'src/app/content/main/panels/manage-panel/manage-panel.component';
import { RepairProcessPanelComponent } from 'src/app/content/main/panels/repair-process-panel/repair-process-panel.component';

export interface ShellRoute {
  url: string;
  name: string;
  permissions: string[];
  component?: Type<any>;
  iconName?: string;
  data?: any;
}

export const ShellRoutes: ShellRoute[] = [
  {
    url: 'home',
    name: 'Home',
    component: HomeComponent,
    iconName: 'home',
    permissions: [],
  },
  {
    url: 'manage',
    name: 'Manage',
    component: ManagePanelComponent,
    iconName: 'manage',
    permissions: [],
  },
  {
    url: 'insight-repair',
    name: 'Insight Repair',
    component: InsightRepairPanelComponent,
    iconName: 'repair',
    permissions: [],
  },
  {
    url: 'repair-process',
    name: 'Repair Process',
    component: RepairProcessPanelComponent,
    iconName: 'repair_process',
    permissions: [],
  },
];

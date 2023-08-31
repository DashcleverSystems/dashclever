import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShellComponent } from './shell.component';
import { ShellRoutes } from './shell-routes';
import { AuthGuard } from '../auth/permission.service';

const routes: Routes = [
  {
    path: '',
    component: ShellComponent,
    children: ShellRoutes.map((route) => {
      return {
        path: route.url,
        component: route.component,
        data: {
          ...route?.data,
          permissions: route.permissions,
        },
        canActivate: [AuthGuard],
      };
    }),
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ShellRoutingModule {}

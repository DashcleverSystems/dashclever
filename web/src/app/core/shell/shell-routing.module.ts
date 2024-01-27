import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShellComponent } from './shell.component';
import { ShellRoutes } from './shell-routes';

const routes: Routes = [
  {
    path: '',
    component: ShellComponent,
    children: ShellRoutes.map((route) => {
      return {
        path: route.url,
        loadChildren: route.moduleSupplier,
        data: {
          ...route?.data,
          permissions: route.permissions,
        },
        canActivate: route.canActivate,
      };
    }),
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ShellRoutingModule {}

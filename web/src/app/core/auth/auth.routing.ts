import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthComponent } from './component/auth.component';
import { AuthorizationIsNeeded } from './permission.service';

const routes: Routes = [
  {
    path: '',
    component: AuthComponent,
    canActivate: [AuthorizationIsNeeded],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AuthRoutingModule {}

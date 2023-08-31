import { NgModule } from '@angular/core';
import { AuthRoutingModule } from './auth.routing';
import { AuthComponent } from './component/auth.component';

@NgModule({
  declarations: [AuthComponent],
  exports: [],
  imports: [AuthRoutingModule],
})
export class AuthModule {}

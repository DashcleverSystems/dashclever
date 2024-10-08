import { NgModule } from '@angular/core';
import { AuthRoutingModule } from './auth.routing';
import { AuthComponent } from '@core/auth/login/auth.component';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '@app/shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { AuthService } from '@core/auth/login/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { CommonFormControlModule } from '@shared/commons/form-control/common-form-control.module';

@NgModule({
  declarations: [AuthComponent],
  exports: [],
  imports: [
    AuthRoutingModule,
    HttpClientModule,
    CommonModule,
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    TranslateModule,
    CommonFormControlModule,
  ],
  providers: [AuthService],
})
export class AuthModule {}

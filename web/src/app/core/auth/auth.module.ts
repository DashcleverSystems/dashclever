import { NgModule } from '@angular/core';
import { AuthRoutingModule } from './auth.routing';
import { AuthComponent } from '@core/auth/login/auth.component';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '@app/shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { AuthService } from '@core/auth/login/auth.service';
import { HttpClientModule } from '@angular/common/http';

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
  ],
  providers: [AuthService],
})
export class AuthModule {}

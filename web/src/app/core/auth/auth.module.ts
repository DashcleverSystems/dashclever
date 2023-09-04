import { NgModule } from '@angular/core';
import { AuthRoutingModule } from './auth.routing';
import { AuthComponent } from './component/auth.component';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '@app/shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { EffectsModule } from '@ngrx/effects';
import { AuthEffects } from './auth.effect';
import { AuthService } from './component/auth.service';
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
    EffectsModule.forFeature(AuthEffects),
  ],
  providers: [AuthService],
})
export class AuthModule {}

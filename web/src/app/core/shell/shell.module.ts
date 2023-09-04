import { NgModule } from '@angular/core';
import { ShellComponent } from './shell.component';
import { ShellRoutingModule } from './shell-routing.module';
import { SidePanelComponent } from './side-panel/side-panel.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { LoginCloudComponent } from './login-cloud/login-cloud.component';
@NgModule({
  imports: [ShellRoutingModule, SharedModule, TranslateModule],
  declarations: [ShellComponent, SidePanelComponent, LoginCloudComponent],
  providers: [],
})
export class ShellModule {}

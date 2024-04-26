import { NgModule } from '@angular/core';
import { ShellComponent } from './shell.component';
import { ShellRoutingModule } from './shell-routing.module';
import { SidePanelComponent } from './side-panel/side-panel.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { LogoutCloudComponent } from '@core/shell/logout-cloud/logout-cloud.component';

@NgModule({
  imports: [ShellRoutingModule, SharedModule, TranslateModule],
  declarations: [ShellComponent, SidePanelComponent, LogoutCloudComponent],
  providers: [],
})
export class ShellModule {}

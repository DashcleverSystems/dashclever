import { NgModule } from '@angular/core';
import { SettingsComponent } from '@content/main/settings/settings.component';
import { SettingRoutingModule } from '@content/main/settings/settings.routing';
import { SharedModule } from '@shared/shared.module';
import { AccessesModule } from '@content/main/home/accesses-selector/accesses-selector.module';
import { ThemeSwitchComponent } from '@content/main/settings/theme-switch/theme-switch.component';

@NgModule({
  declarations: [SettingsComponent, ThemeSwitchComponent],
  imports: [SettingRoutingModule, SharedModule, AccessesModule],
})
export class SettingsModule {}

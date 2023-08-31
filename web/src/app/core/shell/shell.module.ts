import { NgModule } from '@angular/core';
import { ShellComponent } from './shell.component';
import { ShellRoutingModule } from './shell-routing.module';
import { LanguageSelector } from './language-selector/language-selector.component';
import { SidePanelComponent } from './side-panel/side-panel.component';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  imports: [ShellRoutingModule, SharedModule],
  declarations: [ShellComponent, LanguageSelector, SidePanelComponent],
})
export class ShellModule {}

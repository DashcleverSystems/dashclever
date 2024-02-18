import { NgModule } from '@angular/core';
import { HomeComponent } from './main/home/home.component';
import { TranslateModule } from '@ngx-translate/core';
import { SharedModule } from '../shared/shared.module';
import { CoreModule } from '../core/core.module';
import { PanelsModule } from './main/panels/panels.module';

@NgModule({
  declarations: [HomeComponent],
  imports: [SharedModule, TranslateModule, CoreModule, PanelsModule],
  providers: [],
})
export class ContentModule {}

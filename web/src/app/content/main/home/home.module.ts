import { HomeComponent } from '@content/main/home/home.component';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { HomeRoutingModule } from '@content/main/home/home-routing.module';
import { CommonsModule } from '@shared/commons/commons.module';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  imports: [CommonsModule, TranslateModule, CommonModule, HomeRoutingModule],
  declarations: [HomeComponent],
})
export class HomeModule {}

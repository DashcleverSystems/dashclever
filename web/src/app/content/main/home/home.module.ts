import { NgModule } from '@angular/core';
import { AccessesModule } from './accesses-selector/accesses-selector.module';
import { HomeComponent } from './home.component';
import { WorkshopCreatorModule } from './workshop-creator/workshop-creator.module';
import { PrimeModule } from '@app/shared/prime.module';
import { SharedModule } from '@app/shared/shared.module';
import { HomeRoutingModule } from './home.routing';

@NgModule({
  declarations: [HomeComponent],
  imports: [
    SharedModule,
    PrimeModule,
    WorkshopCreatorModule,
    AccessesModule,
    HomeRoutingModule,
  ],
})
export class HomeModule {}

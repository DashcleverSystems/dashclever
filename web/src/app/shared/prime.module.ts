import { NgModule } from '@angular/core';

import { ButtonModule } from 'primeng/button';
import { SplitButtonModule } from 'primeng/splitbutton';
import { TooltipModule } from 'primeng/tooltip';
import { ToastModule } from 'primeng/toast';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { DynamicDialogModule } from 'primeng/dynamicdialog';
import { DialogModule } from 'primeng/dialog';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { DividerModule } from 'primeng/divider';
import { InputNumberModule } from 'primeng/inputnumber';
import { DropdownModule } from 'primeng/dropdown';
import { FileUploadModule } from 'primeng/fileupload';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
@NgModule({
  imports: [
    ButtonModule,
    SplitButtonModule,
    TooltipModule,
    ToastModule,
    InputTextModule,
    TableModule,
    DynamicDialogModule,
    DialogModule,
    OverlayPanelModule,
    DividerModule,
    InputNumberModule,
    DropdownModule,
    FileUploadModule,
    ProgressSpinnerModule,
  ],
  exports: [
    ButtonModule,
    SplitButtonModule,
    TooltipModule,
    ToastModule,
    InputTextModule,
    TableModule,
    DynamicDialogModule,
    DialogModule,
    OverlayPanelModule,
    DividerModule,
    InputNumberModule,
    DropdownModule,
    FileUploadModule,
    ProgressSpinnerModule,
  ],
})
export class PrimeModule {}

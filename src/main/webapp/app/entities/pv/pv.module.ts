import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PVComponent } from './list/pv.component';
import { PVDetailComponent } from './detail/pv-detail.component';
import { PVUpdateComponent } from './update/pv-update.component';
import { PVDeleteDialogComponent } from './delete/pv-delete-dialog.component';
import { PVRoutingModule } from './route/pv-routing.module';

@NgModule({
  imports: [SharedModule, PVRoutingModule],
  declarations: [PVComponent, PVDetailComponent, PVUpdateComponent, PVDeleteDialogComponent],
  entryComponents: [PVDeleteDialogComponent],
})
export class PVModule {}

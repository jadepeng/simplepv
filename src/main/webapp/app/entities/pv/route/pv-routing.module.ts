import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PVComponent } from '../list/pv.component';
import { PVDetailComponent } from '../detail/pv-detail.component';
import { PVUpdateComponent } from '../update/pv-update.component';
import { PVRoutingResolveService } from './pv-routing-resolve.service';

const pVRoute: Routes = [
  {
    path: '',
    component: PVComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PVDetailComponent,
    resolve: {
      pV: PVRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PVUpdateComponent,
    resolve: {
      pV: PVRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PVUpdateComponent,
    resolve: {
      pV: PVRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pVRoute)],
  exports: [RouterModule],
})
export class PVRoutingModule {}

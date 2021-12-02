import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pv',
        data: { pageTitle: 'simplepvApp.pV.home.title' },
        loadChildren: () => import('./pv/pv.module').then(m => m.PVModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

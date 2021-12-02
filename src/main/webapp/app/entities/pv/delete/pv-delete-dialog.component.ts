import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPV } from '../pv.model';
import { PVService } from '../service/pv.service';

@Component({
  templateUrl: './pv-delete-dialog.component.html',
})
export class PVDeleteDialogComponent {
  pV?: IPV;

  constructor(protected pVService: PVService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pVService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

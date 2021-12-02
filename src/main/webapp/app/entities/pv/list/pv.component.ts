import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPV } from '../pv.model';
import { PVService } from '../service/pv.service';
import { PVDeleteDialogComponent } from '../delete/pv-delete-dialog.component';

@Component({
  selector: 'jhi-pv',
  templateUrl: './pv.component.html',
})
export class PVComponent implements OnInit {
  pVS?: IPV[];
  isLoading = false;

  constructor(protected pVService: PVService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pVService.query().subscribe(
      (res: HttpResponse<IPV[]>) => {
        this.isLoading = false;
        this.pVS = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPV): number {
    return item.id!;
  }

  delete(pV: IPV): void {
    const modalRef = this.modalService.open(PVDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pV = pV;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

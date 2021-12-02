import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPV } from '../pv.model';

@Component({
  selector: 'jhi-pv-detail',
  templateUrl: './pv-detail.component.html',
})
export class PVDetailComponent implements OnInit {
  pV: IPV | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pV }) => {
      this.pV = pV;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

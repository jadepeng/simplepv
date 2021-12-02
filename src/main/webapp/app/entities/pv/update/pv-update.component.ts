import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPV, PV } from '../pv.model';
import { PVService } from '../service/pv.service';

@Component({
  selector: 'jhi-pv-update',
  templateUrl: './pv-update.component.html',
})
export class PVUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    host: [],
    url: [],
    pv: [],
  });

  constructor(protected pVService: PVService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pV }) => {
      this.updateForm(pV);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pV = this.createFromForm();
    if (pV.id !== undefined) {
      this.subscribeToSaveResponse(this.pVService.update(pV));
    } else {
      this.subscribeToSaveResponse(this.pVService.create(pV));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPV>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pV: IPV): void {
    this.editForm.patchValue({
      id: pV.id,
      host: pV.host,
      url: pV.url,
      pv: pV.pv,
    });
  }

  protected createFromForm(): IPV {
    return {
      ...new PV(),
      id: this.editForm.get(['id'])!.value,
      host: this.editForm.get(['host'])!.value,
      url: this.editForm.get(['url'])!.value,
      pv: this.editForm.get(['pv'])!.value,
    };
  }
}

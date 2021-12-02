jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PVService } from '../service/pv.service';
import { IPV, PV } from '../pv.model';

import { PVUpdateComponent } from './pv-update.component';

describe('PV Management Update Component', () => {
  let comp: PVUpdateComponent;
  let fixture: ComponentFixture<PVUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pVService: PVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PVUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PVUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PVUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pVService = TestBed.inject(PVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pV: IPV = { id: 456 };

      activatedRoute.data = of({ pV });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(pV));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PV>>();
      const pV = { id: 123 };
      jest.spyOn(pVService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pV });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pV }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pVService.update).toHaveBeenCalledWith(pV);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PV>>();
      const pV = new PV();
      jest.spyOn(pVService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pV });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pV }));
      saveSubject.complete();

      // THEN
      expect(pVService.create).toHaveBeenCalledWith(pV);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PV>>();
      const pV = { id: 123 };
      jest.spyOn(pVService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pV });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pVService.update).toHaveBeenCalledWith(pV);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

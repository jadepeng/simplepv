import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PVDetailComponent } from './pv-detail.component';

describe('PV Management Detail Component', () => {
  let comp: PVDetailComponent;
  let fixture: ComponentFixture<PVDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PVDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pV: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PVDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PVDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pV on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pV).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

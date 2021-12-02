import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PVService } from '../service/pv.service';

import { PVComponent } from './pv.component';

describe('PV Management Component', () => {
  let comp: PVComponent;
  let fixture: ComponentFixture<PVComponent>;
  let service: PVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PVComponent],
    })
      .overrideTemplate(PVComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PVComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PVService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.pVS?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});

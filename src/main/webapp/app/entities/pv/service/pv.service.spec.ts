import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPV, PV } from '../pv.model';

import { PVService } from './pv.service';

describe('PV Service', () => {
  let service: PVService;
  let httpMock: HttpTestingController;
  let elemDefault: IPV;
  let expectedResult: IPV | IPV[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PVService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      host: 'AAAAAAA',
      url: 'AAAAAAA',
      pv: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PV', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PV()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PV', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          host: 'BBBBBB',
          url: 'BBBBBB',
          pv: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PV', () => {
      const patchObject = Object.assign({}, new PV());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PV', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          host: 'BBBBBB',
          url: 'BBBBBB',
          pv: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PV', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPVToCollectionIfMissing', () => {
      it('should add a PV to an empty array', () => {
        const pV: IPV = { id: 123 };
        expectedResult = service.addPVToCollectionIfMissing([], pV);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pV);
      });

      it('should not add a PV to an array that contains it', () => {
        const pV: IPV = { id: 123 };
        const pVCollection: IPV[] = [
          {
            ...pV,
          },
          { id: 456 },
        ];
        expectedResult = service.addPVToCollectionIfMissing(pVCollection, pV);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PV to an array that doesn't contain it", () => {
        const pV: IPV = { id: 123 };
        const pVCollection: IPV[] = [{ id: 456 }];
        expectedResult = service.addPVToCollectionIfMissing(pVCollection, pV);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pV);
      });

      it('should add only unique PV to an array', () => {
        const pVArray: IPV[] = [{ id: 123 }, { id: 456 }, { id: 74163 }];
        const pVCollection: IPV[] = [{ id: 123 }];
        expectedResult = service.addPVToCollectionIfMissing(pVCollection, ...pVArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pV: IPV = { id: 123 };
        const pV2: IPV = { id: 456 };
        expectedResult = service.addPVToCollectionIfMissing([], pV, pV2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pV);
        expect(expectedResult).toContain(pV2);
      });

      it('should accept null and undefined values', () => {
        const pV: IPV = { id: 123 };
        expectedResult = service.addPVToCollectionIfMissing([], null, pV, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pV);
      });

      it('should return initial array if no PV is added', () => {
        const pVCollection: IPV[] = [{ id: 123 }];
        expectedResult = service.addPVToCollectionIfMissing(pVCollection, undefined, null);
        expect(expectedResult).toEqual(pVCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

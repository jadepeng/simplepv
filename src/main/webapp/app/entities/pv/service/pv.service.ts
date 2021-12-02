import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPV, getPVIdentifier } from '../pv.model';

export type EntityResponseType = HttpResponse<IPV>;
export type EntityArrayResponseType = HttpResponse<IPV[]>;

@Injectable({ providedIn: 'root' })
export class PVService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pvs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pV: IPV): Observable<EntityResponseType> {
    return this.http.post<IPV>(this.resourceUrl, pV, { observe: 'response' });
  }

  update(pV: IPV): Observable<EntityResponseType> {
    return this.http.put<IPV>(`${this.resourceUrl}/${getPVIdentifier(pV) as number}`, pV, { observe: 'response' });
  }

  partialUpdate(pV: IPV): Observable<EntityResponseType> {
    return this.http.patch<IPV>(`${this.resourceUrl}/${getPVIdentifier(pV) as number}`, pV, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPV>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPV[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPVToCollectionIfMissing(pVCollection: IPV[], ...pVSToCheck: (IPV | null | undefined)[]): IPV[] {
    const pVS: IPV[] = pVSToCheck.filter(isPresent);
    if (pVS.length > 0) {
      const pVCollectionIdentifiers = pVCollection.map(pVItem => getPVIdentifier(pVItem)!);
      const pVSToAdd = pVS.filter(pVItem => {
        const pVIdentifier = getPVIdentifier(pVItem);
        if (pVIdentifier == null || pVCollectionIdentifiers.includes(pVIdentifier)) {
          return false;
        }
        pVCollectionIdentifiers.push(pVIdentifier);
        return true;
      });
      return [...pVSToAdd, ...pVCollection];
    }
    return pVCollection;
  }
}

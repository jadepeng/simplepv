import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPV, PV } from '../pv.model';
import { PVService } from '../service/pv.service';

@Injectable({ providedIn: 'root' })
export class PVRoutingResolveService implements Resolve<IPV> {
  constructor(protected service: PVService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPV> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pV: HttpResponse<PV>) => {
          if (pV.body) {
            return of(pV.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PV());
  }
}

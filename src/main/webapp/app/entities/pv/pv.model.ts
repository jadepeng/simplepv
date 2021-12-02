export interface IPV {
  id?: number;
  host?: string | null;
  url?: string | null;
  pv?: number | null;
}

export class PV implements IPV {
  constructor(public id?: number, public host?: string | null, public url?: string | null, public pv?: number | null) {}
}

export function getPVIdentifier(pV: IPV): number | undefined {
  return pV.id;
}

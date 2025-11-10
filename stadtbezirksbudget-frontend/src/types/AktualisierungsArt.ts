export enum AktualisierungsArt {
  FACHANWENDUNG = "FACHANWENDUNG",
  E_AKTE = "E_AKTE",
  ZAMMAD = "ZAMMAD",
}

export const AktualisierungsArtText: Record<AktualisierungsArt, string> = {
  [AktualisierungsArt.FACHANWENDUNG]: "Fachanwendung",
  [AktualisierungsArt.E_AKTE]: "E-Akte",
  [AktualisierungsArt.ZAMMAD]: "Zammad",
};
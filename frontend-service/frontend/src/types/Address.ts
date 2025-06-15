export interface Address {
    id: number;
    fullName: string;
    telephoneNumber: number;
    addressFirstLine: string;
    addressSecondLine: string;
    postalCode: string;
    city: string;
    province: string;
    country: string;
    vatId: string;
    defaultAddress: boolean;
  }
import { persistentAtom } from '@nanostores/persistent';

export interface SelectedAddresses {
  deliveryAddressId: number | null;
  billingAddressId: number | null;
}

export const selectedAddressesStore = persistentAtom<SelectedAddresses>(
  'selectedAddresses:',
  { deliveryAddressId: null, billingAddressId: null },
  {
    encode: JSON.stringify,
    decode: JSON.parse,
  },
);
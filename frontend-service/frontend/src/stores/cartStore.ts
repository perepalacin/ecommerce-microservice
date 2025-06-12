import { persistentAtom } from '@nanostores/persistent'
import type { Product } from '../types/Product'

export const cartStore = persistentAtom<Product[]>('cart:', [], {
  encode: JSON.stringify,
  decode: JSON.parse,
})
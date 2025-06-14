import { persistentAtom } from '@nanostores/persistent'
import type { CartItem } from '../types/CartItem'

export const cartStore = persistentAtom<CartItem[]>('cart:', [], {
  encode: JSON.stringify,
  decode: JSON.parse,
})

export function removeCartItem () {

}
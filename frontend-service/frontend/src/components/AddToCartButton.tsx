import { useState } from "preact/hooks";
import { cartStore } from "../stores/cartStore";
import { apiDeleteRequest, apiPostRequest } from "../utils/api-request";
import type { CartItem } from "../types/CartItem";

interface Props {
    productId: number;
    stock: number;
}

export default function AddToCartButton ({productId, stock}: Props) {

    const [productFromCart, setProductFromCart] = useState<CartItem | undefined>(cartStore.get().find((prod) => prod.productId === productId));
    
    async function handleClick(e: any) {
        e.stopPropagation();
        if (!productFromCart) {
            try {
                const url = `/api/v1/carts?productId=${productId}`;
                
                const jsonResponse = await apiPostRequest(url, {});
                
                cartStore.set([...jsonResponse.items]);
                setProductFromCart(jsonResponse.items.find((prod: CartItem) => prod.productId === productId));
                
            } catch (error) {
                console.error("Error adding this this item to the cart:", error);
            }
        } else {
            try {
                const url = `/api/v1/carts?cartItemId=${productFromCart.id}`;
                const jsonResponse = await apiDeleteRequest(url);
                cartStore.set([...cartStore.get().filter((items) => items.id !== productFromCart.id)]);
                setProductFromCart(undefined);
            } catch (error) {
                console.error("Error removing the item form the cart:", error);
            }
        }
    }

    return (
        <button class="btn main-btn text-bold" onClick={handleClick} disabled={stock <= 0 && !productFromCart}>
            {
                productFromCart 
                ?
                <>
                <img src="/icons/x.svg" width="16" height="16" loading="lazy"/>
                Remove from cart
                </>
                :
                <>
                    <img src="/icons/cart.svg" width="15" height="15" loading="lazy"/>
                    Add to cart
                </>
            }
        </button>
    )
}

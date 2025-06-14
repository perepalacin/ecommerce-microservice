import { useEffect, useRef, useState } from "preact/hooks";
import { cartStore } from "../stores/cartStore";
import { apiDeleteRequest } from "../utils/api-request";
import { priceFormatter } from "../utils/formatters";
import Popover from "./ui-generics/Popver";
import type { CartItem } from "../types/CartItem";

export function CartButton () {
    
    const [cartItems, setCartItems] = useState<CartItem[]>(cartStore.get() || []);
    const [isCartPopoverOpen, setIsCartPopoverOpen] = useState(false);
    const rerender = useRef<number>(0);

    async function handleRemoveCartItem (cartItemId: number) {
        try {
            const url = `/api/v1/carts?cartItemId=${cartItemId}`;
            const jsonResponse = await apiDeleteRequest(url);
            cartStore.set([...cartStore.get().filter((items) => items.id !== cartItemId)]);
        } catch (error) {
            console.error("Error removing the item form the cart:", error);
        }
    }

    async function handleDeleteUserCart () {
        try {
            const url = `/api/v1/carts/all`;
            const jsonResponse = await apiDeleteRequest(url);
            cartStore.set([]);
        } catch (error) {
            console.error("Error removing the item form the cart:", error);
        }
    }

    function autocloseCart ( ) {
        setTimeout(() => {
            setIsCartPopoverOpen(false);
        }, 3000);
    }

    useEffect(() => {
        const unsubscribe = cartStore.subscribe((newCart) => {
            setCartItems([...newCart]);
            if (newCart.length > 0 && !isCartPopoverOpen && rerender.current > 0 && window.location.pathname.includes("/products")) {
                setIsCartPopoverOpen(true);
                autocloseCart();
            } else if (newCart.length === 0 && isCartPopoverOpen) {
                setIsCartPopoverOpen(false);
            }
            rerender.current = rerender.current + 1;
        });
        setCartItems(cartStore.get() || []);
        return () => unsubscribe();
    }, []);

    return (
        <Popover
        placement="bottom-end"
        trigger={
            <button class="icon-btn cart-button"><img src="/icons/cart.svg" width="20" height="20" loading="lazy"/></button>
        }
        isOpen={isCartPopoverOpen}
        onOpenChange={setIsCartPopoverOpen}
        >
        {
            cartStore.get().length === 0 ?
            <p class="px-2">You have no items in the cart</p>
            :
            <>
            <ul class="dropdown">
                {cartItems.map((item) =>
                    <li class="cart-item">
                        <img src="/images/prx.png" loading="lazy" class={item.stock < item.quantity ? "disabled-image" : ""} width={35} height={35}/>
                        <a href={"/products/" + item.publicUrl} class="w-100" aria-label="Page of the product">
                        <div class="flex-col justify-start gap-05">
                            <p>{item.brand} - {item.name}</p>
                            <span>Quantity: {item.quantity} {item.stock < item.quantity ? `. Item out of stock! Only ${item.stock} units left` : ""}</span>
                        </div>
                        </a>
                        <p class="text-right">{priceFormatter(item.price*item.quantity)} €</p>
                        <button class="icon-btn outline-btn" onClick={() => handleRemoveCartItem(item.id)}>
                            <img src="/icons/trash.svg" width={12} height={12} loading="lazy" />
                        </button>
                    </li>
                )}
            </ul>
            <div class="flex-row gap-05 w-100 justify-center mt-1">
                <button class="btn outline-btn" onClick={handleDeleteUserCart}>
                        Clear items
                </button>
                <a href="/checkout" aria-label="Checkout page">
                    <button class="btn main-btn">
                        Go to checkout
                    </button>
                </a>
            </div>
            </>
        }
        </Popover>
    );
}
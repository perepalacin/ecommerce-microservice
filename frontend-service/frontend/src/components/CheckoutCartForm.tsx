import { useEffect, useState, useRef } from "preact/hooks";
import { Suspense } from "preact/compat";
import type { CartItem } from "../types/CartItem";
import { priceFormatter } from "../utils/formatters";
import { apiDeleteRequest, apiGetRequest, apiPatchRequest, apiPostRequest } from "../utils/api-request";
import { cartStore } from "../stores/cartStore";

export function CheckoutCartFrom () {

    const [cartItems, setCartItems] = useState<CartItem[]>(cartStore.get() || []);
    const debounceTimeoutRef = useRef<number | null>(null);

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

    async function fetchCartItems() {
        try {
            const url = '/api/v1/carts';
            const jsonResponse = await apiGetRequest(url);
            cartStore.set([...jsonResponse.items]);
            setCartItems([...jsonResponse.items]);
                
        } catch (error) {
            console.error("Error adding this this item to the cart:", error);
        }
    }

    async function handleQuantityChange (cartItemId: number, newQuantity: number) {
        if (newQuantity <= 0) {
            newQuantity = 1;
        }
        const newItems = [...cartItems];
        newItems.forEach((item) => {
            if (item.id === cartItemId) {
                item.quantity = newQuantity;
            }
        });
        setCartItems(newItems);
        if (debounceTimeoutRef.current) {
            clearTimeout(debounceTimeoutRef.current);
        }

        debounceTimeoutRef.current = window.setTimeout(async () => {
            try {
                await apiPatchRequest(`/api/v1/carts/quantity?cartItemId=${cartItemId}&quantity=${newQuantity}`, {});
            } catch (error) {
                console.error("Error updating cart quantities: ", error);
            }
        }, 300);
    }

    useEffect(() => {
        fetchCartItems();
        const unsubscribe = cartStore.subscribe((newCart) => {
            setCartItems([...newCart]);
        });
        return () => unsubscribe();
    }, []);

    // if (cartItems.length === 0) {
    //     return (
    //         <p class="px-2">You have no items in the cart</p>
    //     );
    // }

    return (
        <Suspense fallback={<p>Loading checkout form...</p>}>
        <>
            <ul class="dropdown w-75">
                {cartItems.map((item) =>
                    <li class="cart-item">
                        <img src="/images/prx.png" loading="lazy" class={item.stock < item.quantity ? "disabled-image" : ""} width={60} height={60}/>
                        <a href={"/products/" + item.publicUrl} class="w-100" aria-label="Page of the product">
                        <div class="flex-col justify-start gap-05">
                            <p>{item.brand} - {item.name}</p>
                            <span>Quantity: {item.quantity} {item.stock < item.quantity ? `. Item out of stock! Only ${item.stock} units left` : ""}</span>
                        </div>
                        </a>
                        <p>{priceFormatter(item.price*item.quantity)} €</p>
                        <input class="text-center" style={{width: '80px'}} value={item.quantity} type="number" placeholder="1" required onChange={(e) => {handleQuantityChange(item.id, Number(e.currentTarget.value))}}/>
                        <button class="icon-btn outline-btn" onClick={() => handleRemoveCartItem(item.id)}>
                            <img src="/icons/trash.svg" width={12} height={12} loading="lazy" />
                        </button>
                    </li>
                )}
                <hr />
            <h2 class="text-right">Total price: {priceFormatter(cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0))} €</h2>
            <div class="flex-row gap-05 w-100 justify-end mt-1">
                <button class="btn outline-btn" onClick={handleDeleteUserCart} >
                        Clear items
                        {/* //TODO: disabled button colors!? */}
                </button>
                <a href="/checkout" aria-label="Checkout page">
                    <button class="btn main-btn" disabled={cartItems.find((item) => item.quantity > item.stock) ? true : false}>
                        Continue
                    </button>
                </a>
            </div>
            </ul>
        </>
        </Suspense>
    );
}
import { authStore } from "../stores/authStore";
import { cartStore } from "../stores/cartStore";

interface Props {
    productId: number;
}

export default function AddToCartButton ({productId}: Props) {

    const isProductInCart = cartStore.get().find((prod) => prod.id === productId);
    
    async function handleClick (e: any) {
        e.preventDefault();
        e.stopPropagation();
        if (authStore.get().authenticated) {
            try {
                const response = await fetch("http://localhost:8090/api/v1/carts?productId=" + productId, {
                    method: "POST",
                    headers: {
                    'Authorization': `Bearer ${authStore.get().accessToken}`,
                    'Content-Type': 'application/json'
                    }
                });
                if (response.status === 401) {
                    
                }
                if (!response.ok) {
                    throw new Error(`Response status: ${response.status}`);
                }
                const json = await response.json();
                console.log(json);
                // cartStore.set([...cartStore.get()])

            } catch (error) {
                console.error(error);
            }
        } else {
            window.location.replace("/signin");
        }
         
    }


    return (
        <button class="btn main-btn text-bold" onClick={handleClick}>
            {
                isProductInCart 
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

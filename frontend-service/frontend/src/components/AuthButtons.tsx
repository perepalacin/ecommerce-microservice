import { authStore } from "../stores/authStore";
import Popover from "./ui-generics/Popver";
import PopOver from "./ui-generics/Popver";

interface Props {
    isAuthenticated: boolean;
}

export function AuthButtons({isAuthenticated}: Props) {

    return (
        <div class="flex-row gap-1 align-center gap-05">
        {isAuthenticated ?
            <>
            <Popover
                placement="bottom-end"
                trigger={
                    <button class="icon-btn cart-button"><img src="/icons/cart.svg" width="20" height="20" loading="lazy"/></button>
                }
            >
                <p>Pop over text</p>
            </Popover>
            <Popover
                placement="bottom-end"
                trigger={
                    <button class="icon-btn cart-button"><img src="/icons/user.svg" width="20" height="20" loading="lazy"/></button>
                }
            >
                <div class="flex-col gap-05">
                    <p>Your profile</p>
                    <p>Your Addresses</p>
                    <p>Your Orders</p>
                </div>
            </Popover>
            </>
        :
            <>
                <a href="/auth/signin">
                    <button class="btn outline-btn">
                        Sign in
                    </button>
                </a>
                <a href="/auth/signup">
                    <button class="btn main-btn">
                        Sign up
                    </button>
                </a>
            </>
        }
        </div>
    );
}

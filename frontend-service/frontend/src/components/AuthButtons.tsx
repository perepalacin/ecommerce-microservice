import { apiPostRequest } from "../utils/api-request";
import { CartButton } from "./CartButton";
import Popover from "./ui-generics/Popver";
import PopOver from "./ui-generics/Popver";

interface Props {
    isAuthenticated: boolean;
}

export function AuthButtons({isAuthenticated}: Props) {

    async function handleLogout () {
        try {
            const url = '/api/v1/auth/logout';
            const jsonResponse = await apiPostRequest(url, {});
            window.location.replace("/auth/signin")
        } catch (error) {
            console.error("Error logging out: ", error);
        }
    }

    return (
        <div class="flex-row gap-1 align-center gap-05">
        {isAuthenticated ?
            <>
            <CartButton />
            <Popover
                placement="bottom-end"
                trigger={
                    <button class="icon-btn cart-button"><img src="/icons/user.svg" width="20" height="20" loading="lazy"/></button>
                }
            >
                <ul class="dropdown">
                    <li>
                        <a href="/profile" aria-label="Page to edit your profile">
                            <p>Your profile</p>
                        </a>
                    </li>
                    <li>
                        <a href="/addresses" aria-label="Page to add or edit your addresses">
                            <p>Your Addresses</p>
                        </a>
                    </li>
                    <li>
                        <a href="/orders" aria-label="Page to view or edit your previous orders">
                            <p>Your Orders</p>
                        </a>
                    </li>
                    <li onClick={handleLogout}>
                        <p>Logout</p>
                    </li>
                </ul>
            </Popover>
            </>
        :
            <>
                <a href="/auth/signin" aria-label="Login page">
                    <button class="btn outline-btn">
                        Sign in
                    </button>
                </a>
                <a href="/auth/signup" aria-label="Sign up page">
                    <button class="btn main-btn">
                        Sign up
                    </button>
                </a>
            </>
        }
        </div>
    );
}

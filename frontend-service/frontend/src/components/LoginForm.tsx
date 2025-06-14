import { useState } from "preact/hooks";
import type { AuthAction } from "../pages/auth/[action]/index.astro";
import { handleLoginUser, handleRegisterUser } from "../utils/auth-related-functions";

interface Props {
    pageContent: AuthAction;
}

export default function LoginForm ({pageContent}: Props) {

    const [errorMessage, setErrorMessage] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [registerMessage, setRegisterMessage] = useState("");

    async function handleSubmit (e: SubmitEvent) {
        if (isLoading) {
            return;
        }
        e.preventDefault();
        setIsLoading(true);
        const formData = new FormData(e.target as HTMLFormElement);
        if (pageContent.action === "signin") {
            let response = await handleLoginUser(formData);
            if (response.result === "ERROR") {
                setErrorMessage(response.message);
            } else {
                setIsLoading(false);
                window.location.replace("/products");
            }
        } else if (pageContent.action === "signup") {
            let response = await handleRegisterUser(formData);
            if (response.result === "ERROR") {
                setErrorMessage(response.message);
            } else {
                setRegisterMessage("You have been registered successfully, check your email to validate your account.")
            }
        }
        setIsLoading(false);
    }
    return (
        <>
        {
            registerMessage ?
            <p style={{color: "green"}} class="text-center text-bold fs-1-25">{registerMessage}</p>
            :
            <>
            <h1 class="mb-1">{pageContent.title}</h1>
            <form class="flex-col gap-05 align-center w-50" onSubmit={handleSubmit}>
                <p>{pageContent.desc}</p>
                {errorMessage && <p style={{color: "red"}} class="text-center text-bold fs-1-25">{errorMessage}</p>}
                {pageContent.action === "signup" &&
                <>
                    <input type="text" id="firstName" name="firstName" placeholder="First Name" required/>
                    <input type="text" id="lastName" name="lastName" placeholder="Last Name" required/>
                </>
                }
                <input type="text" id="email" name="email" placeholder="email" required/>
                <input type="password" id="password" name="password" placeholder="password" required/>
                <button disabled={isLoading} type="submit" class="w-100 btn main-btn text-bold">{pageContent.title}</button>
                <p>{pageContent.extra} <a href={pageContent.redirect} aria-label={pageContent.redirectTitle + "page"}>{pageContent.redirectTitle}</a></p>
            </form>
            </>
        }
        </>
    )
}
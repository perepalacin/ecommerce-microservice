import type { AuthAction } from "../pages/auth/[action]/index.astro";
import { authStore } from "../stores/authStore";
import { handleLoginUser } from "../utils/auth-related-functions";

interface Props {
    pageContent: AuthAction;
}

export default function LoginForm ({pageContent}: Props) {

    // TODO: Sign up form, better feedback for the users like password is incorrect and such! 

    if (authStore.get().authenticated) {
      window.location.replace("/products");
      return;
    }

    async function handleSubmit (e: SubmitEvent) {
        e.preventDefault();
        const formData = new FormData(e.target as HTMLFormElement);
        if (pageContent.action === "signin") {
            await handleLoginUser(formData);
        } else if (pageContent.action === "signup") {
            
        }
    }
    return (
        <>
            <h1 class="mb-1">{pageContent.title}</h1>
            <form class="flex-col gap-05 align-center" onSubmit={handleSubmit}>
                <p>{pageContent.desc}</p>
                <input type="text" id="email" name="email" placeholder="email" required/>
                <input type="password" id="password" name="password" placeholder="password" required/>
                <button type="submit" class="w-100 btn main-btn text-bold">{pageContent.title}</button>
                <p>{pageContent.extra} <a href={pageContent.redirect}>{pageContent.redirectTitle}</a></p>
            </form>
        </>
    )
}
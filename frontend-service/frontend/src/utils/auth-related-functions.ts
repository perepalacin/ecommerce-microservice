import { authStore } from "../stores/authStore";

export async function handleLoginUser(request: FormData): Promise<string> {

    if (!request.get("email") || !request.get("password")) {
        return "Email or password is missing";
    }

    try {
      const response = await fetch("http://localhost:8090/api/v1/auth/sign-in", {
        method: "POST",
        credentials: "include",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ 
          email: request.get("email"),
          password: request.get("password")
        })
      });
      if (!response.ok) {
        throw new Error(`Response status: ${response.status}`);
      }
      window.location.replace("/products");
      return "Login Successfully";
    } catch (error: any) {
      console.error(error.message);
      return "There was an error while logging in, please try again later.";
    }
  }

  export async function handleRequestRefreshToken(): Promise<void> {

    try {
      const response = await fetch("http://localhost:8090/api/v1/auth/refresh-token/" + authStore.get().refreshToken, {
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        }
      });
      if (!response.ok) {
        window.location.replace("/signin");
        return;
      }
      const json = await response.json();

      authStore.setKey('accessToken', json.access_token);
      authStore.setKey('expiresIn', json.expires_in);
      authStore.setKey('refreshExpiresIn', json.refresh_expires_in);
      authStore.setKey('refreshToken', json.refresh_token);
      authStore.setKey('authenticated', true);
      return;
    } catch (error: any) {
      console.error(error.message);
      window.location.replace("/signin");
      return;
    }
  }
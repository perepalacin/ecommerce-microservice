export let isRefreshingPromise: Promise<Response> | null = null;
const failedRequestSubscribers: Array<(accessToken: string) => void> = [];

export async function handleLoginUser(request: FormData): Promise<{result: "SUCCESS" | "ERROR"; message: string }> {
    if (!request.get("email") || !request.get("password")) {
        return {result: "ERROR", message: "Email or password is missing"};
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
        console.log(response);
        const message = await response.text();
        throw new Error(`${message}`);
      }
      return {result: "SUCCESS", message: "User logged successfully"};
    } catch (error: any) {
      return {result: "ERROR", message: error.message};
    }
  }

  export async function handleRegisterUser(request: FormData): Promise<{result: "SUCCESS" | "ERROR"; message: string }> {
    if (!request.get("email") || !request.get("password") || !request.get("firstName") || !request.get("lastName")) {
        return {result: "ERROR", message: "Please, fill all the fields in the form"};
    }

    try {
      const response = await fetch("http://localhost:8090/api/v1/auth/sign-up", {
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ 
          firstName: request.get("firstName"),
          lastName: request.get("lastName"),
          email: request.get("email"),
          password: request.get("password")
        })
      });
      if (!response.ok) {
        console.log(response);
        const message = await response.text();
        throw new Error(`${message}`);
      }
      return {result: "SUCCESS", message: "User registered successfully"};
    } catch (error: any) {
      return {result: "ERROR", message: error.message};
    }
  }

export async function handleRequestRefreshToken(): Promise<void> {
  if (isRefreshingPromise) {
      await isRefreshingPromise;
      return;
  }

  try {
      const headers: HeadersInit = {};

      isRefreshingPromise = fetch('http://localhost:8090/api/v1/auth/refresh-token', {
          method: 'POST',
          headers: headers,
          credentials: 'include', 
      });

      const response = await isRefreshingPromise;

      if (!response.ok) {
          const errorText = await response.text();
          console.error("Refresh token request failed:", response.status, errorText);
          
          if (typeof window !== "undefined") {
              window.location.replace("/auth/signin");
          }
          throw new Error(`Refresh token failed: ${response.status} ${errorText}`);
      }

      console.log("Token refreshed successfully.");

  } catch (error: any) {
      console.error("Error during token refresh:", error.message);
      if (typeof window !== "undefined") {
          window.location.replace("/auth/signin");
      }
      throw error; 
  } finally {
      isRefreshingPromise = null;
  }
}

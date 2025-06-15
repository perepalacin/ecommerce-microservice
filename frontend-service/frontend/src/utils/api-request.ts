import { handleRequestRefreshToken, isRefreshingPromise } from "./auth-related-functions";

async function apiFetchWrapper<T = any>(
  { url, config = {}, retries = 0 }: 
  { url: string, config: RequestInit, retries?: number}
): Promise<T> {

  const maxRetries = 1; 
  const fullUrl = "http://localhost:8090" + url;

  try {
      const headers = new Headers(config.headers || {});

      if (config.method && ['POST', 'PUT', 'PATCH'].includes(config.method.toUpperCase()) && !headers.has('Content-Type')) {
          headers.set('Content-Type', 'application/json');
      }

      const updatedConfig: RequestInit = {
          ...config,
          headers: headers,
          credentials: 'include' 
      };

      const response = await fetch(fullUrl, updatedConfig);
      if (response.status === 401) {
          if (retries < maxRetries) {
              console.warn(`Request to ${fullUrl} received 401. Attempting token refresh (Retry ${retries + 1}).`);

              if (isRefreshingPromise) {
                  await isRefreshingPromise;
              } else {
                  await handleRequestRefreshToken();
              }

              return apiFetchWrapper<T>({ url: url, config: config, retries: retries + 1 });

          } else {
              console.error(`Request to ${fullUrl} failed with 401 after ${maxRetries} retry attempts. Redirecting to sign-in.`);
              if (typeof window !== "undefined") {
                  window.location.replace("/auth/signin");
              }
              throw new Error("Authentication required. Please sign in.");
          }
      }

      if (!response.ok) {
          const errorBody = await response.text();
          console.error(`API request to ${fullUrl} failed with status: ${response.status}. Body: ${errorBody}`);
          throw new Error(`API request failed with status: ${response.status} ${response.statusText}`);
      }

      try {
          const contentType = response.headers.get('content-type');
          if (contentType && contentType.includes('application/json')) {
              return await response.json() as T;
          } else {
              return null as T;
          }
      } catch (parseError) {
          console.warn(`Could not parse JSON response for ${fullUrl}. It might be empty or non-JSON.`, parseError);
          return null as T;
      }

  } catch (error: any) {
      console.error(`Error during API call to ${fullUrl}:`, error.message);
      throw error;
  }
}


export async function apiGetRequest<T = any>(url: string): Promise<T> {
  return apiFetchWrapper<T>({ url: url, config: { method: "GET" } });
}

export async function apiPostRequest(url: string, body: any): Promise<any> {
  const config: RequestInit = {
      method: "POST",
      body: JSON.stringify(body),
  };
  return apiFetchWrapper({ url: url, config: config});
}

export async function apiPatchRequest(url: string, body: any): Promise<any> {
    const config: RequestInit = {
        method: "PATCH",
        body: JSON.stringify(body),
    };
    return apiFetchWrapper({ url: url, config: config});
  }
  

export async function apiDeleteRequest(url: string): Promise<any> {
  return apiFetchWrapper({ url: url, config: { method: "DELETE" } });
}

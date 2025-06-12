import { authStore } from "../stores/authStore";
import { handleRequestRefreshToken } from "./auth-related-functions";

async function apiPostRequest (url: string, body: any): Promise<any> {
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
            'Authorization': `Bearer ${authStore.get().accessToken}`,
            'Content-Type': 'application/json'
            }
        });
        if (response.status === 401) {
            handleRequestRefreshToken();
        }
        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }
        const json = await response.json();

    } catch (error) {
        console.error(error);
    }

}
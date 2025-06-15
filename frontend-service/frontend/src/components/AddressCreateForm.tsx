import { useEffect, useState } from "preact/hooks";
import { apiGetRequest, apiPatchRequest, apiPostRequest } from "../utils/api-request";
import type { Address } from "../types/Address";

interface Props {
  addressId: number | undefined;
  // redirectUrl: string | undefined; //TODO : move this to a query param!
}

export function AddressCreateForm({addressId}: Props) {
  const [formData, setFormData] = useState<Address>({
    id: 0,
    fullName: "",
    telephoneNumber: 0,
    addressFirstLine: "",
    addressSecondLine: "",
    postalCode: "",
    city: "",
    province: "",
    country: "",
    vatId: "",
    defaultAddress: false,
  });

  const [submitting, setSubmitting] = useState(false);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  function handleChange(e: Event) {
    const target = e.currentTarget as HTMLInputElement;
    const { name, type } = target;
    const value = type === "checkbox" ? target.checked : target.value;
    setFormData((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(e: Event) {
    e.preventDefault();
    setSubmitting(true);
    setErrorMsg(null);

    const body: Address = {
      ...formData,
      telephoneNumber: Number(formData.telephoneNumber),
    };

    try {
      if (addressId) {
        await apiPatchRequest("/api/v1/addresses", body);
      } else {
        await apiPostRequest("/api/v1/addresses", body);
      }
      //TODO: redirect to preceeding url
      // window.location.href = "/addresses";
    } catch (err: any) {
      console.error("Error creating address:", err);
      setErrorMsg("Could not save the address. Please try again.");
    } finally {
      setSubmitting(false);
    }
  }

  async function handleFetchAddressToEdit(addressId: number) {
    try {
      const jsonResponse = await apiGetRequest<Address>("/api/v1/addresses/" + addressId);
      setFormData(jsonResponse);
    } catch (error) {
      console.error("Error fetching addresses:", error);
      window.location.replace("/404");
    }
  } 

  useEffect(() => {
    if (addressId) {
      handleFetchAddressToEdit(addressId);
    }
  }, [])

  return (
    <form class="flex-col gap-05 w-75" onSubmit={handleSubmit}>
      <h2>Add new address</h2>

      {errorMsg && <p class="text-error">{errorMsg}</p>}

      <label>
        Full name *
        <input
          type="text"
          name="fullName"
          required
          value={formData.fullName}
          onInput={handleChange}
        />
      </label>

      <label>
        Telephone number *
        <input
          type="tel"
          name="telephoneNumber"
          required
          value={formData.telephoneNumber || ""}
          onInput={handleChange}
        />
      </label>

      <label>
        Address line 1 *
        <input
          type="text"
          name="addressFirstLine"
          required
          value={formData.addressFirstLine}
          onInput={handleChange}
        />
      </label>

      <label>
        Address line 2 *
        <input
          type="text"
          name="addressSecondLine"
          required
          value={formData.addressSecondLine}
          onInput={handleChange}
        />
      </label>

      <label>
        Postal code *
        <input
          type="text"
          name="postalCode"
          required
          value={formData.postalCode}
          onInput={handleChange}
        />
      </label>

      <label>
        City *
        <input
          type="text"
          name="city"
          required
          value={formData.city}
          onInput={handleChange}
        />
      </label>

      <label>
        Province *
        <input
          type="text"
          name="province"
          required
          value={formData.province}
          onInput={handleChange}
        />
      </label>

      <label>
        Country *
        <input
          type="text"
          name="country"
          required
          value={formData.country}
          onInput={handleChange}
        />
      </label>

      <label>
        VAT ID *
        <input
          type="text"
          name="vatId"
          required
          value={formData.vatId}
          onInput={handleChange}
        />
      </label>

      <label class="flex-row gap-05 align-center">
        <input
          type="checkbox"
          name="defaultAddress"
          checked={formData.defaultAddress}
          onInput={handleChange}
        />
        Set as default address
      </label>

      <button class="btn main-btn mt-1" type="submit" disabled={submitting}>
        {submitting ? "Saving…" : "Save address"}
      </button>
    </form>
  );
}
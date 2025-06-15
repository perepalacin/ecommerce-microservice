import { useEffect, useState } from "preact/hooks";
import { Suspense } from "preact/compat";
import type { Address } from "../types/Address";
import { apiGetRequest } from "../utils/api-request";
import { selectedAddressesStore } from "../stores/selectedAddressesStore";

export function AddressSelectionForm () {

  const [addresses, setAddresses] = useState<Address[]>([]);
  const [selectedDelivery, setSelectedDelivery] = useState<number | null>(
    selectedAddressesStore.get().deliveryAddressId,
  );
  const [selectedBilling, setSelectedBilling] = useState<number | null>(
    selectedAddressesStore.get().billingAddressId,
  );

  async function fetchAddresses () {
    try {
      const jsonResponse = await apiGetRequest<Address[]>("/api/v1/addresses/all");
      setAddresses(jsonResponse);
    } catch (error) {
      console.error("Error fetching addresses:", error);
    }
  }

  useEffect(() => {
    fetchAddresses();
    const unsubscribe = selectedAddressesStore.subscribe((value) => {
      setSelectedDelivery(value.deliveryAddressId);
      setSelectedBilling(value.billingAddressId);
    });
    return () => unsubscribe();
  }, []);

  function handleSelectDelivery(id: number) {
    selectedAddressesStore.set({
      deliveryAddressId: id,
      billingAddressId: selectedBilling,
    });
  }

  function handleSelectBilling(id: number) {
    selectedAddressesStore.set({
      deliveryAddressId: selectedDelivery,
      billingAddressId: id,
    });
  }

  return (
    <Suspense fallback={<p>Loading addresses…</p>}>
      <div class="flex-col gap-1 w-100 align-center">
        <div class="flex-row w-100 justify-between">
        <h1>Please select the appropiate addresses for this order:</h1>
          <a href="/addresses/create" aria-label="Add new address">
            <button class="btn outline-btn">
              Add new address
            </button>
          </a>
        </div>
        <section class="w-100">
          <h3>Delivery address</h3>
          {addresses.length === 0 && <p>No addresses yet.</p>}
          <ul class="dropdown w-100">
            {addresses.map(addr => (
              <li key={addr.id} class="flex-row gap-05 align-center justify-between">
                <input
                  class="w-10"
                  type="radio"
                  name="delivery"
                  checked={selectedDelivery === addr.id}
                  onChange={() => handleSelectDelivery(addr.id)}
                />
                <div class="flex-col">
                  <p><strong>{addr.fullName}</strong> {addr.defaultAddress && "(default)"}</p>
                  <p>
                    {addr.addressFirstLine} {addr.addressSecondLine}, {addr.postalCode} {addr.city},{" "}
                    {addr.province}, {addr.country}
                  </p>
                  <p>Tel: {addr.telephoneNumber}</p>
                </div>
                <div class="flex-row gap-1">
                  <a href={`/addresses/create/${addr.id}`} class="icon-btn outline-btn" aria-label="Edit address">
                    <img src="/icons/edit.svg" width={12} height={12} loading="lazy" />
                  </a>
                  <button class="icon-btn outline-btn" aria-label="Delete address">
                    <img src="/icons/trash.svg" width={12} height={12} loading="lazy" />
                  </button>
                </div>
              </li>
            ))}
          </ul>
        </section>

        <section class=" w-100 mt-1">
          <h3>Billing address</h3>

          <label class="flex-row gap-05 align-center">
            <input
              class="w-10"
              type="radio"
              name="billing"
              checked={selectedBilling === selectedDelivery && selectedBilling !== null}
              onChange={() => selectedDelivery !== null && handleSelectBilling(selectedDelivery)}
            />
            Use same as delivery
          </label>

          <ul class="dropdown w-100">
            {addresses.map(addr => (
              <li key={addr.id} class="flex-row gap-05 align-center justify-between">
                <input
                  class="w-10"
                  type="radio"
                  name="billing"
                  checked={selectedBilling === addr.id}
                  onChange={() => handleSelectBilling(addr.id)}
                />
                <div class="flex-col">
                  <p><strong>{addr.fullName}</strong></p>
                  <p>
                    {addr.addressFirstLine} {addr.addressSecondLine}, {addr.postalCode} {addr.city},{" "}
                    {addr.province}, {addr.country}
                  </p>
                </div>
                <div>
                </div>
              </li>
            ))}
          </ul>
        </section>

        <div class="flex-row gap-05 w-100 justify-end mt-1">
          <a href="/checkout" aria-label="Continue">
            <button
              class="btn main-btn"
              disabled={selectedDelivery === null || selectedBilling === null}
            >
              Continue
            </button>
          </a>
        </div>

      </div>
    </Suspense>
  );
}
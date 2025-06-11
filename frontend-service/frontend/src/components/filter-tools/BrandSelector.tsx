import { useEffect, useState } from "preact/hooks";
import { updateSearchParams } from "../../utils/update-search-params";

const AVAILABLE_BRANDS = ["Audemars Piguet", "Baume & Mercier", "Casio, Citizen", "Hublot", "IWC", "Jaeger-LeCoultre", "Longines", "Omega", "Patek Philippe", "Rolex", "Seiko", "Tag Heuer", "Tissot", "Tudor"];

interface Props {
    pathname: URL;
}

export default function BrandSelector ({pathname}: Props) {

    const [selectedBrands, setSelectedBrands] = useState(pathname.searchParams.get("brands")?.split(",") || []);

    function handleSelectOption (option: string) {
        if (!AVAILABLE_BRANDS.includes(option)) {
            return;
        }

        if (selectedBrands.includes(option)) {
            setSelectedBrands(selectedBrands.filter((opt) => opt === option));
            updateSearchParams(pathname, "brands", "");
        } else {
            const newSelection = [...selectedBrands];
            newSelection.push(option);
            setSelectedBrands(newSelection);
            updateSearchParams(pathname, "brands", newSelection.join(","));
        }
    }

    useEffect(() => {
        updateSearchParams(pathname, "brands", pathname.searchParams.get("brands") || "");
    }, []);

    return (
        <div class="mb-2">
            <h4 class="mb-1">Brand</h4>
            <div class="flex-col gap-1">
                <ul class="flex-row gap-05 flex-wrap">
                {AVAILABLE_BRANDS.map((opt) => 
                    <li class="flex-row gap-05 align-center">
                        <input type="checkbox" checked={selectedBrands.includes(opt)} value={opt} onClick={(e) => {handleSelectOption(opt)}} />
                        <p>{opt}</p>
                    </li>
                )}
                </ul>
            </div>
        </div>
    );
}
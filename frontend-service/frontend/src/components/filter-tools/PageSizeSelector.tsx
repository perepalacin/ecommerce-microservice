import { useEffect, useState } from "preact/hooks";
import { updateSearchParams } from "../../utils/update-search-params";
import { useStore } from "@nanostores/preact";
import { searchParams } from "../../stores/filterStore";

const AVAILABLE_MECHANISMS = [8, 16, 24];

interface Props {
    pathname: URL;
}

export default function PageSizeSelector ({pathname}: Props) {

    const [selectedPageSize, setSelectedPageSize] = useState<number>(Number(pathname.searchParams.get("size")) || 8);
    const $searchParams = useStore(searchParams);
    function handleSelectOption (option: number) {
        if (!AVAILABLE_MECHANISMS.includes(option)) {
            return;
        }

        if (selectedPageSize === option) {
            setSelectedPageSize(8);
            updateSearchParams(pathname, "size", "");
        } else {
            setSelectedPageSize(option);
            updateSearchParams(pathname, "size", String(option));
        }
    }

    useEffect(() => {
        updateSearchParams(pathname, "size", (pathname.searchParams.get("size")) || "8");
      }, []);


    return (
        <div class="flex-col gap-1">
            <h4>Results per page</h4>
            <ul class="flex-row gap-05 flex-wrap">
            {AVAILABLE_MECHANISMS.map((opt) => 
                <li class="flex-row gap-05 align-center">
                    <input type="checkbox" checked={selectedPageSize===opt} value={opt} onClick={(e) => {handleSelectOption(opt)}} />
                    <p>{opt}</p>
                </li>
            )}
            </ul>
        </div>
    );
}
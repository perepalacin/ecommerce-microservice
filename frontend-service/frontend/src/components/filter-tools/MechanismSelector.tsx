import { useEffect, useState } from "preact/hooks";
import { updateSearchParams } from "../../utils/update-search-params";
import { useStore } from "@nanostores/preact";
import { searchParams } from "../../stores/filterStore";

const AVAILABLE_MECHANISMS = ["Automatic", "Quartz", "Manual"];

interface Props {
    pathname: URL;
}

export default function MechanismSelector ({pathname}: Props) {

    const [selectedMechanisms, setSelectedMechanisms] = useState<String[]>(pathname.searchParams.get("mechanisms")?.split(",") || []);
    const $searchParams = useStore(searchParams);
    function handleSelectOption (option: string) {
        if (!AVAILABLE_MECHANISMS.includes(option)) {
            return;
        }

        if (selectedMechanisms.includes(option)) {
            setSelectedMechanisms(selectedMechanisms.filter((opt) => opt !== option));
            updateSearchParams(pathname, "mechanisms", selectedMechanisms.filter((opt) => opt !== option).join(","));
        } else {
            const newSelection = [...selectedMechanisms];
            newSelection.push(option)
            setSelectedMechanisms(newSelection);
            updateSearchParams(pathname, "mechanisms", newSelection.join(","));
        }
    }

    useEffect(() => {
        updateSearchParams(pathname, "mechanisms", pathname.searchParams.get("mechanisms") || "");
      }, []);


    return (
        <div class="flex-col gap-1">
            <h4>Mechanism</h4>
            <ul class="flex-row gap-05 flex-wrap">
            {AVAILABLE_MECHANISMS.map((opt) => 
                <li class="flex-row gap-05 align-center">
                    <input type="checkbox" checked={selectedMechanisms.includes(opt)} value={opt} onClick={(e) => {handleSelectOption(opt)}} />
                    <p>{opt}</p>
                </li>
            )}
            </ul>
        </div>
    );
}
import { useStore } from '@nanostores/preact';
import { isAdvancedFiltersOpen, setIsAdvancedFiltersOpen } from '../../stores/filterStore';

export default function AdvancedFiltersButton () {
    const $isAdvancedFiltersOpen = useStore(isAdvancedFiltersOpen);
    return (
        <button class={`filters-button btn main-btn ${$isAdvancedFiltersOpen ? "main-btn-active" : ""}`} onClick={() => {setIsAdvancedFiltersOpen(!$isAdvancedFiltersOpen)}}>
            <img src="/icons/filters.svg" width="15" height="15" loading="lazy"/>
            Filters
        </button>
    );
}
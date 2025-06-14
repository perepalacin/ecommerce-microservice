import { useStore } from '@nanostores/preact';
import { isAdvancedFiltersOpen} from '../../stores/filterStore';
import MechanismSelector from './MechanismSelector';
import PriceRangeSelector from './PriceRangeSelector';
import DiameterRangeSelector from './DiameterRangeSelector';
import BrandSelector from './BrandSelector';
import { applyFilters, deleteAllFilters } from '../../utils/update-search-params';
import PageSizeSelector from './PageSizeSelector';

interface Props {
    pathname: URL;
}

export default function AdvancedFilters ({pathname}: Props) {
    
    const $isAdvancedFiltersOpen = useStore(isAdvancedFiltersOpen);
    
    return (
        <section class={`mb-1 filters-section ${$isAdvancedFiltersOpen ? "visible" : ""}`}>
            <div class="flex-row align-center justify-between gap-1 mb-1">
                <h4>Advanced Filters</h4>
                <div class="flex-row gap-1">
                    <a href="/products" aria-label="products page">
                        <button class="btn outline-btn">Clear Filters</button>
                    </a>
                    <button class="btn main-btn" onClick={() => applyFilters(pathname)}>Apply Filters</button>
                </div>
            </div>
            <div class="filters-grid">
                <aside>
                    <BrandSelector pathname={pathname} />
                    <div class="flex-row gap-2">
                        <MechanismSelector pathname={pathname} />
                        <PageSizeSelector pathname={pathname} />
                    </div>
                </aside>
                <aside class="w-100 flex-row gap-1">
                    <div class="w-100 flex-col gap-05">
                        <h4>Diameter</h4>
                        <DiameterRangeSelector pathname={pathname} />
                        <h4>Price Range</h4>
                        <PriceRangeSelector pathname={pathname} />
                    </div>
                </aside>
            </div>
        </section>
    );
}
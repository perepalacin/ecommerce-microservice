import { useStore } from '@nanostores/preact';
import { isAdvancedFiltersOpen} from '../stores/filterStore';

export default function AdvancedFilters () {
    const $isAdvancedFiltersOpen = useStore(isAdvancedFiltersOpen);
    return (
        <>
        { $isAdvancedFiltersOpen && 
            <section class="mb-2">
                <h4>Advanced Filters</h4>
                <div class="filters-grid">
                <aside>
                    <h4>Brand</h4>
                    {/* These 4 all together */}
                </aside>
                <aside>
                    <h4>mechanism</h4>
                    <h4>diameter</h4>
                    <h4>maxPrice</h4>
                    <h4>minPrice</h4>
                </aside>
                </div>
            </section>
        }
        </>
    );
}
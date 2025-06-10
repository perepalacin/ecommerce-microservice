import { atom } from 'nanostores';

export const isAdvancedFiltersOpen = atom(false);

export function setIsAdvancedFiltersOpen (newState: boolean) {
    isAdvancedFiltersOpen.set(newState);
}
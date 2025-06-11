import { atom, map } from 'nanostores';

export const isAdvancedFiltersOpen = atom(false);

export function setIsAdvancedFiltersOpen (newState: boolean) {
    isAdvancedFiltersOpen.set(newState);
}

export const searchParams = map<Record<string, string>>({});
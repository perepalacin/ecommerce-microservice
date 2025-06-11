import { searchParams } from "../stores/filterStore";

export function updateSearchParams (pathname: any, paramToSet: string, newValue: string) {
    const newSearchParams = new URLSearchParams(pathname.searchParams);

    if (newValue === null || newValue === undefined || newValue === '') {
      setSearchParams(paramToSet, "");
      setSearchParams("page", 1);

    } else {
      setSearchParams(paramToSet, newValue);
      setSearchParams("page", 1);
    }

}

export function updateTupleSearchParams (pathname: any, paramToSet: string[], newValues: number[], allowedRanges: number[]) {
  if (paramToSet.length != 2 || newValues.length != 2 || allowedRanges.length != 2) {
    console.error("Invalid parameters to set length, please recheck this function invocation!");
    return;
  }
  
  const newSearchParams = new URLSearchParams(pathname.searchParams);

  if (newValues[0] <= allowedRanges[0] || newValues[0] === undefined || newValues[0] === null) {
    setSearchParams(paramToSet[0], "");
    setSearchParams("page", 1);
  } else {
    newSearchParams.set(paramToSet[0], String(newValues[0]));
    newSearchParams.set('page', '1');
    setSearchParams(paramToSet[0], String(newValues[0]));
    setSearchParams("page", 1);
  }

  if (newValues[1] >= allowedRanges[1] || newValues[1] === undefined || newValues[0] === null || newValues[1] < newValues[0]) {
    setSearchParams(paramToSet[1], "");
    setSearchParams("page", 1);
  } else {
    setSearchParams(paramToSet[1], String(newValues[1]));
    setSearchParams("page", 1);
  }

}

export function setSearchParams (key: string, value: string | number) {
  const existingParam = searchParams.get()[key];
  if (existingParam) {
        searchParams.setKey(key, String(value))
  } else {
      searchParams.setKey(key, String(value));
  }
}

export function deleteAllFilters () {
  searchParams.set({});
}

export function applyFilters (pathname: URL) {
  const newSearchParams = new URLSearchParams(searchParams.get());
  const newUrl = `${pathname.origin}${pathname.pathname}?${newSearchParams.toString()}`;

  if (window.location.href + "?" !== newUrl) {
      window.location.href = newUrl;
  }
}

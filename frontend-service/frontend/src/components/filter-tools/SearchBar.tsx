import { useEffect, useState } from "preact/hooks";
import { applyFilters, updateSearchParams } from "../../utils/update-search-params";

interface Props {
  pathname: URL;
}

export default function SearchBar({pathname}: Props) {
  const [inputValue, setInputValue] = useState(pathname.searchParams.get("query") || "");
  const [debounceTimeout, setDebounceTimeout] = useState<ReturnType<typeof setTimeout> | null>(null);

  function handleChange (event: any) {
    const newValue = event.target.value;
    setInputValue(newValue);
    updateSearchParams(pathname, "query", newValue);

    if (debounceTimeout) {
      clearTimeout(debounceTimeout);
    }

    const newTimeout = setTimeout(() => {
      applyFilters(pathname);
    }, 300); 

    setDebounceTimeout(newTimeout);
  };

  useEffect(() => {
    return () => {
      if (debounceTimeout) {
        clearTimeout(debounceTimeout);
      }
    };
  }, [debounceTimeout]); 

  useEffect(() => {
      updateSearchParams(pathname, "query", inputValue);
  }, []);

  return (
    <input
      type="search"
      name="query"
      placeholder="Search products..."
      value={inputValue}
      onInput={handleChange} 
      aria-label="Search products..."
      class="product-search-bar"
    />
  );
}

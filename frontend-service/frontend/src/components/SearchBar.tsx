import { useEffect, useState } from "preact/hooks";

interface Props {
  query: string;
  currentSearchParams: URLSearchParams;
  pathname: URL;
}

export default function SearchBar({query, currentSearchParams, pathname}: Props) {
  const [inputValue, setInputValue] = useState(query || '');
  const [debounceTimeout, setDebounceTimeout] = useState<ReturnType<typeof setTimeout> | null>(null);

  function handleUpdateUrl (newValue: string) {
    const newSearchParams = new URLSearchParams(currentSearchParams);

    if (newValue === null || newValue === undefined || newValue === '') {
      newSearchParams.delete("query");
    } else {
      newSearchParams.set("query", String(newValue));
      newSearchParams.set('page', '1');
    }

    const newUrl = `${pathname.origin}${pathname.pathname}?${newSearchParams.toString()}`;

    if (window.location.href !== newUrl) {
        window.location.href = newUrl;
    }
  }

  function handleChange (event: any) {
    const newValue = event.target.value;
    setInputValue(newValue);

    if (debounceTimeout) {
      clearTimeout(debounceTimeout);
    }

    const newTimeout = setTimeout(() => {
      handleUpdateUrl(newValue);
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
    if (inputValue !== '') {
      handleUpdateUrl(inputValue);
    }
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

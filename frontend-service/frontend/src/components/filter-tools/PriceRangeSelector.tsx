import { useEffect, useState } from "preact/hooks";
import { RangeSlider } from "../ui-generics/RangeSlider";
import { updateSearchParams, updateTupleSearchParams } from "../../utils/update-search-params";

interface Props {
    pathname: URL;
}

export default function PriceRangeSelector ({pathname}: Props) {

  const [priceRange, setPriceRange] = useState({ min: Number(pathname.searchParams.get("minPrice")) || 0, max: Number(pathname.searchParams.get("max")) || 15000 });
  
  const handlePriceChange = (min: number, max: number) => {
    setPriceRange({ min, max });
    updateTupleSearchParams(pathname, ["minPrice", "maxPrice"], [min, max], [0, 15000]);
  };

  useEffect(() => {
    updateSearchParams(pathname, "minPrice", pathname.searchParams.get("minPrice") || "");
    updateSearchParams(pathname, "maxPrice", pathname.searchParams.get("maxPrice") || "");
  }, []);

  return (
      <RangeSlider
        min={0}
        max={15000}
        step={100}
        initialMinValue={priceRange.min}
        initialMaxValue={priceRange.max}
        onChange={handlePriceChange}
        valueUnit="€"
        className="slider-container"
      />
      );
}
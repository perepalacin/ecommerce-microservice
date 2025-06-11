import { useEffect, useState } from "preact/hooks";
import { RangeSlider } from "../ui-generics/RangeSlider";
import { updateSearchParams, updateTupleSearchParams } from "../../utils/update-search-params";

interface Props {
    pathname: URL;
}

export default function DiameterRangeSelector ({pathname}: Props) {

  const [diameterRange, setDiameterRange] = useState({ min: Number(pathname.searchParams.get("minDiameter")) || 36, max: Number(pathname.searchParams.get("maxDiameter")) || 56 });
  
  const handleDiameterChange = (min: number, max: number) => {
    setDiameterRange({ min, max });
    updateTupleSearchParams(pathname, ["minDiameter", "maxDiameter"], [min, max], [36, 56]);
  };

  useEffect(() => {
    updateSearchParams(pathname, "minDiameter", pathname.searchParams.get("minDiameter") || "");
    updateSearchParams(pathname, "maxDiameter", pathname.searchParams.get("maxDiameter") || "");
  }, []);


  return (
      <RangeSlider
        min={36}
        max={56}
        step={1}
        initialMinValue={diameterRange.min}
        initialMaxValue={diameterRange.max}
        onChange={handleDiameterChange}
        valueUnit="mm"
        className="slider-container"
      />
      );
}
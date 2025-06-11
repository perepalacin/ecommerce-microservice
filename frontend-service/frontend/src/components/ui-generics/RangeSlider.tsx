import { useState, useRef, useCallback, useEffect } from 'preact/hooks';

interface Props {
  min?: number;
  max?: number;
  step?: number;
  initialMinValue?: number;
  initialMaxValue?: number;
  onChange?: (min: number, max: number) => void;
  valueUnit: string;
  className?: string;
}

export const RangeSlider = ({min = 0, max = 1000, step = 1, initialMinValue = min, initialMaxValue = max, onChange, valueUnit, className = '',}: Props) => {
  const [minValue, setMinValue] = useState(initialMinValue);
  const [maxValue, setMaxValue] = useState(initialMaxValue);
  const [isDragging, setIsDragging] = useState<'min' | 'max' | null>(null);
  const sliderRef = useRef<HTMLDivElement>(null);

  const getPercentage = (value: number) => ((value - min) / (max - min)) * 100;

  const getValueFromPosition = useCallback((clientX: number) => {
    if (!sliderRef.current) return min;
    
    const rect = sliderRef.current.getBoundingClientRect();
    const percentage = Math.max(0, Math.min(1, (clientX - rect.left) / rect.width));
    const rawValue = min + percentage * (max - min);
    
    return Math.round(rawValue / step) * step;
  }, [min, max, step]);

  const handleMouseDown = (handle: 'min' | 'max') => (e: MouseEvent) => {
    e.preventDefault();
    setIsDragging(handle);
  };

  const handleTouchStart = (handle: 'min' | 'max') => (e: TouchEvent) => {
    e.preventDefault();
    setIsDragging(handle);
  };

  const updateValue = useCallback((clientX: number) => {
    const newValue = getValueFromPosition(clientX);
    
    if (isDragging === 'min') {
      const clampedValue = Math.min(newValue, maxValue);
      setMinValue(clampedValue);
      onChange?.(clampedValue, maxValue);
    } else if (isDragging === 'max') {
      const clampedValue = Math.max(newValue, minValue);
      setMaxValue(clampedValue);
      onChange?.(minValue, clampedValue);
    }
  }, [isDragging, minValue, maxValue, getValueFromPosition, onChange]);

  useEffect(() => {
    const handleMouseMove = (e: MouseEvent) => {
      if (isDragging) {
        updateValue(e.clientX);
      }
    };

    const handleTouchMove = (e: TouchEvent) => {
      if (isDragging && e.touches[0]) {
        updateValue(e.touches[0].clientX);
      }
    };

    const handleEnd = () => {
      setIsDragging(null);
    };

    if (isDragging) {
      document.addEventListener('mousemove', handleMouseMove);
      document.addEventListener('mouseup', handleEnd);
      document.addEventListener('touchmove', handleTouchMove);
      document.addEventListener('touchend', handleEnd);
    }

    return () => {
      document.removeEventListener('mousemove', handleMouseMove);
      document.removeEventListener('mouseup', handleEnd);
      document.removeEventListener('touchmove', handleTouchMove);
      document.removeEventListener('touchend', handleEnd);
    };
  }, [isDragging, updateValue]);

  const handleKeyDown = (handle: 'min' | 'max') => (e: KeyboardEvent) => {
    let newValue: number;
    const currentValue = handle === 'min' ? minValue : maxValue;
    
    switch (e.key) {
      case 'ArrowLeft':
      case 'ArrowDown':
        e.preventDefault();
        newValue = Math.max(min, currentValue - step);
        break;
      case 'ArrowRight':
      case 'ArrowUp':
        e.preventDefault();
        newValue = Math.min(max, currentValue + step);
        break;
      case 'Home':
        e.preventDefault();
        newValue = min;
        break;
      case 'End':
        e.preventDefault();
        newValue = max;
        break;
      default:
        return;
    }

    if (handle === 'min') {
      const clampedValue = Math.min(newValue, maxValue);
      setMinValue(clampedValue);
      onChange?.(clampedValue, maxValue);
    } else {
      const clampedValue = Math.max(newValue, minValue);
      setMaxValue(clampedValue);
      onChange?.(minValue, clampedValue);
    }
  };

  const minPercentage = getPercentage(minValue);
  const maxPercentage = getPercentage(maxValue);

  return (
    <div className={`range-slider ${className}`}>
      <div className="value-display">
        <div className="value-item">
          <span className="value">{formatValue(minValue, valueUnit)}</span>
          <div className="label">Minimum</div>
        </div>
        <div className="value-item right">
          <span className="value">{formatValue(maxValue, valueUnit)}</span>
          <div className="label">Maximum</div>
        </div>
      </div>

      <div className="slider-wrapper">
        <div
          ref={sliderRef}
          className="slider-track"
          onClick={(e) => {
            const newValue = getValueFromPosition(e.clientX);
            const minDistance = Math.abs(newValue - minValue);
            const maxDistance = Math.abs(newValue - maxValue);
            
            if (minDistance < maxDistance) {
              const clampedValue = Math.min(newValue, maxValue);
              setMinValue(clampedValue);
              onChange?.(clampedValue, maxValue);
            } else {
              const clampedValue = Math.max(newValue, minValue);
              setMaxValue(clampedValue);
              onChange?.(minValue, clampedValue);
            }
          }}
        >
          <div
            className="slider-range"
            style={{
              left: `${minPercentage}%`,
              width: `${maxPercentage - minPercentage}%`,
            }}
          />

          <div
            className={`slider-handle ${isDragging === 'min' ? 'dragging' : ''}`}
            style={{ left: `${minPercentage}%` }}
            tabIndex={0}
            role="slider"
            aria-valuemin={min}
            aria-valuemax={maxValue}
            aria-valuenow={minValue}
            aria-label="Minimum price"
            onMouseDown={handleMouseDown('min')}
            onTouchStart={handleTouchStart('min')}
            onKeyDown={handleKeyDown('min')}
          />

          <div
            className={`slider-handle ${isDragging === 'max' ? 'dragging' : ''}`}
            style={{ left: `${maxPercentage}%` }}
            tabIndex={0}
            role="slider"
            aria-valuemin={minValue}
            aria-valuemax={max}
            aria-valuenow={maxValue}
            aria-label="Maximum price"
            onMouseDown={handleMouseDown('max')}
            onTouchStart={handleTouchStart('max')}
            onKeyDown={handleKeyDown('max')}
          />
        </div>
      </div>
    </div>
  );
};


function formatValue (value: number | string, unit = "") {
  return (`${value.toLocaleString()} ${unit}`);
}
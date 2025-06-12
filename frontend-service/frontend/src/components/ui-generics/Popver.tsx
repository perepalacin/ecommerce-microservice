import { useState, useRef, useEffect } from 'preact/hooks';
import { cloneElement, type ComponentChildren, type VNode } from 'preact';

interface PopoverProps {
  trigger: VNode;
  children: ComponentChildren;
  placement?: 'top' | 'bottom' | 'left' | 'right' | 'top-start' | 'top-end' | 'bottom-start' | 'bottom-end';
  offset?: number;
  className?: string;
  closeOnClickOutside?: boolean;
  closeOnEscape?: boolean;
  triggerOn?: 'click' | 'hover';
  hoverDelay?: number;
}

const Popover = ({
  trigger,
  children,
  placement = 'bottom',
  offset = 8,
  className = '',
  closeOnClickOutside = true,
  closeOnEscape = true,
  triggerOn = 'click',
  hoverDelay = 200,
}: PopoverProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [position, setPosition] = useState({ top: 0, left: 0 });
  const triggerRef = useRef<HTMLElement>(null);
  const popoverRef = useRef<HTMLDivElement>(null);
  const hoverTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  const calculatePosition = () => {
    if (!triggerRef.current || !popoverRef.current) return;

    const triggerRect = triggerRef.current.getBoundingClientRect();
    const popoverRect = popoverRef.current.getBoundingClientRect();
    const viewport = {
      width: window.innerWidth,
      height: window.innerHeight,
    };

    let top = 0;
    let left = 0;

    switch (placement) {
      case 'top':
        top = triggerRect.top - popoverRect.height - offset;
        left = triggerRect.left + (triggerRect.width - popoverRect.width) / 2;
        break;
      case 'top-start':
        top = triggerRect.top - popoverRect.height - offset;
        left = triggerRect.left;
        break;
      case 'top-end':
        top = triggerRect.top - popoverRect.height - offset;
        left = triggerRect.right - popoverRect.width;
        break;
      case 'bottom':
        top = triggerRect.bottom + offset;
        left = triggerRect.left + (triggerRect.width - popoverRect.width) / 2;
        break;
      case 'bottom-start':
        top = triggerRect.bottom + offset;
        left = triggerRect.left;
        break;
      case 'bottom-end':
        top = triggerRect.bottom + offset;
        left = triggerRect.right - popoverRect.width;
        break;
      case 'left':
        top = triggerRect.top + (triggerRect.height - popoverRect.height) / 2;
        left = triggerRect.left - popoverRect.width - offset;
        break;
      case 'right':
        top = triggerRect.top + (triggerRect.height - popoverRect.height) / 2;
        left = triggerRect.right + offset;
        break;
    }

    if (left < 0) left = 8;
    if (left + popoverRect.width > viewport.width) {
      left = viewport.width - popoverRect.width - 8;
    }
    if (top < 0) top = 8;
    if (top + popoverRect.height > viewport.height) {
      top = viewport.height - popoverRect.height - 8;
    }

    setPosition({ top, left });
  };

  const clearHoverTimeout = () => {
    if (hoverTimeoutRef.current) {
      clearTimeout(hoverTimeoutRef.current);
      hoverTimeoutRef.current = null;
    }
  };

  const handleTriggerClick = (e: Event) => {
    if (triggerOn === 'click') {
      e.stopPropagation();
      setIsOpen(!isOpen);
    }
  };

  const handleTriggerMouseEnter = () => {
    if (triggerOn === 'hover') {
      clearHoverTimeout();
      hoverTimeoutRef.current = setTimeout(() => {
        setIsOpen(true);
      }, hoverDelay);
    }
  };

  const handleTriggerMouseLeave = () => {
    if (triggerOn === 'hover') {
      clearHoverTimeout();
      hoverTimeoutRef.current = setTimeout(() => {
        setIsOpen(false);
      }, hoverDelay);
    }
  };

  const handlePopoverMouseEnter = () => {
    if (triggerOn === 'hover') {
      clearHoverTimeout();
    }
  };

  const handlePopoverMouseLeave = () => {
    if (triggerOn === 'hover') {
      clearHoverTimeout();
      hoverTimeoutRef.current = setTimeout(() => {
        setIsOpen(false);
      }, hoverDelay);
    }
  };

  const handleClickOutside = (e: Event) => {
    if (
      triggerOn === 'click' &&
      closeOnClickOutside &&
      popoverRef.current &&
      triggerRef.current &&
      !popoverRef.current.contains(e.target as Node) &&
      !triggerRef.current.contains(e.target as Node)
    ) {
      setIsOpen(false);
    }
  };

  const handleEscape = (e: KeyboardEvent) => {
    if (closeOnEscape && e.key === 'Escape') {
      setIsOpen(false);
    }
  };

  useEffect(() => {
    if (isOpen) {
      calculatePosition();
      
      const handleReposition = () => calculatePosition();
      window.addEventListener('scroll', handleReposition, true);
      window.addEventListener('resize', handleReposition);
      
      return () => {
        window.removeEventListener('scroll', handleReposition, true);
        window.removeEventListener('resize', handleReposition);
      };
    }
  }, [isOpen, placement, offset]);

  useEffect(() => {
    if (triggerOn === 'click' && isOpen) {
      document.addEventListener('click', handleClickOutside);
      document.addEventListener('keydown', handleEscape);
      
      return () => {
        document.removeEventListener('click', handleClickOutside);
        document.removeEventListener('keydown', handleEscape);
      };
    }
  }, [isOpen, closeOnClickOutside, closeOnEscape, triggerOn]);

  useEffect(() => {
    return () => {
      clearHoverTimeout();
    };
  }, []);

  const triggerElement = cloneElement(trigger, {
    ref: triggerRef,
    onClick: (e: Event) => {
      const originalOnClick = (trigger.props as { onClick?: (e: Event) => void }).onClick;

      if (typeof originalOnClick === 'function') {
        originalOnClick(e);
      }
      handleTriggerClick(e);
    },
    onMouseEnter: (e: Event) => {
      const originalOnMouseEnter = (trigger.props as { onMouseEnter?: (e: Event) => void }).onMouseEnter;
      if (typeof originalOnMouseEnter === "function") {
        originalOnMouseEnter(e);
      }
      handleTriggerMouseEnter();
    },
    onMouseLeave: (e: Event) => {
      const oroginalOnMouseLeave = (trigger.props as { onMouseLeave?: (e: Event) => void}).onMouseLeave;
      if (typeof oroginalOnMouseLeave === "function") {
        oroginalOnMouseLeave(e);
      }
      handleTriggerMouseLeave();
    },
  });

  return (
    <>
      {triggerElement}
      {isOpen && (
        <div
          ref={popoverRef}
          className={`popover ${className}`}
          style={{
            position: 'fixed',
            top: `${position.top}px`,
            left: `${position.left}px`,
            zIndex: 1000,
          }}
          onClick={(e) => e.stopPropagation()}
          onMouseEnter={handlePopoverMouseEnter}
          onMouseLeave={handlePopoverMouseLeave}
        >
          <div className="popover-content px-1 py-1">
            {children}
          </div>
        </div>
      )}
    </>
  );
};

export default Popover;
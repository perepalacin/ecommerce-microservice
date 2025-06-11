import { useState } from "preact/hooks";
import type { FunctionComponent, JSX } from "preact"; 

interface Props {
    PopOverTrigger: FunctionComponent<{ onClick: () => void; }>; 
    children: JSX.Element | JSX.Element[] | string | number | null; 
}

export default function PopOver ({PopOverTrigger, children}: Props) {
    const [isPopoverVisible, setIsPopoverOpen] = useState(false);

    const togglePopover = () => {
        setIsPopoverOpen((prevState) => !prevState);
    };

    return (
        <div style={{position: "relative"}}>
            <PopOverTrigger onClick={togglePopover} />

            <div class={`pop-over ${isPopoverVisible ? "visible" : ""}`}>
                {children} 
            </div>
        </div>
    );
}

// example: 
// import PopOver from "./Popver";
// import type { FunctionComponent } from 'preact';

// const AdvancedFiltersTrigger: FunctionComponent<{ onClick: () => void; }> = ({ onClick }) => (
//     <button class="filters-button btn main-btn" onClick={onClick}>
//         <img src="/icons/filters.svg" width="15" height="15" loading="lazy"/>
//         Filters
//     </button>
// );

// export default function AdvandedFilters () {

//     return (
//         <PopOver PopOverTrigger={AdvancedFiltersTrigger}>
//             <div>Hello from the popover content!</div>
//             <p>More details here.</p>
//         </PopOver>
//     );
// }
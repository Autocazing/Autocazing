import SolutionTooltip from "./SolutionTooltip";
import { Tooltip as ReactTooltip } from "react-tooltip";

import "./TooltipStyle.css";

const ExpirationList = ({ item, solution }) => {
    return (
        <div className="flex flex-row justify-between items-center mb-2 mx-4">
            <div className="text-black text-title-sm font-semibold">
                {item.ingredient_name}
            </div>
            <div>
                <button
                    data-tooltip-id={`tooltip-${item.ingredient_id}`}
                    className=" bg-transparent text-title-xsm hover:bg-primary text-primary font-semibold hover:text-white py-0.5 px-1.5 border border-primary hover:border-transparent rounded"
                >
                    솔루션
                </button>
                <ReactTooltip
                    id={`tooltip-${item.ingredient_id}`}
                    place="top"
                    effect="solid"
                    className="custom-tooltip"
                >
                    <SolutionTooltip item={item} solution={solution} />
                </ReactTooltip>
            </div>
        </div>
    );
};

export default ExpirationList;

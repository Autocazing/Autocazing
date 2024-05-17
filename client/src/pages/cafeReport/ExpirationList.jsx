import SolutionTooltip from "./SolutionTooltip";
import { Tooltip as ReactTooltip } from "react-tooltip";
import { SolutionGetApi } from "../../apis/server/ReportApi";
import { useEffect, useState } from "react";
import "./TooltipStyle.css";

const ExpirationList = ({ item }) => {
    const {
        data: SolutionData,
        isLoading,
        isError,
        error,
    } = SolutionGetApi(item.ingredient_id);

    if (isLoading) {
        return <div>Loading...</div>;
    }
    if (isError) {
        return <div>Error: {error.message}</div>;
    }

    // 데이터가 존재하지 않는 경우 예외 처리
    if (!SolutionData || !SolutionData.optimal_sales) {
        return <div>No solution data available.</div>;
    }
    return (
        <div className="flex flex-row justify-between items-center mb-2 mx-4">
            <div className="text-black text-title-sm font-semibold">
                {item.ingredient_name}
            </div>
            <div>
                <button
                    data-tooltip-id={`tooltip-${item.ingredient_id}`}
                    data-tooltip-content={SolutionData.optimal_sales[0]}
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
                    <SolutionTooltip item={item} SolutionData={SolutionData} />
                </ReactTooltip>
            </div>
        </div>
    );
};

export default ExpirationList;

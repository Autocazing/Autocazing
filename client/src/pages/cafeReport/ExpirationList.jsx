import SolutionTooltip from "./SolutionTooltip";
import { Tooltip as ReactTooltip } from "react-tooltip";
import { SolutionGetApi } from "../../apis/server/ReportApi";
import { useEffect, useState } from "react";
import "./TooltipStyle.css";
import { useQueryClient, useQuery } from "@tanstack/react-query";

const ExpirationList = ({ item, reportId }) => {
    const {
        data: SolutionData,
        isLoading,
        isError,
        error,
    } = SolutionGetApi(reportId, item.ingredient_id);

    console.log(item, reportId);

    return (
        <div className="flex flex-row justify-between items-center mb-2 mx-4">
            <div className="text-black text-title-sm font-semibold">
                {item.ingredient_name}
            </div>
            <div>
                <button
                    data-tooltip-id={`tooltip-${item.ingredient_id}`}
                    data-tooltip-content={SolutionData?.optimal_sales?.[0]}
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
                    {isLoading ? (
                        <div>솔루션이 없습니다</div>
                    ) : isError ? (
                        <div>솔루션이 없습니다</div>
                    ) : SolutionData ? (
                        <SolutionTooltip
                            item={item}
                            SolutionData={SolutionData}
                        />
                    ) : (
                        <div>솔루션 데이터가 없습니다.</div>
                    )}
                </ReactTooltip>
            </div>
        </div>
    );
};

export default ExpirationList;

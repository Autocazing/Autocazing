import Calendar from "./Calendar";
import { useState } from "react";

const CafeReport = () => {
    return (
        <div>
            <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                    리포트
                </h2>
                <ol className="flex items-center gap-2">
                    <li>리포트 /</li>
                    <li className="font-bold text-primary">리포트</li>
                </ol>
            </div>

            <Calendar />
        </div>
    );
};

export default CafeReport;

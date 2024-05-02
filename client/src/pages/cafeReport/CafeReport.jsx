import Calendar from "./Calendar";
import { useState } from "react";

const CafeReport = () => {
    //이건 더미데이터 나중에 api통해서 받아와야함
    const [reportData, setReportData] = useState([
        {
            date: "2024-04-29",
            yesterday: 120,
            predict: 130,
        },
        {
            date: "2024-04-30",
            yesterday: 150,
            predict: 160,
        },
        {
            date: "2024-05-01",
            yesterday: 140,
            predict: 150,
        },
    ]);

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

            <Calendar reportData={reportData} />
        </div>
    );
};

export default CafeReport;

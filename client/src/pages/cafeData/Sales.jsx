import { useEffect, useState } from "react";
import SalesChart from "../../components/cafeData/SalesChart.jsx";
import {
    GetSalesDay,
    GetSalesMonth,
    GetSalesWeek,
} from "../../apis/server/CafeDataApi.jsx";

const Sales = () => {
    const { data: SalesDay } = GetSalesDay();
    const { data: SalesWeek } = GetSalesWeek();
    const { data: SalesMonth } = GetSalesMonth();

    const [dayData, setDayData] = useState([]);
    const [weekData, setWeekData] = useState([]);

    useEffect(() => {
        if (SalesDay) {
            if (SalesDay.length > 1) {
                const lastSevenTotalSales = SalesDay.slice(-7) // 마지막 7개의 데이터만 가져옵니다.
                    .map((item) => item.totalSales); // totalSales 값만 추출합니다.
                setDayData(lastSevenTotalSales);
            }
        }
    }, [SalesDay, SalesWeek, SalesMonth]);

    useEffect(() => {
        if (SalesWeek) {
            if (SalesWeek.length > 1) {
                const lastSevenTotalSales = SalesWeek.slice(-7) // 마지막 7개의 데이터만 가져옵니다.
                    .map((item) => item.totalSales); // totalSales 값만 추출합니다.
                setWeekData(lastSevenTotalSales);
            }
        }
    }, [SalesDay, SalesWeek, SalesMonth]);

    return (
        <div>
            <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                    Data
                </h2>
                <ol className="flex items-center gap-2">
                    <li>Data /</li>
                    <li className="font-bold text-primary">매출별</li>
                </ol>
            </div>
            <div className="mt-4 grid grid-cols-12 gap-4 md:mt-6 md:gap-6 2xl:mt-7.5 2xl:gap-7.5">
                <SalesChart dayData={dayData} weekData={weekData} />
            </div>
        </div>
    );
};

export default Sales;

import { ReportMonthGetApi } from "../../apis/server/ReportApi";
import ExpirationList from "./ExpirationList";

const DashboardReport = () => {
    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth() + 1;
    const day = today.getDate();
    const {
        data: monthReportData,
        isLoading,
        isError,
        error,
    } = ReportMonthGetApi(year, month, day);

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (isError) {
        return <div>Error: {error.message}</div>;
    }

    console.log(monthReportData);
    const formattedExpectedSales =
        monthReportData[0].expected_monthly_sales.toLocaleString("ko-KR");
    const formattedCurrentSales =
        monthReportData[0].current_monthly_sales.toLocaleString("ko-KR");

    return (
        <div className="col-span-12 rounded-sm border border-stroke bg-white p-7.5 shadow-default dark:border-strokedark dark:bg-boxdark xl:col-span-4">
            <div>
                <div className="mb-3">
                    <div className="font-semibold text-black dark:text-white text-title-md">
                        Today report
                    </div>
                </div>
                <div className="mb-4">
                    <div className="font-semibold text-black text-title-sm mb-0">
                        예상 매출액
                    </div>
                    <div className="text-title-xsm">
                        AI 할용해 매출액을 예상 합니다.
                    </div>
                    <div className="flex flex-row justify-center gap-10 mt-3">
                        <div className="text-center">
                            <div className=" text-title-sm font-semibold text-primary">
                                {formattedExpectedSales}
                            </div>
                            <div className="text-title-xsm">
                                예상 월 매출액(원)
                            </div>
                        </div>
                        <div className="text-center">
                            <div className="text-black text-title-sm font-semibold text-primary">
                                {formattedCurrentSales}
                            </div>
                            <div className="text-title-xsm">
                                누적 월 매출액(원)
                            </div>
                        </div>
                    </div>
                </div>

                <div className="mb-4">
                    <div className="font-semibold text-black text-title-sm mb-0">
                        배송중인 재료
                    </div>
                    <div className="text-title-xsm mb-3">
                        배송중인 재료를 나타냅니다.
                    </div>
                    <div className="flex flex-justify pl-5 text-center justify-center gap-4">
                        {monthReportData[0].on_delivery_ingredients.map(
                            (ingredient, index) => (
                                <div
                                    key={index}
                                    className="text-primary text-title-sm font-semibold"
                                >
                                    {ingredient.ingredient_name}
                                </div>
                            ),
                        )}
                    </div>
                </div>
                <div className="mb-4">
                    <div className="font-semibold text-black text-title-sm mb-0">
                        유통기한 임박 재료
                    </div>
                    <div className="text-title-xsm mb-3">
                        유통기한 임박 재료에 대한 솔루션을 제공합니다.
                    </div>
                    <div>
                        {monthReportData[0].expiration_specifics.map((item) => (
                            <ExpirationList
                                key={item.ingredient_id}
                                item={item}
                            />
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default DashboardReport;

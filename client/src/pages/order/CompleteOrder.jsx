import { GetOredered } from "../../apis/server/OrderApi";
import ProgressOrderTable from "../../components/order/ProgressOrderTable";

const CompleteOrder = () => {
    const { data: Ordered } = GetOredered();
    return (
        <>
            <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                    완료된 발주 (table 바꿔야함)
                </h2>
                <ol className="flex items-center gap-2">
                    <li>발주 /</li>
                    <li className="font-bold text-primary">완료된 발주</li>
                </ol>
            </div>
            <ProgressOrderTable Ordered={Ordered} />
        </>
    );
};

export default CompleteOrder;

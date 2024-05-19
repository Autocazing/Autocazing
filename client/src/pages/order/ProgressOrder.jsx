import ProgressOrderTable from "../../components/order/ProgressOrderTable";

const ProgressOrder = () => {
    return (
        <>
            <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                    진행중인 발주
                </h2>
                <ol className="flex items-center gap-2">
                    <li>발주 /</li>
                    <li className="font-bold text-primary">진행중인 발주</li>
                </ol>
            </div>
            <ProgressOrderTable />
        </>
    );
};

export default ProgressOrder;

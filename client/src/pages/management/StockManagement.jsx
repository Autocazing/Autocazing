import StockTable from "../../components/management/StockTable";

const StockManagement = () => {
    return (
        <>
            <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                    재고 관리
                </h2>
                <ol className="flex items-center gap-2">
                    <li>가게 관리 /</li>
                    <li className="font-bold text-primary">재고 관리</li>
                </ol>
            </div>
            <StockTable />
        </>
    );
};

export default StockManagement;

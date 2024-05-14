import StockListInfo from "./StockListInfo";

const StockTable = ({ stockInfo }) => {
    if (!stockInfo || stockInfo.length === 0) {
        return <div>No stock data available.</div>;
    }
    return (
        <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
            <div className="flex-col gap-3 flex sm:flex-row sm:items-center sm:justify-between">
                <h4 className="mb-6 text-xl font-semibold text-black dark:text-white flex-row">
                    Stock List
                </h4>
            </div>

            <div
                className="flex flex-col overflow-auto"
                style={{ height: "35rem" }}
            >
                <div className="grid grid-cols-5 rounded-sm bg-gray-2 dark:bg-meta-4 sm:grid-cols-5">
                    <div className="p-2.5  text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-sm">
                            품목명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-sm">
                            유통기한
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-sm">
                            총량
                        </h5>
                    </div>
                    <div className="p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-sm">
                            발주 예정 수량
                        </h5>
                    </div>
                    <div className="p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-sm">
                            수정/삭제
                        </h5>
                    </div>
                </div>

                {stockInfo.map((stock, index) => (
                    <StockListInfo
                        key={index}
                        stock={stock}
                        isLastItem={index === stockInfo.length - 1}
                    />
                ))}
            </div>
        </div>
    );
};

export default StockTable;

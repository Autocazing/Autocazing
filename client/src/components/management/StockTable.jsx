import modifyIcon from "../../images/orderlist/modify.svg";
import deleteIcon from "../../images/orderlist/delete.svg";
import testmilk from "../../images/management/testmilk.jpg";

const StockList = [
    // 테스트용
    {
        name: "우유(1L)",
        expiration: "2024-04-27",
        amout: 3,
        PRcount: 5,
    },
    {
        name: "우유(1L)",
        expiration: "2024-04-29",
        amout: 7,
        PRcount: 5,
    },
    {
        name: "우유(1L)",
        expiration: "2024-05-02",
        amout: 11,
        PRcount: 5,
    },
    {
        name: "원두(2KG)",
        expiration: "2024-04-27",
        amout: 8,
        PRcount: 3,
    },
    {
        name: "우유(1L)",
        expiration: "2024-04-27",
        amout: 2,
        PRcount: 0,
    },
    {
        name: "우유(1L)",
        expiration: "2024-05-02",
        amout: 11,
        PRcount: 5,
    },
    {
        name: "원두(2KG)",
        expiration: "2024-04-27",
        amout: 8,
        PRcount: 3,
    },
    {
        name: "우유(1L)",
        expiration: "2024-04-27",
        amout: 2,
        PRcount: 0,
    },
    {
        name: "우유(1L)",
        expiration: "2024-04-27",
        amout: 2,
        PRcount: 0,
    },
    {
        name: "우유(1L)",
        expiration: "2024-05-02",
        amout: 11,
        PRcount: 5,
    },
    {
        name: "원두(2KG)",
        expiration: "2024-04-27",
        amout: 8,
        PRcount: 3,
    },
    {
        name: "우유(1L)",
        expiration: "2024-04-27",
        amout: 2,
        PRcount: 0,
    },
];

const StockTable = () => {
    return (
        <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
            <div className="flex-col gap-3 flex sm:flex-row sm:items-center sm:justify-between">
                <h4 className="mb-6 text-xl font-semibold text-black dark:text-white flex-row">
                    Stock List
                </h4>
                <button className="bg-transparent hover:bg-blue-500 text-blue-700 font-semibold hover:text-white py-2 px-4 border border-blue-500 hover:border-transparent rounded">
                    재고추가
                </button>
            </div>

            <div
                className="flex flex-col overflow-auto"
                style={{ height: "35rem" }}
            >
                <div className="grid grid-cols-3 rounded-sm bg-gray-2 dark:bg-meta-4 sm:grid-cols-5">
                    <div className="p-2.5 xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            품목명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            유통기한
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            총량
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            발주 예정 수량
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            수정/삭제
                        </h5>
                    </div>
                </div>

                {StockList.map((stock, key) => (
                    <div
                        className={`grid grid-cols-3 sm:grid-cols-5 ${
                            key === StockList.length - 1
                                ? ""
                                : "border-b border-stroke dark:border-strokedark"
                        }`}
                        key={key}
                    >
                        <div className="flex items-center gap-3 p-2.5 xl:p-5">
                            <div className="flex-shrink-0 h-1- w-10">
                                <img src={testmilk} alt="tmp"></img>
                            </div>
                            <p className="hidden text-black dark:text-white sm:block">
                                {stock.name}
                            </p>
                        </div>

                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {stock.expiration}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {stock.amout}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {stock.PRcount}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <button className="mr-2">
                                <img src={modifyIcon} alt="Modify" />
                            </button>
                            <button>
                                <img src={deleteIcon} alt="delete" />
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default StockTable;

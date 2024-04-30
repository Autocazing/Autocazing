const ProgressOrder = [
    // 테스트용
    {
        name: "우유(1L)",
        amount: 10,
        price: "20000",
        state: "배달 중",
        company: "동민상사",
        orderdate: "2024.04.26",
        arrivedate: "2024.05.01",
    },
    {
        name: "시리얼(1L)",
        amount: 3,
        price: "6000",
        state: "배달 전",
        company: "동민중사",
        orderdate: "2024.04.26",
        arrivedate: "2024.05.04",
    },
    {
        name: "초코가루(500g)",
        amount: 1,
        price: "5000",
        state: "배달 전",
        company: "동민하사",
        orderdate: "2024.04.26",
        arrivedate: "2024.05.10",
    },
    {
        name: "설탕(1kg)",
        amount: 5,
        price: "25000",
        state: "배달 전",
        company: "동민병장",
        orderdate: "2024.04.26",
        arrivedate: "2024.05.11",
    },
    {
        name: "밀가루(1kg)",
        amount: 1,
        price: "10000",
        state: "배달 중",
        company: "동민상병",
        orderdate: "2024.04.26",
        arrivedate: "2024.05.01",
    },
];

const ProgressOrderTable = () => {
    return (
        <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
            <h4 className="mb-6 text-xl font-semibold text-black dark:text-white">
                Progress Order List
            </h4>

            <div className="flex flex-col">
                <div className="grid grid-cols-3 rounded-sm bg-gray-2 dark:bg-meta-4 sm:grid-cols-7">
                    <div className="p-2.5 xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            품목명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            총량
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            가격(원)
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            상태
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            업체
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            발주일자
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            도착예정일
                        </h5>
                    </div>
                </div>

                {ProgressOrder.map((order, key) => (
                    <div
                        className={`grid grid-cols-3 sm:grid-cols-7 ${
                            key === ProgressOrder.length - 1
                                ? ""
                                : "border-b border-stroke dark:border-strokedark"
                        }`}
                        key={key}
                    >
                        <div className="flex items-center gap-3 p-2.5 xl:p-5">
                            <div className="flex-shrink-0">
                                사진
                                {/* <img src={brand.logo} alt="Brand" /> */}
                            </div>
                            <p className="hidden text-black dark:text-white sm:block">
                                {order.name}
                            </p>
                        </div>

                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {order.amount}
                            </p>
                        </div>

                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {order.price}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {order.state}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {order.company}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {order.orderdate}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {order.arrivedate}
                            </p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ProgressOrderTable;

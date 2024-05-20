import { useEffect, useState } from "react";

const ProgressOrderTable = ({ Ordered }) => {
    const [ProgressOrder, setProgressOrder] = useState([]);
    // name: "우유(1L)",
    // amount: 10,
    // price: "20000",
    // state: "배달 중",
    // company: "동민상사",
    // orderdate: "2024.04.26",
    // arrivedate: "2024.05.01",

    //console.log(Ordered);

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        return `${year}.${month}.${day}`;
    };

    useEffect(() => {
        if (Ordered !== undefined) {
            if (Ordered.length > 0) {
                setProgressOrder(Ordered);
            }
        }
    });
    return (
        <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
            <h4 className="mb-6 text-xl font-semibold text-black dark:text-white">
                Progress Order List
            </h4>

            <div
                style={{ height: "35rem" }}
                className="flex flex-col overflow-auto"
            >
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

                {ProgressOrder.map((order, orderIndex) => (
                    <div key={orderIndex}>
                        {order.specifics.map((specific, specificIndex) => (
                            <div
                                className={`grid grid-cols-3 sm:grid-cols-7 ${
                                    orderIndex === ProgressOrder.length - 1 &&
                                    specificIndex === order.specifics.length - 1
                                        ? ""
                                        : "border-b border-stroke dark:border-strokedark"
                                }`}
                                key={`${orderIndex}-${specificIndex}`}
                            >
                                <div className="flex items-center gap-3 p-2.5 xl:p-5">
                                    <p className="hidden text-black dark:text-white sm:block">
                                        {specific.ingredientName}
                                    </p>
                                </div>

                                <div className="flex items-center justify-center p-2.5 xl:p-5">
                                    <p className="text-black dark:text-white">
                                        {specific.ingredientQuanrtity.toLocaleString()}{" "}
                                        {/* 주문량 */}
                                    </p>
                                </div>

                                <div className="flex items-center justify-center p-2.5 xl:p-5">
                                    <p className="text-black dark:text-white">
                                        {specific.ingredientPrice.toLocaleString()}{" "}
                                        {/* 가격 */}
                                    </p>
                                </div>

                                <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                                    <p className="text-black dark:text-white">
                                        {specific.restockSpecificStatus ===
                                        "ORDERED"
                                            ? "배달전"
                                            : specific.restockSpecificStatus ===
                                              "ON_DELIVERY"
                                            ? "배달중"
                                            : specific.restockSpecificStatus ===
                                              "ARRIVED"
                                            ? "배달완료"
                                            : specific.restockSpecificStatus}
                                    </p>
                                </div>

                                <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                                    <p className="text-black dark:text-white">
                                        {specific.venderName} {/* 업체 */}
                                    </p>
                                </div>

                                <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                                    <p className="text-black dark:text-white">
                                        {formatDate(specific.updatedAt)}{" "}
                                        {/* 발주일자 */}
                                    </p>
                                </div>

                                <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                                    <p className="text-black dark:text-white">
                                        {formatDate(specific.arrivedAt)}{" "}
                                        {/* 도착예정일 */}
                                    </p>
                                </div>
                            </div>
                        ))}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ProgressOrderTable;

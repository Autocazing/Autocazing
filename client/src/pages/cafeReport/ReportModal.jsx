import Modal from "react-modal";
import ExpirationList from "./ExpirationList";

const customStyles = {
    overlay: {
        backgroundColor: "rgba(0, 0, 0, 0.4)",
        width: "100%",
        height: "100vh",
        zIndex: "10",
        position: "fixed",
        top: "0",
        left: "0",
    },
    content: {
        width: "30rem",

        zIndex: "150",
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        borderRadius: "10px",
        boxShadow: "2px 2px 2px rgba(0, 0, 0, 0.25)",
        backgroundColor: "white",
        padding: "20px",
        display: "flex",
        flexDirection: "column",
        justifyContent: "start",
        overflow: "auto",
    },
};

const ReportModal = ({ isOpen, onClose, data }) => {
    // 날짜 포맷팅
    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date
            .toLocaleDateString("ko-KR", {
                year: "numeric",
                month: "2-digit",
                day: "2-digit",
            })
            .replace(/\./g, "")
            .replace(/(\d{4})(\d{2})(\d{2})/, "$1.$2.$3");
    };

    const formattedExpectedSales =
        data.expected_monthly_sales.toLocaleString("ko-KR");
    const formattedCurrentSales =
        data.current_monthly_sales.toLocaleString("ko-KR");

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onClose}
            style={customStyles}
            contentLabel="Report Details"
            ariaHideApp={false}
            shouldCloseOnOverlayClick={true}
        >
            <div className=" col-span-12 rounded-sm border border-stroke bg-white p-7.5 shadow-default dark:border-strokedark dark:bg-boxdark xl:col-span-4">
                <div>
                    <h1 className="text-3xl mb-8 font-semibold text-black dark:text-white">
                        {formatDate(data.created_at)} 리포트
                    </h1>
                </div>
                <div>
                    <div className="mb-4">
                        <div className="font-semibold text-black text-title-sm mb-0">
                            예상 매출액
                        </div>
                        <div className="text-title-xsm">
                            AI 활용해 매출액을 예상 합니다.
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

                        {data.on_delivery_ingredients.length > 0 ? (
                            <div className="flex flex-justify pl-5 text-center justify-center gap-4">
                                {data.on_delivery_ingredients.map(
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
                        ) : (
                            <div className="flex flex-justify my-8 pl-5 text-center justify-center gap-4">
                                배송중인 재료가 없습니다.
                            </div>
                        )}
                    </div>
                    <div className="mb-4">
                        <div className="font-semibold text-black text-title-sm mb-0">
                            유통기한 임박 재료
                        </div>
                        <div className="text-title-xsm mb-3">
                            유통기한 임박 재료에 대한 솔루션을 제공합니다.
                        </div>

                        {data.expiration_specifics.length > 0 ? (
                            <div>
                                {data.expiration_specifics.map((item) => (
                                    <ExpirationList
                                        key={item.ingredient_id}
                                        item={item}
                                        solution={data.ingredient_solutions}
                                    />
                                ))}
                            </div>
                        ) : (
                            <div className="flex flex-justify pl-5 my-8 text-center justify-center gap-4">
                                유통기한 임박 재료가 없습니다.
                            </div>
                        )}
                    </div>
                </div>
                <button
                    onClick={onClose}
                    className="flex w-full mt-8 justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90 "
                >
                    닫기
                </button>
            </div>
        </Modal>
    );
};

export default ReportModal;

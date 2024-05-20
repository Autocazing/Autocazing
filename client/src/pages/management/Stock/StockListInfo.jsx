import modifyIcon from "../../../images/orderlist/modify.svg";
import deleteIcon from "../../../images/orderlist/delete.svg";

import { useState } from "react";
import { StockDeleteApi } from "../../../apis/server/StockApi";
import StockPutModal from "./StockPutModal";
import Swal from "sweetalert2";

const StockListInfo = ({ stock, isLastItem }) => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const deleteStock = StockDeleteApi(stock.stockId);

    const handleDelete = () => {
        Swal.fire({
            title: `"${stock.ingredientName}"를 삭제하시겠습니까?`,
            text: "이 작업은 되돌릴 수 없습니다!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3C50E0",
            cancelButtonColor: "#d33",
            confirmButtonText: "삭제",
            cancelButtonText: "취소",
            reverseButtons: true,
        }).then((result) => {
            if (result.isConfirmed) {
                deleteStock.mutate(null, {
                    onSuccess: () => {
                        Swal.fire({
                            title: "삭제 완료!",
                            text: "재고가 성공적으로 삭제되었습니다.",
                            icon: "success",
                            confirmButtonColor: "#3C50E0", // 버튼 색상 설정
                            iconColor: "#3C50E0", // 아이콘 색상 설정
                        });
                    },
                    onError: () => {
                        Swal.fire(
                            "오류 발생!",
                            "재고를 삭제하는 도중 오류가 발생했습니다.",
                            "error",
                        );
                    },
                });
            }
        });
    };

    return (
        <div
            className={`grid grid-cols-5 sm:grid-cols-5 ${
                isLastItem
                    ? ""
                    : "border-b border-stroke dark:border-strokedark"
            }`}
            key={stock.stockId}
        >
            <div className="flex items-center justify-center gap-3 p-2.5 xl:p-5">
                {/* <div className="hidden flex-shrink-0 h-1- w-10 sm:block">
                    <img src={stock.image} alt="tmp"></img>
                </div> */}
                <p className=" text-black dark:text-white text-cen sm:block">
                    {stock.ingredientName}
                </p>
            </div>

            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {stock.expirationDate}
                </p>
            </div>

            <div className="items-center justify-center p-2.5 sm:flex   xl:p-5">
                <p className="text-black dark:text-white text-center">
                    {stock.quantity}
                </p>
            </div>

            <div className="items-center justify-center p-2.5 sm:flex xl:p-5">
                <p className="text-black dark:text-white text-center">
                    {stock.deliveringCount}
                </p>
            </div>

            <div className="items-center justify-center p-2.5 sm:flex xl:p-5">
                <button
                    onClick={() => setModalIsOpen(true)}
                    className="mr-2 sm:ml-8"
                >
                    <img src={modifyIcon} alt="Modify" />
                </button>
                <button onClick={handleDelete}>
                    <img src={deleteIcon} alt="delete" />
                </button>
                {modalIsOpen && (
                    <StockPutModal
                        isOpen={modalIsOpen}
                        onClose={() => setModalIsOpen(false)}
                        initialValue={stock}
                    />
                )}
            </div>
        </div>
    );
};

export default StockListInfo;

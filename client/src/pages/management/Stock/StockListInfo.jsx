import modifyIcon from "../../../images/orderlist/modify.svg";
import deleteIcon from "../../../images/orderlist/delete.svg";

import { useState } from "react";
import { StockDeleteApi } from "../../../apis/server/StockApi";
import StockPutModal from "./StockPutModal";

const StockListInfo = ({ stock, isLastItem }) => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const deleteStock = StockDeleteApi(stock.stockId);

    const handleDelete = () => {
        deleteStock.mutate();
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

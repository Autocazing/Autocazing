import StockTable from "./StockTable";
import StockManagementModal from "./StockManagementModal";
import { useState } from "react";

import { StockGetApi } from "../../../apis/server/StockApi";
const StockManagement = () => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const { data: stockInfo, isLoading, isError, error } = StockGetApi();
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
            <StockTable stockInfo={stockInfo} />
            <div className="flex justify-end mt-6">
                <button
                    onClick={() => setModalIsOpen(true)}
                    className="bg-transparent hover:bg-primary text-primary font-semibold hover:text-white py-2 px-4 border border-primary hover:border-transparent rounded"
                >
                    재고추가
                </button>
            </div>
            {modalIsOpen && (
                <StockManagementModal
                    isOpen={modalIsOpen}
                    onClose={() => setModalIsOpen(false)}
                    initialValue={[]}
                />
            )}
        </>
    );
};

export default StockManagement;

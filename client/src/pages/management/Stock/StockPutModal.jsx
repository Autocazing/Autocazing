import Modal from "react-modal";
import { useEffect, useState } from "react";
import closeIcon from "../../../images/icon/close.svg";
import { MaterialGetApi } from "../../../apis/server/MaterialApi";
import { StockEditApi } from "../../../apis/server/StockApi";

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
        height: "45%",
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
        overflow: "auto",
    },
};

const StockPutModal = ({ isOpen, onClose, initialValue }) => {
    const [stockPutData, setStockPutData] = useState({
        ingredientId: initialValue.ingredientId || 0,
        quantity: initialValue.quantity || 0,
        expirationDate: initialValue.expirationDate || "",
    });

    const { data: materialInfo, isLoading, isError, error } = MaterialGetApi();
    const editStock = StockEditApi(initialValue.stockId);
    const handleEdit = (e) => {
        editStock.mutate(stockPutData);
        onClose();
    };
    const handleChange = (e) => {
        const { name, value, type } = e.target;
        const newValue = type === "number" ? parseInt(value, 10) || 0 : value;
        setStockPutData((prevState) => ({
            ...prevState,
            [name]: newValue,
        }));
    };
    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onClose}
            style={customStyles}
            contentLabel="Add Company"
            ariaHideApp={false}
        >
            <div className="flex justify-end">
                <button
                    onClick={onClose}
                    style={{
                        background: "transparent",
                        border: "none",
                        cursor: "pointer",
                    }}
                    aria-label="Close modal"
                >
                    <img
                        src={closeIcon}
                        alt="Close"
                        style={{ width: "24px", height: "24px" }}
                    />
                </button>
            </div>

            <h1 className="text-center text-3xl my-4 font-semibold text-black dark:text-white">
                재고 수정
            </h1>

            <div className="p-6.5">
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        품목명
                    </label>
                    <select
                        value={stockPutData.ingredientId}
                        name="ingredientId"
                        onChange={handleChange}
                        disabled={true}
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    >
                        {materialInfo &&
                            materialInfo.map((material) => (
                                <option
                                    key={material.ingredientId}
                                    value={material.ingredientId}
                                >
                                    {material.ingredientName}
                                </option>
                            ))}
                    </select>
                </div>
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        총량
                    </label>
                    <input
                        value={stockPutData.quantity}
                        onChange={handleChange}
                        name="quantity"
                        type="number"
                        placeholder="총량 입력"
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div>
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        유통기한
                    </label>
                    <input
                        value={stockPutData.expirationDate}
                        name="expirationDate"
                        type="date"
                        onChange={handleChange}
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div>

                <button
                    onClick={handleEdit}
                    className="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90 "
                >
                    수정하기
                </button>
            </div>
        </Modal>
    );
};

export default StockPutModal;

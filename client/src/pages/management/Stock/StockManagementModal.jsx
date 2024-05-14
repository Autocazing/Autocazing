import Modal from "react-modal";
import { useEffect, useState } from "react";
import closeIcon from "../../../images/icon/close.svg";
import ExcelJS from "exceljs";
import { MaterialGetApi } from "../../../apis/server/MaterialApi";
import { StockEditApi, StockPostApi } from "../../../apis/server/StockApi";

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
        height: "65%",
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
const StockManagementModal = ({ isOpen, onClose, initialValue }) => {
    const [stockPostData, setStockPostData] = useState([
        {
            quantity: initialValue.quantity || 0,
            expirationDate: initialValue.expirationDate || "",
            ingredientId: initialValue.ingredientId || 0,
        },
    ]);

    const [productNames, setProductNames] = useState({});

    useEffect(() => {
        console.log(stockPostData);
    }, [stockPostData]);

    const { data: materialInfo, isLoading, isError, error } = MaterialGetApi();
    const postStock = StockPostApi();
    const editStock = StockEditApi(initialValue.stockId);

    const handleSubmit = (e) => {
        postStock.mutate(stockPostData);
        onClose();
    };

    const handleEdit = (e) => {
        editStock.mutate(stockPostData);
        onClose();
    };

    const handleInputChange = (e) => {
        const { name, value, type } = e.target;
        setStockPostData((prevState) => ({
            ...prevState,
            [name]: type === "number" ? parseInt(value, 10) || 0 : value,
        }));
        // console.log(stockPostData);
    };

    const handleSelectChange = (e) => {
        setStockPostData((prevState) => ({
            ...prevState,
            ingredientId: parseInt(e.target.value, 10),
        }));
    };

    const handleFileSelect = (e) => {
        const selectedFile = e.target.files[0];

        const workbook = new ExcelJS.Workbook();
        const reader = new FileReader();

        reader.onload = async (e) => {
            const data = new Uint8Array(e.target.result);
            await workbook.xlsx.load(data);

            const worksheet = workbook.getWorksheet(1);
            const newData = [];
            const newProductNames = {};

            worksheet.eachRow((row, rowNumber) => {
                if (rowNumber === 1) return; // 첫 번째 행을 건너뜀
                const rowData = row.values.filter((value) => value != null);
                const [name, period, volume] = rowData.slice(0, 3);

                const expirationDate =
                    period instanceof Date
                        ? `${period.getFullYear()}-${(period.getMonth() + 1)
                              .toString()
                              .padStart(2, "0")}-${period
                              .getDate()
                              .toString()
                              .padStart(2, "0")}`
                        : period;

                const ingredientData = materialInfo.find(
                    (material) => material.ingredientName === name,
                );
                const ingredientId = ingredientData
                    ? ingredientData.ingredientId
                    : null;

                if (ingredientId !== null) {
                    newData.push({
                        ingredientId,
                        quantity: volume,
                        expirationDate,
                    });
                    newProductNames[ingredientId] = name;
                }
            });

            setStockPostData(newData);
            setProductNames(newProductNames);
        };

        reader.readAsArrayBuffer(selectedFile);
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
            <h1 className="text-3xl my-4 font-semibold text-black dark:text-white">
                재고추가
            </h1>
            <div className="p-6.5">
                {stockPostData.some((item) => item.quantity > 0) ? (
                    <table className="min-w-full leading-normal mb-6">
                        <thead>
                            <tr>
                                <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                                    품목명
                                </th>
                                <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                                    총량
                                </th>
                                <th className="px-5 py-3 border-b-2 border-gray-200 bg-gray-100 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">
                                    유통기한
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            {stockPostData.map((item, index) => (
                                <tr key={index}>
                                    <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                                        {productNames[item.ingredientId]}
                                    </td>
                                    <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                                        {item.quantity}
                                    </td>
                                    <td className="px-5 py-5 border-b border-gray-200 bg-white text-sm">
                                        {item.expirationDate}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                ) : (
                    <p className="text-gray-800 font-semibold text-center text-lg mt-10 mb-10">
                        영수증 파일이 있다면 파일을 넣어주세요.
                    </p>
                )}

                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        영주증 파일
                    </label>
                    <input
                        onChange={handleFileSelect}
                        type="file"
                        className="w-full cursor-pointer rounded-lg border-[1.5px] border-stroke bg-transparent outline-none transition file:mr-5 file:border-collapse file:cursor-pointer file:border-0 file:border-r file:border-solid file:border-stroke file:bg-whiter file:py-3 file:px-5 file:hover:bg-primary file:hover:bg-opacity-10 focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:file:border-form-strokedark dark:file:bg-white/30 dark:file:text-white dark:focus:border-primary"
                    />
                </div>
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        품목명
                    </label>
                    <select
                        name="ingredientId"
                        onChange={handleSelectChange}
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    >
                        <option value="" disabled>
                            재고 추가할 품목을 선택해주세요
                        </option>
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
                        name="quantity"
                        onChange={handleInputChange}
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
                        name="expirationDate"
                        onChange={handleInputChange}
                        type="date"
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div>
                {/* <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        발주 예정 수량
                    </label>
                    <input
                        name="predictOrder"
                        onChange={handleInputChange}
                        type="number"
                        placeholder="발주 예정 수량 입력"
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div> */}

                {Object.keys(initialValue).length === 0 ? (
                    <button
                        onClick={handleSubmit}
                        className="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90 "
                    >
                        추가하기
                    </button>
                ) : (
                    <button
                        onClick={handleEdit}
                        className="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90 "
                    >
                        수정하기
                    </button>
                )}
            </div>
        </Modal>
    );
};

export default StockManagementModal;

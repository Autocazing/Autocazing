import Modal from "react-modal";
import { useState } from "react";
import closeIcon from "../../images/icon/close.svg";
import ExcelJS from "exceljs";
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
const StockManagementModal = ({ isOpen, onClose }) => {
    const [stockPostData, setStockPostData] = useState([
        {
            name: "",
            volume: 0,
            period: "",
            predictOrder: 0,
        },
    ]);

    const handleInputChange = (e) => {
        const { name, value, type } = e.target;
        setStockPostData((prevState) => ({
            ...prevState,
            [name]: type === "number" ? parseInt(value, 10) || 0 : value,
        }));
        // console.log(stockPostData);
    };

    const fileselect = (e) => {
        const selectedfile = e.target.files[0]; // 선택된 파일 가져오기

        //파일 읽기
        const workbook = new ExcelJS.Workbook();
        const render = new FileReader();

        render.onload = async (e) => {
            const data = new Uint8Array(e.target.result);
            await workbook.xlsx.load(data); // 엑셀 파일 로드

            const worksheet = workbook.getWorksheet(1);

            // 여기서 데이터 처리해야함
            worksheet.eachRow((row, rowNumber) => {
                if (rowNumber === 1) return;
                const rowData = row.values.filter((value) => value != null);
                const [name, expiration, amount, PRcount] = rowData.slice(0);

                const item = {
                    name,
                    expiration,
                    amount,
                    PRcount,
                };

                setStockPostData(item);
            });
        };

        // 파일을 ArrayBuffer로 읽음
        render.readAsArrayBuffer(selectedfile);
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
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        영주증 파일
                    </label>
                    <input
                        onChange={fileselect}
                        type="file"
                        className="w-full cursor-pointer rounded-lg border-[1.5px] border-stroke bg-transparent outline-none transition file:mr-5 file:border-collapse file:cursor-pointer file:border-0 file:border-r file:border-solid file:border-stroke file:bg-whiter file:py-3 file:px-5 file:hover:bg-primary file:hover:bg-opacity-10 focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:file:border-form-strokedark dark:file:bg-white/30 dark:file:text-white dark:focus:border-primary"
                    />
                </div>
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        품목명
                    </label>
                    <select
                        name="name"
                        onChange={handleInputChange}
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    >
                        <option
                            value=""
                            disabled
                            className="text-body dark:text-bodydark"
                        >
                            재고 추가할 품목을 선택해주세요
                        </option>
                        <option
                            value="우유"
                            className="text-body dark:text-bodydark"
                        >
                            우유
                        </option>
                        <option
                            value="원두"
                            className="text-body dark:text-bodydark"
                        >
                            원두
                        </option>
                        <option
                            value="연유"
                            className="text-body dark:text-bodydark"
                        >
                            연유
                        </option>
                    </select>
                </div>
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        총량
                    </label>
                    <input
                        name="volume"
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
                        name="period"
                        onChange={handleInputChange}
                        type="date"
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div>
                <div className="mb-4.5">
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
                </div>

                <button className="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90 ">
                    추가하기
                </button>
            </div>
        </Modal>
    );
};

export default StockManagementModal;

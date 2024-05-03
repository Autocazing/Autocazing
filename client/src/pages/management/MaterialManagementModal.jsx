import Modal from "react-modal";
import { useState } from "react";
import closeIcon from "../../images/icon/close.svg";

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
const MaterialManagementModal = ({ isOpen, onClose }) => {
    const [materialPostData, setMaterialPostData] = useState({
        picture: "",
        name: "",
        price: 0,
        volume: 0,
        unit: "",
        minimum: 0,
        orderVolume: 0,
        period: 0,
        companyName: "",
    });
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
                재료추가
            </h1>
            <div className="p-6.5">
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        제품 사진
                    </label>
                    <input
                        type="file"
                        className="w-full cursor-pointer rounded-lg border-[1.5px] border-stroke bg-transparent outline-none transition file:mr-5 file:border-collapse file:cursor-pointer file:border-0 file:border-r file:border-solid file:border-stroke file:bg-whiter file:py-3 file:px-5 file:hover:bg-primary file:hover:bg-opacity-10 focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:file:border-form-strokedark dark:file:bg-white/30 dark:file:text-white dark:focus:border-primary"
                    />
                </div>
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        재료명
                    </label>
                    <input
                        type="text"
                        placeholder="재료명을 입력해주세요."
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div>
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        재료가격
                    </label>
                    <input
                        type="number"
                        placeholder="재료 가격을 입력해주세요."
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div>
                <div className="mb-4.5 flex flex-col gap-6 xl:flex-row">
                    <div className="w-full xl:w-1/2">
                        <label className="mb-2.5 block text-black dark:text-white">
                            용량
                        </label>
                        <input
                            type="number"
                            placeholder="제품의 용량을 입력해주세요"
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>

                    <div className="w-full xl:w-1/2">
                        <label className="mb-2.5 block text-black dark:text-white">
                            단위
                        </label>
                        <select className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary">
                            <option
                                value=""
                                disabled
                                className="text-body dark:text-bodydark"
                            >
                                단위를 선택해주세요.
                            </option>
                            <option
                                value="g"
                                className="text-body dark:text-bodydark"
                            >
                                g
                            </option>
                            <option
                                value="kg"
                                className="text-body dark:text-bodydark"
                            >
                                kg
                            </option>
                            <option
                                value="L"
                                className="text-body dark:text-bodydark"
                            >
                                L
                            </option>
                            <option
                                value="ml"
                                className="text-body dark:text-bodydark"
                            >
                                ml
                            </option>
                            <option
                                value="직접입력"
                                className="text-body dark:text-bodydark"
                            >
                                직접입력
                            </option>
                        </select>
                    </div>
                </div>

                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        재료 최소 개수
                    </label>
                    <input
                        type="number"
                        placeholder="재료 최소 개수를 입력해주세요."
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div>

                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        자동 발주 수량
                    </label>
                    <input
                        type="number"
                        placeholder="자동으로 발주시킬 수량을 입력해주세요."
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div>
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        담당 업체명
                    </label>
                    <select className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary">
                        <option
                            value=""
                            disabled
                            className="text-body dark:text-bodydark"
                        >
                            담당 업체를 선택해주세요.
                        </option>
                        <option
                            value="지호원두"
                            className="text-body dark:text-bodydark"
                        >
                            지호 원두
                        </option>
                        <option
                            value="동민 원두"
                            className="text-body dark:text-bodydark"
                        >
                            동민 원두
                        </option>
                        <option
                            value="영후 원두"
                            className="text-body dark:text-bodydark"
                        >
                            영후 원두
                        </option>
                    </select>
                </div>

                <button className="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90 ">
                    추가하기
                </button>
            </div>
        </Modal>
    );
};

export default MaterialManagementModal;

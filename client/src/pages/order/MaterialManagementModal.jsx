import Modal from "react-modal";
import { useEffect, useState } from "react";
// import closeIcon from "../../../images/icon/close.svg";
import closeIcon from "../../images/icon/close.svg";
// import { CompanyGetApi } from "../../../apis/server/CompanyApi";
const customStyles = {
    overlay: {
        backgroundColor: "rgba(0, 0, 0, 0.1)",
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
const MaterialManagementModal = ({ isOpen, onClose, initialValue }) => {
    const [count, setCount] = useState(initialValue.ingredientQuantity);

    const ButtonClick = () => {
        console.log(count);
        return 0;
    };

    const CountChange = (newValue) => {
        // 값이 변경될 때 상태 업데이트
        setCount(newValue);
        // 상태가 변경될 때 부모 컴포넌트로 값 전달
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
                수정
            </h1>
            <div className="p-6.5">
                <div className="mb-0.5 flex flex-col gap-6 xl:flex-row">
                    <div className="mb-4.5" style={{ width: "100%" }}>
                        <label className="mb-2.5 block text-black dark:text-white">
                            재료명
                        </label>
                        <input
                            name="ingredientName"
                            readOnly
                            defaultValue={initialValue.ingredientName}
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>
                </div>

                <div className="mb-1 flex flex-col gap-6 xl:flex-row">
                    <div className="mb-4.5" style={{ width: "100%" }}>
                        <label className="mb-2.5 block text-black dark:text-white">
                            재료 개수
                        </label>
                        <input
                            name="minimumCount"
                            value={count}
                            onChange={(e) => CountChange(e.target.value)}
                            type="number"
                            placeholder="개수 입력"
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>
                </div>

                <button
                    onClick={ButtonClick}
                    className="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90 "
                >
                    수정하기
                </button>
            </div>
        </Modal>
    );
};

export default MaterialManagementModal;

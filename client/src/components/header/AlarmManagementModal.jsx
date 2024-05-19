import Modal from "react-modal";
import { useState } from "react";
import closeIcon from "../../images/icon/close.svg";
import { useQueryClient } from "@tanstack/react-query";
import { PutStatus } from "../../apis/server/OrderApi";

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
        height: "24%",
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

const AlarmManagementModal = ({ isOpen, onClose }) => {
    const queryClient = useQueryClient();
    const [putNo, setPutNo] = useState(0);

    const handleOrder = () => {
        // 발주하는 코드

        queryClient.invalidateQueries("GetBasket"); // 발주하고 장바구니 초기화
        onClose();
    };

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onClose}
            style={customStyles}
            contentLabel="Alarm Management"
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
            <div className="text-center text-xl text-black">
                자동 발주를 진행하시겠습니까?
            </div>
            <div className="flex mt-5 space-x-4 justify-center">
                <button
                    onClick={handleOrder}
                    className="w-20 justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90 "
                >
                    YES
                </button>
                <button
                    onClick={onClose}
                    className="w-20 justify-center rounded bg-red-400 p-3 font-medium text-gray hover:bg-opacity-90 "
                >
                    NO
                </button>
            </div>
        </Modal>
    );
};

export default AlarmManagementModal;

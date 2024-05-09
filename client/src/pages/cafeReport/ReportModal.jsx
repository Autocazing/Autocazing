import Modal from "react-modal";

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
        height: "70%",
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
        justifyContent: "space-between",
        overflow: "auto",
    },
};

const ReportModal = ({ isOpen, onClose, data }) => {
    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onClose}
            style={customStyles}
            contentLabel="Report Details"
            ariaHideApp={false}
        >
            <div>
                <div>
                    <h1 className="text-3xl my-4 font-semibold text-black dark:text-white">
                        {data.date} 리포트
                    </h1>
                </div>
                <div>
                    <p>어제: {data.yesterday}</p>
                    <p>예측: {data.predict}</p>
                </div>
            </div>

            <button
                onClick={onClose}
                className="mt-auto mx-auto inline-flex items-center justify-center rounded-md bg-primary py-2 px-4 text-center font-medium text-white hover:bg-opacity-90 focus:outline-none"
            >
                닫기
            </button>
        </Modal>
    );
};

export default ReportModal;

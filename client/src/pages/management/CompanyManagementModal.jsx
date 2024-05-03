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

const CompanyManagementModal = ({ isOpen, onClose }) => {
    const [companyPostData, setCompanyPostData] = useState({
        companyName: "",
        name: "",
        email: "",
    });

    // 추가하기 버ㅌ느 누르면 companyPostData axios Post로 보내기
    const handleChange = (e) => {
        const { name, value } = e.target;
        setCompanyPostData((prevState) => ({
            ...prevState,
            [name]: value,
        }));

        // console.log(companyPostData);
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
                업체추가
            </h1>
            <form action="#">
                <div className="p-6.5">
                    <div className="mb-4.5">
                        <label className="mb-2.5 block text-black dark:text-white">
                            업체명
                        </label>
                        <input
                            type="text"
                            name="companyName"
                            value={companyPostData.companyName}
                            onChange={handleChange}
                            placeholder="업체명을 입력해주세요."
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>

                    <div className="mb-4.5">
                        <label className="mb-2.5 block text-black dark:text-white">
                            담당자 이름
                        </label>
                        <input
                            type="text"
                            name="name"
                            value={companyPostData.name}
                            onChange={handleChange}
                            placeholder="담당자 이름을 입력해주세요."
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>
                    <div className="mb-4.5">
                        <label className="mb-2.5 block text-black dark:text-white">
                            연락처 <span className="text-meta-1">*</span>
                        </label>
                        <input
                            type="email"
                            name="email"
                            onChange={handleChange}
                            value={companyPostData.email}
                            placeholder="담당자 연락처를 입력해주세요."
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>

                    <button className="flex w-full justify-center rounded bg-primary p-3 font-medium text-gray hover:bg-opacity-90 ">
                        추가하기
                    </button>
                </div>
            </form>
        </Modal>
    );
};

export default CompanyManagementModal;

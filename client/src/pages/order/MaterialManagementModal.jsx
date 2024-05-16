import Modal from "react-modal";
import { useEffect, useState } from "react";
// import closeIcon from "../../../images/icon/close.svg";
import closeIcon from "../../images/icon/close.svg";
import { CompanyGetApi } from "../../apis/server/CompanyApi";
// import { CompanyGetApi } from "../../../apis/server/CompanyApi";
import {
    MaterialScaleGetApi,
    MaterialEditApi,
} from "../../apis/server/MaterialApi";
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
    const [materialPostData, setMaterialPostData] = useState({
        storeId: 1,
        venderId: initialValue.venderId || 1, // vendorid 0은 아예 없는 값이여서 default값 1로 수정함
        ingredientName: initialValue.ingredientName || "",
        ingredientPrice: initialValue.ingredientPrice || 0,
        ingredientCapacity: initialValue.ingredientCapacity || 0,
        scale: initialValue.scale || { scaleId: 1, unit: "" },
        minimumCount: initialValue.minimumCount || 0,
        deliveryTime: initialValue.deliveryTime || 0,
        orderCount: initialValue.orderCount || 0,
        imageUrl: "",
    });

    const [isDirectInput, setIsDirectInput] = useState(false);

    const editMaterial = MaterialEditApi(initialValue.ingredientId);
    const {
        data: companyInfo,
        isLoading: companyLoading,
        isError: companyError,
        error: companyErrorInfo,
    } = CompanyGetApi();
    const {
        data: materialScaleInfo,
        isLoading: scaleLoading,
        isError: scaleError,
        error: scaleErrorInfo,
    } = MaterialScaleGetApi();

    // API 호출 결과를 기다리는 중에 조건부 렌더링을 하지 않도록 합니다.
    if (companyLoading || scaleLoading) {
        return <div>Loading...</div>; // 로딩 상태 처리
    }

    if (companyError || scaleError) {
        return (
            <div>
                {companyError
                    ? `Error loading company data: ${companyErrorInfo.message}`
                    : ""}
                {scaleError
                    ? `Error loading scale data: ${scaleErrorInfo.message}`
                    : ""}
            </div>
        ); // 에러 상태 처리
    }

    const handleEdit = (e) => {
        editMaterial.mutate(materialPostData);
        onClose();
    };

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        if (name === "venderId") {
            setMaterialPostData((prevState) => ({
                ...prevState,
                venderId: value, // 선택된 업체의 ID를 venderId로 저장
                // companyName: selectedText, // 선택된 업체의 이름을 저장, 필요하지 않으면 이 라인 제거
            }));
        } else {
            setMaterialPostData((prevState) => ({
                ...prevState,
                [name]: value,
            }));
        }

        console.log(value);
    };

    const handleUnitChange = (event) => {
        const { value } = event.target;
        if (value === "직접입력") {
            setIsDirectInput(true);
            setMaterialPostData((prevState) => ({
                ...prevState,
                scale: { scaleId: 0, unit: "" },
            }));
        } else {
            setIsDirectInput(false);
            const selectedScale = materialScaleInfo.find(
                (scale) => scale.unit === value,
            );
            setMaterialPostData((prevState) => ({
                ...prevState,
                scale: {
                    scaleId: selectedScale.scaleId,
                    unit: selectedScale.unit,
                },
            }));
        }
    };

    const handleDirectUnitInput = (event) => {
        const { value } = event.target;
        setMaterialPostData((prevState) => ({
            ...prevState,
            scale: { ...prevState.scale, unit: value },
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
            <h1 className="text-3xl my-4 font-semibold text-black dark:text-white">
                재료추가
            </h1>
            <div className="p-6.5">
                <div className="mb-0.5 flex flex-col gap-6 xl:flex-row">
                    <div className="mb-4.5">
                        <label className="mb-2.5 block text-black dark:text-white">
                            재료명
                        </label>
                        <input
                            name="ingredientName"
                            value={materialPostData.ingredientName}
                            onChange={handleInputChange}
                            type="text"
                            placeholder="재료명 입력"
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>
                    <div className="mb-4.5">
                        <label className="mb-2.5 block text-black dark:text-white">
                            재료가격
                        </label>
                        <input
                            name="ingredientPrice"
                            value={materialPostData.ingredientPrice}
                            onChange={handleInputChange}
                            type="number"
                            placeholder="재료 가격 입력"
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>
                </div>

                <div className="mb-4.5 flex flex-col gap-6 xl:flex-row">
                    <div className="w-full xl:w-1/2">
                        <label className="mb-2.5 block text-black dark:text-white">
                            용량
                        </label>
                        <input
                            name="ingredientCapacity"
                            value={materialPostData.ingredientCapacity}
                            onChange={handleInputChange}
                            type="number"
                            placeholder="제품 용량 입력"
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>
                </div>
                <div className="mb-1 flex flex-col gap-6 xl:flex-row">
                    <div className="mb-4.5">
                        <label className="mb-2.5 block text-black dark:text-white">
                            재료 최소 개수
                        </label>
                        <input
                            name="minimumCount"
                            value={materialPostData.minimumCount}
                            onChange={handleInputChange}
                            type="number"
                            placeholder="임계점 입력"
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>
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

export default MaterialManagementModal;

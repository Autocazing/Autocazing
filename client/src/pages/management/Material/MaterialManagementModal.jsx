import Modal from "react-modal";
import { useEffect, useState } from "react";
import closeIcon from "../../../images/icon/close.svg";
import { CompanyGetApi } from "../../../apis/server/CompanyApi";
import {
    MaterialScaleGetApi,
    MaterialPostApi,
    MaterialScalePostApi,
    MaterialEditApi,
} from "../../../apis/server/MaterialApi";
import Swal from "sweetalert2";
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
const MaterialManagementModal = ({ isOpen, onClose, initialValue }) => {
    const [materialPostData, setMaterialPostData] = useState({
        storeId: 1,
        venderId: initialValue.venderId || 0, // disable 속성 없애서 0으로 둠
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

    const postMaterial = MaterialPostApi();
    const postMaterialScale = MaterialScalePostApi();
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

    const handleSubmit = (e) => {
        postMaterial.mutate(materialPostData); // 재료 정보 추가
        postMaterialScale.mutate(materialPostData.scale); // 스케일 정보 추가
        onClose(); // 모달 닫기

        Swal.fire({
            // 성공 알림 표시
            title: "재료 추가 완료!",
            text: "재료 정보가 성공적으로 추가되었습니다.",
            icon: "success",
            iconColor: "#3C50E0",
            confirmButtonText: "확인",
            confirmButtonColor: "#3C50E0",
        });
    };

    const handleEdit = (e) => {
        editMaterial.mutate(materialPostData, {
            onSuccess: () => {
                Swal.fire({
                    title: "재료 수정 완료!",
                    text: "재료 정보가 성공적으로 수정되었습니다.",
                    icon: "success",
                    iconColor: "#3C50E0",
                    confirmButtonText: "확인",
                    confirmButtonColor: "#3C50E0",
                }).then((result) => {
                    if (result.isConfirmed) {
                        onClose();
                    }
                });
            },
        });
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

    // const handleInputChange = (event) => {
    //     const { name, value } = event.target;
    //     setMaterialPostData((prevState) => ({
    //         ...prevState,
    //         [name]: value,
    //     }));
    // };
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
            {Object.keys(initialValue).length === 0 ? (
                <h1 className="text-center text-3xl my-4 font-semibold text-black dark:text-white">
                    재료 추가
                </h1>
            ) : (
                <h1 className="text-center text-3xl my-4 font-semibold text-black dark:text-white">
                    재료 수정
                </h1>
            )}
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
                            min={0}
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
                            min={0}
                            placeholder="제품 용량 입력"
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>

                    <div className="w-full xl:w-1/2">
                        <label className="mb-2.5 block text-black dark:text-white">
                            단위
                        </label>
                        <select
                            name="unit"
                            value={
                                isDirectInput
                                    ? "직접입력"
                                    : materialPostData.scale.unit
                            }
                            onChange={handleUnitChange}
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        >
                            <option value="" disabled>
                                단위 선택
                            </option>
                            {materialScaleInfo.map((scale) => (
                                <option key={scale.scaleId} value={scale.unit}>
                                    {scale.unit}
                                </option>
                            ))}
                            <option value="직접입력">직접입력</option>
                        </select>
                        {isDirectInput && (
                            <input
                                type="text"
                                placeholder="단위 직접 입력"
                                className="mt-2 w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                                onChange={handleDirectUnitInput}
                            />
                        )}
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
                            min={0}
                            placeholder="임계점 입력"
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>

                    <div className="mb-4.5">
                        <label className="mb-2.5 block text-black dark:text-white">
                            자동 발주 수량
                        </label>
                        <input
                            name="orderCount"
                            value={materialPostData.orderCount}
                            onChange={handleInputChange}
                            type="number"
                            min={0}
                            placeholder="자동 발주 수량 입력"
                            className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                        />
                    </div>
                </div>
                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        배송 소요 기간
                    </label>
                    <input
                        name="deliveryTime"
                        value={materialPostData.deliveryTime}
                        onChange={handleInputChange}
                        type="number"
                        min={0}
                        placeholder="배송 소요 기간 입력"
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    />
                </div>

                <div className="mb-4.5">
                    <label className="mb-2.5 block text-black dark:text-white">
                        담당 업체명
                    </label>
                    <select
                        name="venderId"
                        value={materialPostData.venderId}
                        onChange={handleInputChange}
                        className="w-full rounded border-[1.5px] border-stroke bg-transparent py-3 px-5 text-black outline-none transition focus:border-primary active:border-primary disabled:cursor-default disabled:bg-whiter dark:border-form-strokedark dark:bg-form-input dark:text-white dark:focus:border-primary"
                    >
                        <option value="">담당 업체를 선택해주세요.</option>
                        {companyInfo.map((company) => (
                            <option
                                key={company.venderId}
                                value={company.venderId}
                            >
                                {company.venderName}
                            </option>
                        ))}
                    </select>
                </div>

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

export default MaterialManagementModal;

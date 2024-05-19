import modifyIcon from "../../../images/orderlist/modify.svg";
import deleteIcon from "../../../images/orderlist/delete.svg";
import { MaterialDeleteApi } from "../../../apis/server/MaterialApi";
import { useState } from "react";
import MaterialManagementModal from "./MaterialManagementModal";
import Swal from "sweetalert2";

const MaterialListInfo = ({ material, isLastItem }) => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const deleteMaterial = MaterialDeleteApi(material.ingredientId);
    const handleDelete = () => {
        Swal.fire({
            title: `"${material.ingredientName}"를 삭제하시겠습니까?`,
            text: "이 작업은 되돌릴 수 없습니다!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3C50E0",
            cancelButtonColor: "#d33",
            confirmButtonText: "삭제",
            cancelButtonText: "취소",
            reverseButtons: true,
        }).then((result) => {
            if (result.isConfirmed) {
                deleteMaterial.mutate(null, {
                    onSuccess: () => {
                        Swal.fire({
                            title: "삭제 완료!",
                            text: "재료가 성공적으로 삭제되었습니다.",
                            icon: "success",
                            confirmButtonColor: "#3C50E0", // 버튼 색상 설정
                            iconColor: "#3C50E0", // 아이콘 색상 설정
                        });
                    },
                    onError: () => {
                        Swal.fire(
                            "오류 발생!",
                            "재료를 삭제하는 도중 오류가 발생했습니다.",
                            "error",
                        );
                    },
                });
            }
        });
    };
    // 원화 포맷 함수
    const formatPrice = (price) => {
        return new Intl.NumberFormat("ko-KR").format(price);
    };
    return (
        <div
            className={`grid grid-cols-7 sm:grid-cols-7 text-xs sm:text-base ${
                isLastItem
                    ? ""
                    : "border-b border-stroke dark:border-strokedark"
            }`}
            key={material.ingredientId}
        >
            <div className="flex items-center text-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {material.ingredientName}
                </p>
            </div>

            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {formatPrice(material.ingredientPrice)}
                </p>
            </div>

            <div className=" flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {material.ingredientCapacity}
                    {material.scale.unit}
                </p>
            </div>

            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {material.minimumCount}
                </p>
            </div>

            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {material.deliveryTime}일
                </p>
            </div>

            <div className="flex items-center text-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {material.venderName}
                </p>
            </div>

            <div className="flex items-center text-center justify-center p-2.5 xl:p-5">
                <button
                    onClick={() => setModalIsOpen(true)}
                    className="mr-2 sm:ml-8"
                >
                    <img
                        src={modifyIcon}
                        alt="Modify"
                        className="w-3.5 h-3.5 sm:w-auto sm:h-auto"
                    />
                </button>
                <button onClick={handleDelete}>
                    <img
                        src={deleteIcon}
                        alt="delete"
                        className="w-3.5 h-3.5 sm:w-auto sm:h-auto"
                    />
                </button>
                {modalIsOpen && (
                    <MaterialManagementModal
                        isOpen={modalIsOpen}
                        onClose={() => setModalIsOpen(false)}
                        initialValue={material}
                    />
                )}
            </div>
        </div>
    );
};

export default MaterialListInfo;

import modifyIcon from "../../../images/orderlist/modify.svg";
import deleteIcon from "../../../images/orderlist/delete.svg";
import { useState } from "react";
import { CompanyDeleteApi } from "../../../apis/server/CompanyApi";
import CompanyManagementModal from "./CompanyManagementModal";
import Swal from "sweetalert2";

const CompanyListInfo = ({ company, isLastItem }) => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const deleteCompany = CompanyDeleteApi(company.venderId);

    const handleDelete = () => {
        Swal.fire({
            title: `"${company.venderName}"를 삭제하시겠습니까?`,
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
                deleteCompany.mutate(null, {
                    onSuccess: () => {
                        Swal.fire({
                            title: "삭제 완료!",
                            text: "업체가 성공적으로 삭제되었습니다.",
                            icon: "success",
                            confirmButtonColor: "#3C50E0", // 버튼 색상 설정
                            iconColor: "#3C50E0", // 아이콘 색상 설정
                        });
                    },
                    onError: () => {
                        Swal.fire(
                            "오류 발생!",
                            "업체를 삭제하는 도중 오류가 발생했습니다.",
                            "error",
                        );
                    },
                });
            }
        });
    };

    return (
        <div
            className={`grid grid-cols-4 sm:grid-cols-5 ${
                isLastItem
                    ? ""
                    : "border-b border-stroke dark:border-strokedark"
            }`}
            key={company.venderId} // 이제 key는 여기서 사용되지 않습니다. key는 최상위 반복되는 요소에만 필요합니다.
        >
            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {company.venderName}
                </p>
            </div>

            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {company.venderManager}
                </p>
            </div>

            <div className="items-center justify-center pt-2.5 pb-2.5 text-xs sm:flex sm:text-base xl:p-5">
                <p className="text-center text-black dark:text-white">
                    {company.venderManagerContact}
                </p>
            </div>
            <div className="items-center justify-center pt-2.5 pb-2.5 text-xs sm:flex sm:text-base xl:p-5">
                <p className="text-center text-black dark:text-white overflow-hidden text-overflow-ellipsis white-space-nowrap">
                    {company.venderDescription}
                </p>
            </div>

            <div className="items-center justify-center text-center p-2.5 sm:flex xl:p-5">
                <button
                    onClick={() => setModalIsOpen(true)}
                    className="mr-2 sm:ml-8"
                >
                    <img
                        className="w-5 h-5 sm:w-auto sm:h-auto"
                        src={modifyIcon}
                        alt="Modify"
                    />
                </button>
                <button onClick={handleDelete}>
                    <img
                        className="w-5 h-5 sm:w-auto sm:h-auto"
                        src={deleteIcon}
                        alt="Delete"
                    />
                </button>
                {modalIsOpen && (
                    <CompanyManagementModal
                        isOpen={modalIsOpen}
                        onClose={() => setModalIsOpen(false)}
                        initialValue={company}
                    />
                )}
            </div>
        </div>
    );
};

export default CompanyListInfo;

import modifyIcon from "../../../images/orderlist/modify.svg";
import deleteIcon from "../../../images/orderlist/delete.svg";
import { useState } from "react";
import { CompanyDeleteApi } from "../../../apis/server/CompanyApi";
import CompanyManagementModal from "./CompanyManagementModal";

const CompanyListInfo = ({ company, isLastItem }) => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const deleteCompany = CompanyDeleteApi(company.venderId);

    const handleDelete = () => {
        deleteCompany.mutate();
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

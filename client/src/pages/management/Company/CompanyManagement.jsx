import { useState } from "react";
import CompanyManagementModal from "./CompanyManagementModal";
import CompanyTable from "./CompanyTable";

const CompanyManagement = () => {
    const [modalIsOpen, setModalIsOpen] = useState(false);

    return (
        <>
            <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                    업체 관리
                </h2>
                <ol className="flex items-center gap-2">
                    <li>가게 관리 /</li>
                    <li className="font-bold text-primary">업체 관리</li>
                </ol>
            </div>
            <CompanyTable />
            <button
                onClick={() => setModalIsOpen(true)}
                className="bg-transparent hover:bg-primary text-primary font-semibold hover:text-white py-2 px-4 border border-primary hover:border-transparent rounded"
            >
                업체추가
            </button>

            {modalIsOpen && (
                <CompanyManagementModal
                    isOpen={modalIsOpen}
                    onClose={() => setModalIsOpen(false)}
                />
            )}
        </>
    );
};

export default CompanyManagement;

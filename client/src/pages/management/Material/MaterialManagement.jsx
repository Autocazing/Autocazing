import MaterialTable from "./MaterialTable";
import { useState } from "react";
import MaterialManagementModal from "./MaterialManagementModal";

import { MaterialGetApi } from "../../../apis/server/MaterialApi";
const MaterialManagement = () => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const { data: materialInfo, isLoading, isError, error } = MaterialGetApi();

    return (
        <>
            <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                    재료 관리
                </h2>
                <ol className="flex items-center gap-2">
                    <li>재료 관리 /</li>
                    <li className="font-bold text-primary">재료 관리</li>
                </ol>
            </div>
            <MaterialTable materialInfo={materialInfo} />
            <button
                onClick={() => setModalIsOpen(true)}
                className="bg-transparent hover:bg-primary text-primary font-semibold hover:text-white py-2 px-4 border border-primary hover:border-transparent rounded"
            >
                재료추가
            </button>

            {modalIsOpen && (
                <MaterialManagementModal
                    isOpen={modalIsOpen}
                    onClose={() => setModalIsOpen(false)}
                    initialValue={[]}
                />
            )}
        </>
    );
};

export default MaterialManagement;

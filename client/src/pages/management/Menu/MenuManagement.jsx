import MenuTable from "./MenuTable";
import MenuManagementModal from "./MenuManagementModal";
import { useState } from "react";
import { MenuGetApi } from "../../../apis/server/MenuApi";

const MenuManagement = () => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const { data: menuInfo, isLoading, isError, error } = MenuGetApi();
    return (
        <>
            <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                    메뉴 관리
                </h2>
                <ol className="flex items-center gap-2">
                    <li>가게 관리 /</li>
                    <li className="font-bold text-primary">메뉴 관리</li>
                </ol>
            </div>
            <MenuTable menuInfo={menuInfo} />
            <button
                onClick={() => setModalIsOpen(true)}
                className="bg-transparent hover:bg-primary text-primary font-semibold hover:text-white py-2 px-4 border border-primary hover:border-transparent rounded"
            >
                메뉴추가
            </button>

            {modalIsOpen && (
                <MenuManagementModal
                    isOpen={modalIsOpen}
                    onClose={() => setModalIsOpen(false)}
                    initialValue={[]}
                />
            )}
        </>
    );
};

export default MenuManagement;

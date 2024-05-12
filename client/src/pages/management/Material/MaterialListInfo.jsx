import modifyIcon from "../../../images/orderlist/modify.svg";
import deleteIcon from "../../../images/orderlist/delete.svg";
import { MaterialDeleteApi } from "../../../apis/server/MaterialApi";
import { useState } from "react";
import MaterialManagementModal from "./MaterialManagementModal";

const MaterialListInfo = ({ material, isLastItem }) => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const deleteMaterial = MaterialDeleteApi(material.ingredientId);

    const handleDelete = () => {
        deleteMaterial.mutate();
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
                    {material.ingredientPrice}
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

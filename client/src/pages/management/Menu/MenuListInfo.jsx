import modifyIcon from "../../../images/orderlist/modify.svg";
import deleteIcon from "../../../images/orderlist/delete.svg";
import checkbox from "../../../images/orderlist/checkbox.svg";
import nocheck from "../../../images/orderlist/nocheck.svg";

import { useState } from "react";
import { MenuDeleteApi } from "../../../apis/server/MenuApi";
import MenuManagementModal from "./MenuManagementModal";

const MenuListInfo = ({ menu, isLastItem }) => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const deleteMenu = MenuDeleteApi(menu.menuId);

    const handleDelete = () => {
        deleteMenu.mutate();
    };
    //원화 포맷 함수 추가
    const formatPrice = (price) => {
        return new Intl.NumberFormat("ko-KR").format(price);
    };
    // 재료 이름을 쉼표로 연결하는 함수
    const ingredientNames =
        menu.ingredientoDtoList?.length > 0
            ? menu.ingredientoDtoList
                  .map((ingredient) => ingredient.ingredientName)
                  .join(", ")
            : "재료 정보 없음";

    return (
        <div
            className={`grid grid-cols-6 sm:grid-cols-6 ${
                isLastItem
                    ? ""
                    : "border-b border-stroke dark:border-strokedark"
            }`}
            key={menu.menuId}
        >
            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">{menu.menuName}</p>
            </div>

            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {formatPrice(menu.menuPrice)}
                </p>
            </div>

            <div className="items-center justify-center p-2.5 sm:flex xl:p-5">
                <p className=" text-center text-black dark:text-white">
                    {ingredientNames}
                </p>
            </div>

            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <img
                    src={menu.onEvent ? checkbox : nocheck}
                    alt={menu.onEvent ? "Event Active" : "No Event"}
                />
            </div>
            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {menu.discountRate}
                </p>
            </div>

            <div className="items-center justify-center p-2.5 sm:flex xl:p-5">
                <button
                    onClick={() => setModalIsOpen(true)}
                    className="mr-2 sm:ml-8"
                >
                    <img src={modifyIcon} alt="Modify" />
                </button>
                <button onClick={handleDelete}>
                    <img src={deleteIcon} alt="delete" />
                </button>
                {modalIsOpen && (
                    <MenuManagementModal
                        isOpen={modalIsOpen}
                        onClose={() => setModalIsOpen(false)}
                        initialValue={menu}
                    />
                )}
            </div>
        </div>
    );
};

export default MenuListInfo;

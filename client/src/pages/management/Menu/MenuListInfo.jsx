import modifyIcon from "../../../images/orderlist/modify.svg";
import deleteIcon from "../../../images/orderlist/delete.svg";

import { useState } from "react";

const MenuListInfo = ({ menu, isLastItem }) => {
    return (
        <div
            className={`grid grid-cols-4 sm:grid-cols-4 ${
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
                <p className="text-black dark:text-white">{menu.menuPrice}</p>
            </div>

            <div className="items-center justify-center p-2.5 sm:flex xl:p-5">
                <p className=" text-center text-black dark:text-white">
                    {`Ingredient ID: ${menu.ingredients.ingredientId}, Capacity: ${menu.ingredients.capacity}`}
                </p>
            </div>

            <div className="items-center justify-center p-2.5 sm:flex xl:p-5">
                <button className="mr-2">
                    <img src={modifyIcon} alt="Modify" />
                </button>
                <button>
                    <img src={deleteIcon} alt="delete" />
                </button>
            </div>
        </div>
    );
};

export default MenuListInfo;

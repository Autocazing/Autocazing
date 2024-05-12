import modifyIcon from "../../../images/orderlist/modify.svg";
import deleteIcon from "../../../images/orderlist/delete.svg";

const MaterialListInfo = ({ material, isLastItem }) => {
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
                </p>
            </div>

            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {material.minimumCount}
                </p>
            </div>

            <div className="flex items-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {material.deliveryTime}
                </p>
            </div>

            <div className="flex items-center text-center justify-center p-2.5 xl:p-5">
                <p className="text-black dark:text-white">
                    {material.venderName}
                </p>
            </div>

            <div className="flex items-center text-center justify-center p-2.5 xl:p-5">
                <button className="mr-2">
                    <img
                        src={modifyIcon}
                        alt="Modify"
                        className="w-3.5 h-3.5 sm:w-auto sm:h-auto"
                    />
                </button>
                <button>
                    <img
                        src={deleteIcon}
                        alt="delete"
                        className="w-3.5 h-3.5 sm:w-auto sm:h-auto"
                    />
                </button>
            </div>
        </div>
    );
};

export default MaterialListInfo;

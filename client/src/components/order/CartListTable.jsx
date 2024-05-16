import MaterialManagementModal from "../../pages/order/MaterialManagementModal";
import modifyIcon from "../../images/orderlist/modify.svg";
import deleteIcon from "../../images/orderlist/delete.svg";
import { useEffect, useState } from "react";

const CartListTable = ({ Basket }) => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [selectedItem, setSelectedItem] = useState(null);
    const [cartList, setCartList] = useState([]);
    useEffect(() => {
        if (Basket !== undefined) {
            setCartList(Basket[0].specifics);
            // console.log(Basket[0].specifics);
        }
    });

    const handleItemClick = (item) => {
        // console.log(item);
        setSelectedItem(item);
        setModalIsOpen(true);
    };

    const deleteItem = (item) => {
        console.log(item);
    };

    return (
        <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
            <div className="flex-col gap-3 flex sm:flex-row sm:items-center sm:justify-between">
                <h4 className="mb-6 text-xl font-semibold text-black dark:text-white flex-row">
                    Cart List
                </h4>
                <button className="bg-transparent hover:bg-blue-500 text-blue-700 font-semibold hover:text-white py-2 px-4 border border-blue-500 hover:border-transparent rounded">
                    발주하기
                </button>
            </div>

            <div className="flex flex-col">
                <div className="grid grid-cols-3 rounded-sm bg-gray-2 dark:bg-meta-4 sm:grid-cols-6">
                    <div className="p-2.5 xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            재료명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            총량
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            가격(원)
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            업체명
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            배송소요기간
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            수정/삭제
                        </h5>
                    </div>
                </div>

                {cartList.map((order, key) => (
                    <div
                        className={`grid grid-cols-3 sm:grid-cols-6 ${
                            key === cartList.length - 1
                                ? ""
                                : "border-b border-stroke dark:border-strokedark"
                        }`}
                        key={key}
                    >
                        <div className="flex items-center gap-3 p-2.5 xl:p-5">
                            <p className="hidden text-black dark:text-white sm:block">
                                {order.ingredientName}
                            </p>
                        </div>

                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {order.ingredientQuanrtity}
                            </p>
                        </div>

                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {order.ingredientPrice}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {order.venderName}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {order.ordertime}
                            </p>
                        </div>
                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <button
                                className="mr-2"
                                onClick={() => handleItemClick(order)}
                            >
                                <img
                                    src={modifyIcon}
                                    alt="Modify"
                                    onClick={() => setModalIsOpen(true)}
                                />
                            </button>
                            <button>
                                <img
                                    src={deleteIcon}
                                    alt="delete"
                                    onClick={() => deleteItem(order)}
                                />
                            </button>
                            {modalIsOpen && selectedItem && (
                                <MaterialManagementModal
                                    isOpen={modalIsOpen}
                                    onClose={() => setModalIsOpen(false)}
                                    initialValue={{
                                        ingredientName:
                                            selectedItem.ingredientName,
                                        ingredientQuantity:
                                            selectedItem.ingredientQuanrtity,
                                        restockOrderSpecificId:
                                            selectedItem.restockOrderSpecificId,
                                    }}
                                />
                            )}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default CartListTable;

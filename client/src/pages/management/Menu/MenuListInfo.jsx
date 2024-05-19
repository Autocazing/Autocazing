import modifyIcon from "../../../images/orderlist/modify.svg";
import deleteIcon from "../../../images/orderlist/delete.svg";
import checkbox from "../../../images/orderlist/checkbox.svg";
import nocheck from "../../../images/orderlist/nocheck.svg";
import Swal from "sweetalert2";
import { useState } from "react";
import { MenuDeleteApi } from "../../../apis/server/MenuApi";
import MenuManagementModal from "./MenuManagementModal";

const MenuListInfo = ({ menu, isLastItem }) => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const deleteMenu = MenuDeleteApi(menu.menuId);

    const handleDelete = () => {
        Swal.fire({
            title: `"${menu.menuName}"를 삭제하시겠습니까?`,
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
                deleteMenu.mutate(null, {
                    onSuccess: () => {
                        Swal.fire({
                            title: "삭제 완료!",
                            text: "메뉴가 성공적으로 삭제되었습니다.",
                            icon: "success",
                            confirmButtonColor: "#3C50E0", // 버튼 색상 설정
                            iconColor: "#3C50E0", // 아이콘 색상 설정
                        });
                    },
                    onError: () => {
                        Swal.fire(
                            "오류 발생!",
                            "메뉴를 삭제하는 도중 오류가 발생했습니다.",
                            "error",
                        );
                    },
                });
            }
        });
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

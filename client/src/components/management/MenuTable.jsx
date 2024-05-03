import modifyIcon from "../../images/orderlist/modify.svg";
import deleteIcon from "../../images/orderlist/delete.svg";

const MenuList = [
    // 테스트용
    {
        menuname: "아메리카노",
        price: "3000",
        material: "원두",
    },
    {
        menuname: "카페라뗴",
        price: "4000",
        material: "원두,우유",
    },
    {
        menuname: "돌체라뗴",
        price: "5000",
        material: "원두,우유,연유",
    },
];

const MenuTable = () => {
    return (
        <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
            <div className="flex-col gap-3 flex sm:flex-row sm:items-center sm:justify-between">
                <h4 className="mb-6 text-xl font-semibold text-black dark:text-white flex-row">
                    Menu List
                </h4>
                <button className="bg-transparent hover:bg-blue-500 text-blue-700 font-semibold hover:text-white py-2 px-4 border border-blue-500 hover:border-transparent rounded">
                    메뉴추가
                </button>
            </div>

            <div
                className="flex flex-col overflow-auto"
                style={{ height: "35rem" }}
            >
                <div className="grid grid-cols-3 rounded-sm bg-gray-2 dark:bg-meta-4 sm:grid-cols-4">
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            업체명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            담당자 이름
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            담당자 연락처
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            수정/삭제
                        </h5>
                    </div>
                </div>

                {MenuList.map((menu, key) => (
                    <div
                        className={`grid grid-cols-3 sm:grid-cols-4 ${
                            key === MenuList.length - 1
                                ? ""
                                : "border-b border-stroke dark:border-strokedark"
                        }`}
                        key={key}
                    >
                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {menu.menuname}
                            </p>
                        </div>

                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {menu.price}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {menu.material}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <button className="mr-2">
                                <img src={modifyIcon} alt="Modify" />
                            </button>
                            <button>
                                <img src={deleteIcon} alt="delete" />
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MenuTable;

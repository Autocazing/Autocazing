import MenuListInfo from "./MenuListInfo";

const MenuTable = ({ menuInfo }) => {
    if (!menuInfo || menuInfo.length === 0) {
        return <div>No company data available.</div>;
    }
    return (
        <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
            <div className="flex-col gap-3 flex sm:flex-row sm:items-center sm:justify-between">
                <h4 className="mb-6 text-xl font-semibold text-black dark:text-white flex-row">
                    Menu List
                </h4>
            </div>

            <div
                className="flex flex-col overflow-auto"
                style={{ height: "35rem" }}
            >
                <div className="grid grid-cols-6 rounded-sm bg-gray-2 dark:bg-meta-4 sm:grid-cols-6">
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            메뉴명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            메뉴가격
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            메뉴재료
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            할인적용여부
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            할인율(%)
                        </h5>
                    </div>
                    <div className="p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            수정/삭제
                        </h5>
                    </div>
                </div>
                {menuInfo.map((menu, index) => (
                    <MenuListInfo
                        key={menu.menuId}
                        menu={menu}
                        isLastItem={index === menuInfo.length - 1}
                    />
                ))}
            </div>
        </div>
    );
};

export default MenuTable;

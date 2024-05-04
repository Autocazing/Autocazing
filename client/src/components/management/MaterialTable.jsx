import modifyIcon from "../../images/orderlist/modify.svg";
import deleteIcon from "../../images/orderlist/delete.svg";

const MaterialList = [
    // 테스트용
    {
        materialname: "서울 우유",
        price: "2000",
        amount: "1",
        minnum: 2,
        delivertime: 2,
        companyname: "싸피상사",
    },
    {
        materialname: "연유",
        price: "4000",
        amount: "1",
        minnum: 1,
        delivertime: 3,
        companyname: "쿠팡",
    },
    {
        materialname: "원두",
        price: "10000",
        amount: "2",
        minnum: 3,
        delivertime: 1,
        companyname: "지호 원두",
    },
];

const MaterialTable = () => {
    return (
        <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
            <div className="flex-col gap-3 flex sm:flex-row sm:items-center sm:justify-between">
                <h4 className="mb-6 text-xl font-semibold text-black dark:text-white flex-row">
                    Material List
                </h4>
            </div>

            <div
                className="flex flex-col overflow-auto"
                style={{ height: "35rem" }}
            >
                <div className="grid grid-cols-3 rounded-sm bg-gray-2 dark:bg-meta-4 sm:grid-cols-7">
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            재료명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            재료 가격(원)
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            용량
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            최소 개수
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            배송소요기간
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            업체명
                        </h5>
                    </div>
                    <div className="hidden p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-sm font-medium uppercase xsm:text-base">
                            수정/삭제
                        </h5>
                    </div>
                </div>

                {MaterialList.map((material, key) => (
                    <div
                        className={`grid grid-cols-3 sm:grid-cols-7 ${
                            key === MaterialList.length - 1
                                ? ""
                                : "border-b border-stroke dark:border-strokedark"
                        }`}
                        key={key}
                    >
                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {material.materialname}
                            </p>
                        </div>

                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {material.price}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {material.amount}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {material.minnum}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {material.delivertime}
                            </p>
                        </div>

                        <div className="hidden items-center justify-center p-2.5 sm:flex xl:p-5">
                            <p className="text-black dark:text-white">
                                {material.companyname}
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

export default MaterialTable;

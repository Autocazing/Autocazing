import modifyIcon from "../../images/orderlist/modify.svg";
import deleteIcon from "../../images/orderlist/delete.svg";

const CompanyList = [
    // 테스트용
    {
        companyname: "지호상사",
        managername: "문지호",
        email: "moonwlfgh@naver.com",
    },
    {
        companyname: "동민상사",
        managername: "김영후",
        email: "0hoo@naver.com",
    },
    {
        companyname: "영후상사",
        managername: "위동민",
        email: "dsadadsad@naver.com",
    },
];

const CompanyTable = () => {
    return (
        <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
            <div className="flex-col gap-3 flex sm:flex-row sm:items-center sm:justify-between">
                <h4 className="mb-6 text-xl font-semibold text-black dark:text-white flex-row">
                    Company List
                </h4>
            </div>

            <div
                className="flex flex-col overflow-auto"
                style={{ height: "35rem" }}
            >
                <div className="grid grid-cols-4 rounded-sm bg-gray-2 dark:bg-meta-4 sm:grid-cols-4">
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-sm font-medium uppercase sm:text-base">
                            업체명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center text-sm xl:p-5">
                        <h5 className="text-sm font-medium uppercase sm:text-base">
                            담당자 이름
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-xs pt-0.5 font-medium uppercase sm:text-base sm:pt-0">
                            담당자 연락처
                        </h5>
                    </div>
                    <div className=" p-2.5 text-center sm:block xl:p-5 sm:ml-8">
                        <h5 className="text-sm font-medium uppercase sm:text-base">
                            수정/삭제
                        </h5>
                    </div>
                </div>

                {CompanyList.map((company, key) => (
                    <div
                        className={`grid grid-cols-4 sm:grid-cols-4 ${
                            key === CompanyList.length - 1
                                ? ""
                                : "border-b border-stroke dark:border-strokedark"
                        }`}
                        key={key}
                    >
                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {company.companyname}
                            </p>
                        </div>

                        <div className="flex items-center justify-center p-2.5 xl:p-5">
                            <p className="text-black dark:text-white">
                                {company.managername}
                            </p>
                        </div>

                        <div className="items-center justify-center pt-2.5 pb-2.5 text-xs  sm:flex sm:text-base xl:p-5">
                            <p className="text-center text-black dark:text-white">
                                {company.email}
                            </p>
                        </div>

                        <div className="items-center justify-center text-center p-2.5 sm:flex xl:p-5">
                            <button className="mr-2 sm:ml-8">
                                <img
                                    className="w-5 h-5 sm:w-auto sm:h-auto"
                                    src={modifyIcon}
                                    alt="Modify"
                                />
                            </button>
                            <button>
                                <img
                                    className="w-5 h-5 sm:w-auto sm:h-auto"
                                    src={deleteIcon}
                                    alt="delete"
                                />
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default CompanyTable;

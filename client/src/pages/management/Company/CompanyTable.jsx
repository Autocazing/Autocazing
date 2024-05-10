import CompanyListInfo from "./CompanyListInfo";

const CompanyTable = ({ companyInfo }) => {
    if (!companyInfo || companyInfo.length === 0) {
        return <div>No company data available.</div>;
    }
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

                {companyInfo.map((company, index) => (
                    <CompanyListInfo
                        key={company.venderId}
                        company={company}
                        isLastItem={index === companyInfo.length - 1}
                    />
                ))}
            </div>
        </div>
    );
};

export default CompanyTable;

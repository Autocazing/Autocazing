import MaterialListInfo from "./MaterialListInfo";

const MaterialTable = ({ materialInfo }) => {
    if (!materialInfo || materialInfo.length === 0) {
        return <div>No company data available.</div>;
    }
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
                <div className="grid grid-cols-7 rounded-sm bg-gray-2 dark:bg-meta-4 sm:grid-cols-7">
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-xs font-medium uppercase sm:text-base">
                            재료명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-xs font-medium uppercase sm:text-base">
                            재료 가격(원)
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-xs font-medium uppercase sm:text-base">
                            용량
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-xs font-medium uppercase sm:text-base">
                            최소 개수
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-xs font-medium uppercase sm:text-base">
                            배송소요기간
                        </h5>
                    </div>
                    <div className="p-2.5 text-center xl:p-5">
                        <h5 className="text-xs font-medium uppercase sm:text-base">
                            업체명
                        </h5>
                    </div>
                    <div className="p-2.5 text-center sm:block xl:p-5">
                        <h5 className="text-xs font-medium uppercase sm:text-base">
                            수정/삭제
                        </h5>
                    </div>
                </div>

                {materialInfo.map((material, index) => (
                    <MaterialListInfo
                        key={index}
                        material={material}
                        isLastItem={index === materialInfo.length - 1}
                    />
                ))}
            </div>
        </div>
    );
};

export default MaterialTable;

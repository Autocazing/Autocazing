const MaterialManagement = () => {
    return (
        <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                업체 관리
            </h2>
            <ol className="flex items-center gap-2">
                <li>재료 관리 /</li>
                <li className="font-bold text-primary">재료 관리</li>
            </ol>
        </div>
    );
};

export default MaterialManagement;

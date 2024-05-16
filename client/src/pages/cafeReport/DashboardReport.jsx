const DashboardReport = () => {
    const today = new Date();
    const month = today.getMonth();

    return (
        <div className="col-span-12 rounded-sm border border-stroke bg-white p-7.5 shadow-default dark:border-strokedark dark:bg-boxdark xl:col-span-4">
            <div>
                <h4 className="text-xl font-semibold text-black dark:text-white">
                    Today report
                </h4>
            </div>
        </div>
    );
};

export default DashboardReport;

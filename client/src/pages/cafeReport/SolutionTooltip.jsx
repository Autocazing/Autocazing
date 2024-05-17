const SolutionTooltip = ({ item, SolutionData }) => {
    const salesData = SolutionData?.optimal_sales;

    return (
        <div>
            <h4 className="text-title-sm mb-4 text-black">
                {item.ingredient_name}에 대한 솔루션
            </h4>
            <div className="flex justify-center w-full">
                <table className="text-center w-auto">
                    <thead>
                        <tr>
                            <th className="px-2 py-1 text-primary font-semibold">
                                메뉴명
                            </th>
                            <th className="px-2 py-1 text-primary font-semibold">
                                잔 수
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        {salesData &&
                            Object.entries(salesData).map(([key, value]) => (
                                <tr key={key}>
                                    <td className="px-2 py-1 text-black">
                                        {key}
                                    </td>
                                    <td className="px-2 py-1 text-black">
                                        {value}
                                    </td>
                                </tr>
                            ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default SolutionTooltip;

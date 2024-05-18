const SolutionTooltip = ({ item, solution }) => {
    const filteredSolutions = solution.filter(
        (sol) => sol.ingredient_id === item.ingredient_id,
    );
    console.log("Solution data: ", solution);
    console.log("Filtered Solutions: ", filteredSolutions);

    return (
        <div>
            <h4 className="text-title-sm mb-4 text-black">
                {item.ingredient_name}에 대한 솔루션
            </h4>
            <div className="flex justify-center w-full">
                {filteredSolutions.length > 0 ? (
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
                            {filteredSolutions.map(
                                ({ menu_name, sale_quantity }, index) => (
                                    <tr key={index}>
                                        <td className="px-2 py-1 text-black">
                                            {menu_name}
                                        </td>
                                        <td className="px-2 py-1 text-black">
                                            {sale_quantity}
                                        </td>
                                    </tr>
                                ),
                            )}
                        </tbody>
                    </table>
                ) : (
                    <div className="text-center text-black">
                        해당 재료에 대한 솔루션이 없습니다.
                    </div>
                )}
            </div>
        </div>
    );
};

export default SolutionTooltip;

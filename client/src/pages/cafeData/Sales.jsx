import { useState } from "react";
import SalesChart from "../../components/cafeData/SalesChart.jsx";

const Sales = () => {
    const [list, setList] = useState([1, 2, 3, 4, 5]);
    const [isOpen, setIsOpen] = useState(false);
    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };
    return (
        <div>
            <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                    Data
                </h2>
                <ol className="flex items-center gap-2">
                    <li>Data /</li>
                    <li className="font-bold text-primary">매출별</li>
                </ol>
            </div>
            {/* <button className="dropdown-btn" onClick={toggleDropdown}>
                Dropdown Button
            </button>
            {isOpen && (
                <div className="dropdown-content">
                    {list.map((item, index) => {
                        return <li key={index}>{list[index]}</li>;
                    })}
                </div>
            )} */}
            <div className="mt-4 grid grid-cols-12 gap-4 md:mt-6 md:gap-6 2xl:mt-7.5 2xl:gap-7.5">
                <SalesChart />
            </div>
        </div>
    );
};

export default Sales;

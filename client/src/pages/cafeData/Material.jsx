import { useState } from "react";
import ChartOne from "../../components/cafeData/ChartOne.jsx";

const Material = () => {
    const [list, setList] = useState([1, 2, 3, 4, 5]);
    const [isOpen, setIsOpen] = useState(false);
    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };
    return (
        <div>
            <button className="dropdown-btn" onClick={toggleDropdown}>
                Dropdown Button
            </button>
            {isOpen && (
                <div className="dropdown-content">
                    {list.map((item, index) => {
                        return <li key={index}>{list[index]}</li>;
                    })}
                </div>
            )}
            <div className="mt-4 grid grid-cols-12 gap-4 md:mt-6 md:gap-6 2xl:mt-7.5 2xl:gap-7.5">
                <ChartOne />
            </div>
        </div>
    );
};

export default Material;

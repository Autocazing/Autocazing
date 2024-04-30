import { Outlet } from "react-router-dom";

const CafeData = () => {
    return (
        <div>
            데이터
            <Outlet />
        </div>
    );
};

export default CafeData;

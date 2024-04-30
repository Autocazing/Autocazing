import { Route, Routes } from "react-router-dom";
import Dashboard from "./pages/dasgboard/Dashboard";

function App() {
    return (
        <div>
            <Routes>
                <Route path="/" element={<Dashboard />} />
                <Route path="/cafeData" element={<Dashboard />} />
                <Route path="/order" element={<Dashboard />} />
                <Route path="/cafeReport" element={<Dashboard />} />
                <Route path="/management" element={<Dashboard />} />
            </Routes>
            {/* <div className="text-3xl font-bold underline">hello</div> */}
        </div>
    );
}

export default App;

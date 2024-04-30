import { Route, Routes } from "react-router-dom";
import Dashboard from "./pages/dasgboard/Dashboard";
import Material from "./pages/cafeData/Material";
import Menu from "./pages/cafeData/Menu";
import Sales from "./pages/cafeData/Sales";
import CafeData from "./pages/cafeData/CafeData";
import OrderList from "./pages/order/OrderList";
import ProgressOrder from "./pages/order/ProgressOrder";
import Order from "./pages/order/Order";
import CafeReport from "./pages/cafeReport/CafeReport";
import Management from "./pages/management/Management";
import CompanyManagement from "./pages/management/CompanyManagement";
import MaterialManagement from "./pages/management/MaterialManagement";
import MenuManagement from "./pages/management/MenuManagement";
import StockManagement from "./pages/management/StockManagement";

function App() {
    return (
        <div>
            <Routes>
                <Route path="/" element={<Dashboard />} />
                <Route path="/cafeData" element={<CafeData />}>
                    <Route path="material" element={<Material />}></Route>
                    <Route path="menu" element={<Menu />}></Route>
                    <Route path="sales" element={<Sales />}></Route>
                </Route>
                <Route path="/order" element={<Order />}>
                    <Route path="orderList" element={<OrderList />}></Route>
                    <Route
                        path="progressOrder"
                        element={<ProgressOrder />}
                    ></Route>
                </Route>
                <Route path="/cafeReport" element={<CafeReport />} />
                <Route path="/management" element={<Management />}>
                    <Route
                        path="companyManagement"
                        element={<CompanyManagement />}
                    ></Route>
                    <Route
                        path="materialManagement"
                        element={<MaterialManagement />}
                    ></Route>
                    <Route
                        path="menuManagement"
                        element={<MenuManagement />}
                    ></Route>
                    <Route
                        path="stockManagement"
                        element={<StockManagement />}
                    ></Route>
                </Route>
            </Routes>
            {/* <div className="text-3xl font-bold underline">hello</div> */}
        </div>
    );
}

export default App;

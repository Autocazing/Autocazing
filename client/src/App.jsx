import { Route, Routes } from "react-router-dom";
import Dashboard from "./pages/dasgboard/Dashboard";
import Material from "./pages/cafeData/Material";
import Menu from "./pages/cafeData/Menu";
import Sales from "./pages/cafeData/Sales";
import CafeData from "./pages/cafeData/CafeData";
import CartList from "./pages/order/CartList";
import ProgressOrder from "./pages/order/ProgressOrder";
import Order from "./pages/order/Order";
import CafeReport from "./pages/cafeReport/CafeReport";
import Management from "./pages/management/Management";
import CompanyManagement from "./pages/management/CompanyManagement";
import MaterialManagement from "./pages/management/MaterialManagement";
import MenuManagement from "./pages/management/MenuManagement";
import StockManagement from "./pages/management/StockManagement";
import LoginPage from "./pages/login/LoginPage";

import DefaultLayout from "./layout/DefaultLayout";

function App() {
    return (
        <div>
            <DefaultLayout>
                <Routes>
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/cafeData" element={<CafeData />}>
                        <Route path="material" element={<Material />}></Route>
                        <Route path="menu" element={<Menu />}></Route>
                        <Route path="sales" element={<Sales />}></Route>
                    </Route>
                    <Route path="/order" element={<Order />}>
                        <Route path="orderList" element={<CartList />}></Route>
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

                    <Route path="/" element={<LoginPage />}></Route>
                </Routes>
                {/* <div className="text-3xl font-bold underline">hello</div> */}
            </DefaultLayout>
        </div>
    );
}

export default App;

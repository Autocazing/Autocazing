import { Route, Routes, useLocation } from "react-router-dom";

import Test from "./pages/Test";

function App() {
    return (
        <Routes>
            <Route
                path="/"
                element={
                    <>
                        {/* <PageTitle title="eCommerce Dashboard | TailAdmin - Tailwind CSS Admin Dashboard Template" /> */}
                        <Test />
                    </>
                }
            />
        </Routes>
    );
}

export default App;

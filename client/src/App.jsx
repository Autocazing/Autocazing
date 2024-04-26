import { Route, Routes } from "react-router-dom";

import Test from "./pages/Test";
function App() {
    return (
        <div>
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
            {/* <div className="text-3xl font-bold underline">hello</div> */}
        </div>
    );
}

export default App;

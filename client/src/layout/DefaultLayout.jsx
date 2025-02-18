import React, { useState } from "react";
import Header from "../components/header/Header";
import Sidebar from "../components/Sidebar/Sidebar";

const DefaultLayout = ({ children }) => {
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const isLoginPage = window.location.pathname === "/";
    const posPage = window.location.pathname === "/pos";
    if (isLoginPage) {
        return (
            <div className="dark:bg-boxdark-2 dark:text-bodydark">
                {children}
            </div>
        );
    } else if (posPage) {
        return (
            <>
                <div>{children}</div>
            </>
        );
    } else {
        return (
            <div className="dark:bg-boxdark-2 dark:text-bodydark">
                {/* <!-- ===== Page Wrapper Start ===== --> */}
                <div className="flex h-screen overflow-hidden">
                    {/* <!-- ===== Sidebar Start ===== --> */}
                    <Sidebar
                        sidebarOpen={sidebarOpen}
                        setSidebarOpen={setSidebarOpen}
                    />
                    {/* <!-- ===== Sidebar End ===== --> */}

                    {/* <!-- ===== Content Area Start ===== --> */}
                    <div className="relative flex flex-1 flex-col overflow-y-auto overflow-x-hidden">
                        {/* <!-- ===== Header Start ===== --> */}
                        <Header
                            sidebarOpen={sidebarOpen}
                            setSidebarOpen={setSidebarOpen}
                        />
                        {/* <!-- ===== Header End ===== --> */}

                        {/* <!-- ===== Main Content Start ===== --> */}
                        <main>
                            <div className="mx-auto max-w-screen-2xl p-4 md:p-6 2xl:p-10">
                                {children}
                            </div>
                        </main>
                        {/* <!-- ===== Main Content End ===== --> */}
                    </div>
                    {/* <!-- ===== Content Area End ===== --> */}
                </div>
                {/* <!-- ===== Page Wrapper End ===== --> */}
            </div>
        );
    }
};

export default DefaultLayout;

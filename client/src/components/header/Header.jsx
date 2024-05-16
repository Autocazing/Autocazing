import DropdownNotification from "./DropdownNotification";
import DropdownUser from "./DropdownUser";
import { useEffect, useState } from "react";
import { EventSourcePolyfill } from "event-source-polyfill";
import { GetAlarmList } from "../../apis/server/Alarm";
import { QueryClient, useQueryClient } from "@tanstack/react-query";

const Header = (props) => {
    // 알림 SSE 구현
    const token = localStorage.getItem("accessToken");
    const [alarmlist, setAlarmlist] = useState([]);
    const { data: alarmInfo } = GetAlarmList();
    const posPage = window.location.pathname === "/pos";

    const queryClient = useQueryClient();

    useEffect(() => {
        if (token) {
            // login 되었을 때
            try {
                const EventSource = EventSourcePolyfill;
                const fetchSse = async () => {
                    const eventSource = new EventSource(
                        `https://k10e204.p.ssafy.io/api/alerts/connect`, // url 추가해야함
                        {
                            headers: {
                                Authorization: `Bearer ${token}`,
                            },
                            withCredentials: true,
                            heartbeatTimeout: 1500000,
                        },
                    );

                    eventSource.addEventListener("connect", (e) => {
                        if (alarmInfo !== undefined) {
                            setAlarmlist(alarmInfo);
                        }
                    });

                    eventSource.addEventListener("restock", (e) => {
                        queryClient.invalidateQueries("Alarm");
                        setAlarmlist(alarmInfo);
                    });

                    eventSource.addEventListener("sales", (e) => {
                        console.log("sales 갱신", e);
                    });
                };

                fetchSse();
            } catch (err) {
                console.log("실시간 알람 통신 에러", err);
            }
        }
    });

    if (posPage) {
        return <></>;
    }
    return (
        <header className="sticky top-0 z-999 flex w-full bg-white drop-shadow-1 dark:bg-boxdark dark:drop-shadow-none">
            <div className="flex flex-grow items-center justify-between px-4 py-4 shadow-2 md:px-6 2xl:px-11">
                <div className="flex items-center gap-2 sm:gap-4 lg:hidden">
                    {/* <!-- Hamburger Toggle BTN --> */}
                    <button
                        aria-controls="sidebar"
                        onClick={(e) => {
                            e.stopPropagation();
                            props.setSidebarOpen(!props.sidebarOpen);
                        }}
                        className="z-99999 block rounded-sm border border-stroke bg-white p-1.5 shadow-sm dark:border-strokedark dark:bg-boxdark lg:hidden"
                    >
                        <span className="relative block h-5.5 w-5.5 cursor-pointer">
                            <span className="du-block absolute right-0 h-full w-full">
                                <span
                                    className={`relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-[0] duration-200 ease-in-out dark:bg-white ${
                                        !props.sidebarOpen &&
                                        "!w-full delay-300"
                                    }`}
                                ></span>
                                <span
                                    className={`relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-150 duration-200 ease-in-out dark:bg-white ${
                                        !props.sidebarOpen &&
                                        "delay-400 !w-full"
                                    }`}
                                ></span>
                                <span
                                    className={`relative left-0 top-0 my-1 block h-0.5 w-0 rounded-sm bg-black delay-200 duration-200 ease-in-out dark:bg-white ${
                                        !props.sidebarOpen &&
                                        "!w-full delay-500"
                                    }`}
                                ></span>
                            </span>
                            <span className="absolute right-0 h-full w-full rotate-45">
                                <span
                                    className={`absolute left-2.5 top-0 block h-full w-0.5 rounded-sm bg-black delay-300 duration-200 ease-in-out dark:bg-white ${
                                        !props.sidebarOpen && "!h-0 !delay-[0]"
                                    }`}
                                ></span>
                                <span
                                    className={`delay-400 absolute left-0 top-2.5 block h-0.5 w-full rounded-sm bg-black duration-200 ease-in-out dark:bg-white ${
                                        !props.sidebarOpen && "!h-0 !delay-200"
                                    }`}
                                ></span>
                            </span>
                        </span>
                    </button>
                    {/* <!-- Hamburger Toggle BTN --> */}

                    {/* <Link className="block flex-shrink-0 lg:hidden" to="/">
                        <img src={LogoIcon} alt="Logo" />
                    </Link> */}
                </div>

                <div className="hidden sm:block">
                    <form
                        action="https://formbold.com/s/unique_form_id"
                        method="POST"
                    >
                        <div className="relative">오토카징</div>
                    </form>
                </div>

                <div className="flex items-center gap-3 2xsm:gap-7">
                    <ul className="flex items-center gap-2 2xsm:gap-4">
                        {/* <!-- Notification Menu Area --> */}
                        <DropdownNotification alarmlist={alarmlist} />
                        {/* <!-- Notification Menu Area --> */}
                    </ul>

                    {/* <!-- User Area --> */}
                    <DropdownUser />
                    {/* <!-- User Area --> */}
                </div>
            </div>
        </header>
    );
};

export default Header;

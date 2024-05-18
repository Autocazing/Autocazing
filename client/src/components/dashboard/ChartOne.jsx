import React, { useEffect, useState } from "react";
import ReactApexChart from "react-apexcharts";
import { PiMoonStarsDuotone } from "react-icons/pi";
// 테스트

const getDayOfWeek = (calday) => {
    const days = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
    const date = new Date(calday);
    const week = date.getDay();
    return days[week];
};

const getKoreanDateString = (date) => {
    return date
        .toLocaleDateString("ko-KR", {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
        })
        .replace(/. /g, "-")
        .replace(".", "");
};

const LastWeekDays = () => {
    const labels = [];
    const today = new Date();

    for (let i = 6; i >= 0; i--) {
        const pastDate = new Date(today);
        pastDate.setDate(today.getDate() - i);
        const dateString = getKoreanDateString(pastDate);
        const dayOfWeek = getDayOfWeek(dateString);
        labels.push(dayOfWeek);
    }

    return labels;
};

const options = {
    legend: {
        show: false,
        position: "top",
        horizontalAlign: "left",
    },
    colors: ["#3C50E0", "#80CAEE"],
    chart: {
        fontFamily: "Satoshi, sans-serif",
        height: 335,
        type: "area",
        dropShadow: {
            enabled: true,
            color: "#623CEA14",
            top: 10,
            blur: 4,
            left: 0,
            opacity: 0.1,
        },
        toolbar: {
            show: false,
        },
    },
    responsive: [
        {
            breakpoint: 1024,
            options: {
                chart: {
                    height: 300,
                },
            },
        },
        {
            breakpoint: 1366,
            options: {
                chart: {
                    height: 350,
                },
            },
        },
    ],
    stroke: {
        width: [2, 2],
        curve: "straight",
    },
    // labels: {
    //   show: false,
    //   position: "top",
    // },
    grid: {
        xaxis: {
            lines: {
                show: true,
            },
        },
        yaxis: {
            lines: {
                show: true,
            },
        },
    },
    dataLabels: {
        enabled: false,
    },
    markers: {
        size: 4,
        colors: "#fff",
        strokeColors: ["#3056D3", "#80CAEE"],
        strokeWidth: 3,
        strokeOpacity: 0.9,
        strokeDashArray: 0,
        fillOpacity: 1,
        discrete: [],
        hover: {
            size: undefined,
            sizeOffset: 5,
        },
    },
    xaxis: {
        type: "category",
        categories: LastWeekDays(),
        axisBorder: {
            show: false,
        },
        axisTicks: {
            show: false,
        },
    },
    yaxis: {
        title: {
            style: {
                fontSize: "0px",
            },
        },
        min: 0,
        max: 100,
    },
};

const getFormattedDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");

    return `${year}.${month}.${day}`;
};

const getLastWeekDate = () => {
    const today = new Date();
    const sixDaysAgo = new Date(today);
    sixDaysAgo.setDate(today.getDate() - 6); // 6일 전 날짜 설정
    return getFormattedDate(sixDaysAgo);
};

const ChartOne = ({ thisWeekSold, thisMonthAvgSold }) => {
    const [maxSize, setMaxSize] = useState(0);
    // const [minSize, setMinSize] = useState(0);

    const today = getFormattedDate(new Date());

    const lastweek = getLastWeekDate();

    const [state, setState] = useState({
        series: [
            {
                name: "Product One",
                data: [0, 0, 0, 0, 0, 0, 0],
            },

            {
                name: "Product Two",
                data: [0, 0, 0, 0, 0, 0, 0],
            },
        ],
    });

    useEffect(() => {
        // console.log(thisWeekSold);
        // thisWeekSold의 길이가 0보다 큰 경우에만 데이터를 업데이트합니다.

        if (thisWeekSold.length > 0) {
            console.log(thisWeekSold);
            // thisWeekSold의 값을 사용하여 "Product One"의 데이터를 업데이트합니다.
            const productOneData = thisWeekSold.map((item) => item.totalSales);

            // state를 업데이트합니다.
            setState((prevState) => ({
                ...prevState,
                series: [
                    {
                        ...prevState.series[0],
                        data: productOneData,
                    },
                    prevState.series[1], // Product Two는 변경하지 않습니다.
                ],
            }));

            const maxVal = Math.max(...productOneData);
            // const minVal = Math.min(...productOneData);
            setMaxSize(maxVal);
            // setMinSize(minVal);
        }
    }, [thisWeekSold]);

    useEffect(() => {
        // console.log(thisMonthAvgSold);
        const length = Object.keys(thisMonthAvgSold).length;
        if (length > 0) {
            // Update Product Two data with thisMonthAvgSold
            const productTwoData = Object.values(thisMonthAvgSold);
            setState((prevState) => ({
                ...prevState,
                series: [
                    prevState.series[0], // Product One remains unchanged
                    {
                        ...prevState.series[1],
                        data: productTwoData,
                    },
                ],
            }));
        }
    }, [thisMonthAvgSold]);

    const updatedOptions = {
        ...options, // 기존 옵션들 복사
        yaxis: {
            ...options.yaxis, // 기존 yaxis 옵션 복사
            min: 0, // 최솟값(min)을 minSize로 업데이트
            max: maxSize, // 최댓값(max)을 maxSize로 업데이트
        },
    };

    return (
        <div className="col-span-8 rounded-sm border border-stroke bg-white px-5 pt-7.5 pb-5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:col-span-8">
            <div className="mt-8 flex flex-nowrap items-start justify-between gap-3 sm:flex-nowrap">
                <div className="flex w-full flex-wrap gap-3 sm:gap-5">
                    <div className="flex min-w-47.5">
                        <span className="mt-1 mr-2 flex h-4 w-full max-w-4 items-center justify-center rounded-full border border-primary">
                            <span className="block h-2.5 w-full max-w-2.5 rounded-full bg-primary"></span>
                        </span>
                        <div className="w-full">
                            <p className="font-semibold text-primary">
                                최근 7일 매출 현황
                            </p>
                        </div>
                    </div>
                    <div className="flex min-w-47.5">
                        <span className="mt-1 mr-2 flex h-4 w-full max-w-4 items-center justify-center rounded-full border border-secondary">
                            <span className="block h-2.5 w-full max-w-2.5 rounded-full bg-secondary"></span>
                        </span>
                        <div className="w-full">
                            <p className="font-semibold text-secondary">
                                최근 1달 요일별 매출
                            </p>
                        </div>
                    </div>
                </div>
                {/* <div className="flex w-full max-w-45 justify-end">
                    <div className="inline-flex items-center rounded-md bg-whiter p-1.5 dark:bg-meta-4">
                        <button className="rounded bg-white py-1 px-3 text-xs font-medium text-black shadow-card hover:bg-white hover:shadow-card dark:bg-boxdark dark:text-white dark:hover:bg-boxdark">
                            Day
                        </button>
                        <button className="rounded py-1 px-3 text-xs font-medium text-black hover:bg-white hover:shadow-card dark:text-white dark:hover:bg-boxdark">
                            Week
                        </button>
                        <button className="rounded py-1 px-3 text-xs font-medium text-black hover:bg-white hover:shadow-card dark:text-white dark:hover:bg-boxdark">
                            Month
                        </button>
                    </div>
                </div> */}
            </div>
            <p className="font-bold text-center mt-2">
                {lastweek} ~ {today}
            </p>

            <div>
                <div id="chartOne" className="-ml-5">
                    <ReactApexChart
                        options={updatedOptions}
                        series={state.series}
                        type="area"
                        height={350}
                    />
                </div>
            </div>
        </div>
    );
};

export default ChartOne;

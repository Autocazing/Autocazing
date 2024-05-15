import React, { useEffect, useState } from "react";
import ReactApexChart from "react-apexcharts";
// 테스트
let options = {
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
        categories: [
            "Sep",
            "Oct",
            "Nov",
            "Dec",
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
        ],
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
        max: 10000000,
    },
};

const SalesChart = ({ dayData, weekData, monthData }) => {
    const [state, setState] = useState({
        series: [
            {
                name: "Product One",
                data: [23, 11, 22, 27, 13, 22, 37, 21, 44, 22, 30, 45],
            },
        ],
    });

    const [selectedButton, setSelectedButton] = useState("day");

    useEffect(() => {
        // 페이지가 처음 로드될 때 또는 dayData가 변경될 때 실행
        if (dayData && selectedButton === "day") {
            // dayData를 그대로 사용하거나 필요에 따라 가공
            const newData = dayData.map((item) => item);

            const newSeries = [
                {
                    name: "New Product",
                    data: newData,
                },
            ];

            setState({ series: newSeries });
        }
    }, [dayData, selectedButton]);

    useEffect(() => {
        // 페이지가 처음 로드될 때 또는 dayData가 변경될 때 실행
        if (dayData && selectedButton === "week") {
            // dayData를 그대로 사용하거나 필요에 따라 가공
            const newData = weekData.map((item) => item);

            const newSeries = [
                {
                    name: "New Product",
                    data: newData,
                },
            ];

            setState({ series: newSeries });
        }
    }, [weekData, selectedButton]);

    useEffect(() => {
        // 페이지가 처음 로드될 때 또는 dayData가 변경될 때 실행
        if (dayData && selectedButton === "month") {
            // dayData를 그대로 사용하거나 필요에 따라 가공
            const newData = monthData.map((item) => item);

            const newSeries = [
                {
                    name: "New Product",
                    data: newData,
                },
            ];

            setState({ series: newSeries });
        }
    }, [monthData, selectedButton]);

    useEffect(() => {
        let max = 0;
        if (selectedButton === "day" && dayData) {
            max = Math.max(...dayData);
        } else if (selectedButton === "week" && weekData) {
            max = Math.max(...weekData);
        } else if (selectedButton === "month" && monthData) {
            max = Math.max(...monthData);
        }

        if (max > 1) {
            setMaxSize(max);
        }
    }, [dayData, weekData, monthData, selectedButton]);

    const [maxSize, setMaxSize] = useState(0);

    const updatedOptions = {
        ...options, // 기존 옵션들 복사
        yaxis: {
            ...options.yaxis, // 기존 yaxis 옵션 복사
            min: 0, // 최솟값(min)을 minSize로 업데이트
            max: maxSize, // 최댓값(max)을 maxSize로 업데이트
        },
    };

    const handleButtonClick = (button) => {
        // if (button === "month") {
        //     const newSeries = [
        //         {
        //             name: "New Product",
        //             data: [10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120],
        //         },
        //     ];

        //     setState({ series: newSeries });
        // }

        // if (button === "week") {
        //     const newSeries = [
        //         {
        //             name: "New Product",
        //             data: [30, 41, 12, 27, 13, 62, 27, 11, 44, 22, 40, 45],
        //         },
        //     ];

        //     setState({ series: newSeries });
        // }

        // if (button === "day") {
        //     // dayData를 적절히 가공하여 그래프에 표시할 데이터로 변환
        //     const newData = dayData.map((item) => item); // 예시로, dayData를 그대로 사용하거나, 다른 방식으로 가공

        //     // 시리즈 데이터 업데이트
        //     const newSeries = [
        //         {
        //             name: "New Product",
        //             data: newData,
        //         },
        //     ];

        //     // 상태 업데이트
        //     setState({ series: newSeries });
        // }

        setSelectedButton(button);
    };

    return (
        <div
            className="col-span-12 rounded-sm border border-stroke bg-white px-5 pt-7.5 pb-5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:col-span-12"
            style={{ height: "40rem" }}
        >
            <div className="flex flex-wrap items-start justify-between gap-3 sm:flex-nowrap mt-16">
                <div className="flex w-full flex-wrap gap-3 sm:gap-5">
                    <div className="flex min-w-47.5">
                        {/* <span className="mt-1 mr-2 flex h-4 w-full max-w-4 items-center justify-center rounded-full border border-primary">
                            <span className="block h-2.5 w-full max-w-2.5 rounded-full bg-primary"></span>
                        </span>
                        <div className="w-full">
                            <p className="font-semibold text-primary">
                                Total Revenue
                            </p>
                            <p className="text-sm font-medium">
                                12.04.2022 - 12.05.2022
                            </p>
                        </div> */}
                    </div>
                    <div className="flex min-w-47.5">
                        {/* <span className="mt-1 mr-2 flex h-4 w-full max-w-4 items-center justify-center rounded-full border border-secondary">
                            <span className="block h-2.5 w-full max-w-2.5 rounded-full bg-secondary"></span>
                        </span>
                        <div className="w-full">
                            <p className="font-semibold text-secondary">
                                Total Sales
                            </p>
                            <p className="text-sm font-medium">
                                12.04.2022 - 12.05.2022
                            </p>
                        </div> */}
                    </div>
                </div>
                <div className="flex w-full max-w-45 justify-end">
                    <div className="inline-flex items-center rounded-md bg-whiter p-1.5 dark:bg-meta-4">
                        <button
                            className={
                                selectedButton === "day"
                                    ? "rounded bg-white py-1 px-3 text-xs font-medium text-black shadow-card hover:bg-white hover:shadow-card dark:bg-boxdark dark:text-white dark:hover:bg-boxdark"
                                    : "rounded py-1 px-3 text-xs font-medium text-black hover:bg-white hover:shadow-card dark:text-white dark:hover:bg-boxdark"
                            }
                            onClick={() => handleButtonClick("day")}
                        >
                            Day
                        </button>
                        <button
                            className={
                                selectedButton === "week"
                                    ? "rounded bg-white py-1 px-3 text-xs font-medium text-black shadow-card hover:bg-white hover:shadow-card dark:bg-boxdark dark:text-white dark:hover:bg-boxdark"
                                    : "rounded py-1 px-3 text-xs font-medium text-black hover:bg-white hover:shadow-card dark:text-white dark:hover:bg-boxdark"
                            }
                            onClick={() => handleButtonClick("week")}
                        >
                            Week
                        </button>
                        <button
                            className={
                                selectedButton === "month"
                                    ? "rounded bg-white py-1 px-3 text-xs font-medium text-black shadow-card hover:bg-white hover:shadow-card dark:bg-boxdark dark:text-white dark:hover:bg-boxdark"
                                    : "rounded py-1 px-3 text-xs font-medium text-black hover:bg-white hover:shadow-card dark:text-white dark:hover:bg-boxdark"
                            }
                            onClick={() => handleButtonClick("month")}
                        >
                            Month
                        </button>
                    </div>
                </div>
            </div>

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

export default SalesChart;

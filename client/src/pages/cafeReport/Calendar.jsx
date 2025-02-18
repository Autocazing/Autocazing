import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import "./Calendar.css";
import ReportModal from "./ReportModal";
import { useEffect, useState } from "react";

import { ReportMonthGetApi } from "../../apis/server/ReportApi";
const Calendar = () => {
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [selectedData, setSelectedData] = useState(null);
    const [year, setYear] = useState("");
    const [month, setMonth] = useState("");

    const {
        data: monthReportData,
        isLoading,
        isError,
        error,
    } = ReportMonthGetApi(year, month);

    const events =
        monthReportData?.map((data) => ({
            title: `${data.created_at.split("T")[0]} 리포트`, // YYYY-MM-DD 포맷
            start: data.created_at.split("T")[0], // ISO 문자열에서 날짜 부분만 사용
            allDay: true,
        })) || [];
    const handleDatesSet = (dateInfo) => {
        setMonth(dateInfo.startStr.slice(5, 7)); // 월 추출
        setYear(dateInfo.startStr.slice(0, 4)); // 연도 추출
    };

    // 클릭한 날짜와 데이터 안의 날짜가 일치하는지 찾고 모달 여는 함수
    const handleEventClick = (clickInfo) => {
        const clickedDate = clickInfo.event.startStr;
        const data = monthReportData.find(
            (d) => d.created_at.split("T")[0] === clickedDate,
        );
        setSelectedData(data);
        setModalIsOpen(true);
    };
    return (
        <div className="rounded-sm border border-stroke bg-white px-5 pt-6 pb-2.5 shadow-default dark:border-strokedark dark:bg-boxdark sm:px-7.5 xl:pb-1">
            <div>
                <FullCalendar
                    initialView="dayGridMonth"
                    plugins={[dayGridPlugin]}
                    showNonCurrentDates={false}
                    contentHeight={600}
                    events={events}
                    eventClick={handleEventClick}
                    datesSet={handleDatesSet}
                />
                {selectedData && (
                    <ReportModal
                        isOpen={modalIsOpen}
                        onClose={() => setModalIsOpen(false)}
                        data={selectedData}
                    />
                )}
            </div>
        </div>
    );
};

export default Calendar;

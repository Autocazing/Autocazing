import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import "./Calendar.css";
const Calendar = ({ reportData }) => {
    const events = reportData.map((data) => ({
        title: `${data.date} 리포트`,
        start: data.date,
        allDay: true,
    }));

    return (
        <div>
            <FullCalendar
                initialView="dayGridMonth"
                plugins={[dayGridPlugin]}
                showNonCurrentDates={false}
                contentHeight={600}
                events={events}
            />
        </div>
    );
};

export default Calendar;

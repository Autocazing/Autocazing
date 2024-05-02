import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import "./Calendar.css";
const Calendar = () => {
    return (
        <div>
            <FullCalendar
                initialView="dayGridMonth"
                plugins={[dayGridPlugin]}
                showNonCurrentDates={false}
                contentHeight={600}
            />
        </div>
    );
};

export default Calendar;

import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery } from "@tanstack/react-query";

const GetAlarmList = () => {
    const fetchGet = () => axiosInstance.get("/alerts");

    return useQuery({
        queryKey: ["Alarm"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

export { GetAlarmList };

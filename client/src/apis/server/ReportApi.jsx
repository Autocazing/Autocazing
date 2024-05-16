import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
const ReportMonthGetApi = (year, month, day) => {
    const fetchGet = () =>
        axiosInstance.get(
            `/solution/report?year=${year}&month=${month}&day=${day}`,
        );

    return useQuery({
        queryKey: ["GetMonthReport"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

export { ReportMonthGetApi };

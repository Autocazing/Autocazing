import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
const ReportdayGetApi = (year, month, day) => {
    const fetchGet = () =>
        axiosInstance.get(
            `/solution/report?year=${year}&month=${month}&day=${day}`,
        );

    return useQuery({
        queryKey: ["GetMonthReport", day],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

const ReportMonthGetApi = (year, month) => {
    const fetchGet = () =>
        axiosInstance.get(`/solution/report?year=${year}&month=${month}`);

    return useQuery({
        queryKey: ["GetMonthReport", year, month],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

const SolutionGetApi = (report_id, ingredient_id) => {
    const fetchGet = () =>
        axiosInstance.get(
            `/solution/${report_id}/ingredient-solution/${ingredient_id}`,
        );

    return useQuery({
        queryKey: ["GetSolution", report_id, ingredient_id],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

export { ReportMonthGetApi, SolutionGetApi, ReportdayGetApi };

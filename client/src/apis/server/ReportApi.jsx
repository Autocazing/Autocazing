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
        staleTime: 5 * 60 * 1000, // 5분 동안 새로고침 없음
        cacheTime: 15 * 60 * 1000, // 15분 동안 캐시 유지
        select: (data) => data.data,
        enabled: !!day,
    });
};

const ReportMonthGetApi = (year, month) => {
    const fetchGet = () =>
        axiosInstance.get(`/solution/report?year=${year}&month=${month}`);

    return useQuery({
        queryKey: ["GetMonthReport", year, month],
        queryFn: fetchGet,
        staleTime: 5 * 60 * 1000, // 5분 동안 새로고침 없음
        cacheTime: 15 * 60 * 1000, // 15분 동안 캐시 유지
        select: (data) => data.data,
        enabled: !!month,
    });
};

// const SolutionGetApi = (report_id, ingredient_id) => {
//     const fetchGet = () =>
//         axiosInstance.get(
//             `/solution/${report_id}/ingredient-solution/${ingredient_id}`,
//         );
//     console.log("솔루션 axios");
//     return useQuery({
//         queryKey: ["GetSolution", report_id, ingredient_id],
//         queryFn: fetchGet,
//         staleTime: 5 * 60 * 1000, // 5분 동안 새로고침 없음
//         cacheTime: 15 * 60 * 1000, // 15분 동안 캐시 유지
//         select: (data) => data.data,
//         enabled: !!report_id,
//     });
// };

export { ReportMonthGetApi, ReportdayGetApi };

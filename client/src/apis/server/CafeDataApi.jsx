import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery } from "@tanstack/react-query";

const GetSalesDay = () => {
    const fetchGet = () => axiosInstance.get("/sales?type=day");

    return useQuery({
        queryKey: ["GetSalesDay"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

const GetSalesWeek = () => {
    const fetchGet = () => axiosInstance.get("/sales?type=week");

    return useQuery({
        queryKey: ["GetSalesWeek"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

const GetSalesMonth = () => {
    const fetchGet = () => axiosInstance.get("/sales?type=month");

    return useQuery({
        queryKey: ["GetSalesMonth"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

export { GetSalesDay, GetSalesWeek, GetSalesMonth };

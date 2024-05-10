import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery } from "@tanstack/react-query";

const GetSalesSold = () => {
    const fetchGet = () => axiosInstance.get("/sales/sold");

    return useQuery({
        queryKey: ["GetSales"],
        queryFn: fetchGet,
        select: (data) => data,
    });
};

const GetSalesDay = () => {
    const fetchGet = () => axiosInstance.get("/sales?type=day");

    return useQuery({
        queryKey: ["GetSalesDay"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

export { GetSalesSold, GetSalesDay };

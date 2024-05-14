import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";

const StockGetApi = () => {
    const fetchGet = () => axiosInstance.get("/stocks");

    return useQuery({
        queryKey: ["GetStock"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

export { StockGetApi };

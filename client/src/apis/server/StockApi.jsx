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

const StockPostApi = () => {
    const queryClient = useQueryClient();

    const fetchPost = (postData) => {
        return axiosInstance.post("/stocks", postData);
    };

    const mutation = useMutation({
        mutationFn: fetchPost,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["GetStock"] });
            console.log("성공");
        },
        onError: (error) => {
            console.error("실패", error);
        },
    });

    return mutation;
};

export { StockGetApi, StockPostApi };

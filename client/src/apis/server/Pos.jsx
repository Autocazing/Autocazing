import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";

const GetMenu = () => {
    const fetchGet = () => axiosInstance.get("/menus");

    return useQuery({
        queryKey: ["getMenu"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

const PostOrders = () => {
    const queryClient = useQueryClient();

    const fetchPost = (postData) => {
        return axiosInstance.post("/orders", postData);
    };

    const mutation = useMutation({
        mutationFn: fetchPost,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["GetVenders"] });
            console.log("성공");
        },
        onError: (error) => {
            console.error("실패", error);
        },
    });

    return mutation;
};

export { GetMenu, PostOrders };

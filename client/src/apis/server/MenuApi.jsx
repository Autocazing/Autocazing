import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";

const MenuGetApi = () => {
    const fetchGet = () => axiosInstance.get("/menus");

    return useQuery({
        queryKey: ["GetMenu"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

const MenuDeleteApi = (menuId) => {
    const queryClient = useQueryClient();
    const fetchDelete = () => {
        return axiosInstance.delete(`/menus/${menuId}`);
    };
    const mutation = useMutation({
        mutationFn: fetchDelete,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["GetMenu"] });
            console.log("성공");
        },
        onError: (error) => {
            console.error("실패", error);
        },
    });
    return mutation;
};

const MenuPostApi = () => {
    const queryClient = useQueryClient();

    const fetchPost = (postData) => {
        return axiosInstance.post("/menus", postData);
    };

    const mutation = useMutation({
        mutationFn: fetchPost,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["GetMenu"] });
            console.log("성공");
        },
        onError: (error) => {
            console.error("실패", error);
        },
    });

    return mutation;
};

export { MenuGetApi, MenuDeleteApi, MenuPostApi };

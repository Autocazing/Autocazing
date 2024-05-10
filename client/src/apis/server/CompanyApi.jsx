import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";

const CompanyGetApi = () => {
    const fetchGet = () => axiosInstance.get("/venders");

    return useQuery({
        queryKey: ["GetVenders"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

const CompanyPostApi = () => {
    const queryClient = useQueryClient();

    const fetchPost = (postData) => {
        return axiosInstance.post("/venders", postData);
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

const CompanyDeleteApi = (venderId) => {
    const queryClient = useQueryClient();

    const fetchDelete = () => {
        return axiosInstance.delete(`/venders/${venderId}`);
    };

    const mutation = useMutation({
        mutationFn: fetchDelete,
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

export { CompanyGetApi, CompanyPostApi, CompanyDeleteApi };

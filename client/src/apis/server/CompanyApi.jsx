import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery, useMutation } from "@tanstack/react-query";

const CompanyGetApi = () => {
    const fetchGet = () => axiosInstance.get("/venders");

    return useQuery({
        queryKey: ["GetVenders"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

const CompanyPostApi = () => {
    const fetchPost = (postData) => {
        return axiosInstance.post("/venders", postData);
    };

    const mutation = useMutation(fetchPost, {
        onSuccess: () => {
            console.log("성공");
        },
        onError: (error) => {
            console.error("실패", error);
        },
    });

    return mutation;
};

export { CompanyGetApi, CompanyPostApi };

import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";

const MaterialGetApi = () => {
    const fetchGet = () => axiosInstance.get("/ingredients");

    return useQuery({
        queryKey: ["GetIngredient"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

// 단위

const MaterialScaleGetApi = () => {
    const fetchGet = () => axiosInstance.get("/ingredients-scales");

    return useQuery({
        queryKey: ["GetIngredientScale"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};
export { MaterialGetApi, MaterialScaleGetApi };

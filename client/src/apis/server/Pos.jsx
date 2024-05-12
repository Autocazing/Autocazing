import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery } from "@tanstack/react-query";

const GetMenu = () => {
    const fetchGet = () => axiosInstance.get("/menus");

    return useQuery({
        queryKey: ["getMenu"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

export { GetMenu };

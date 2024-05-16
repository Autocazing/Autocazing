import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery } from "@tanstack/react-query";

const GetOredered = () => {
    const fetchGet = () => axiosInstance.get("/restocks?status=ORDERED");

    return useQuery({
        queryKey: ["GetOredered"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

const GetBasket = () => {
    const fetchGet = () => axiosInstance.get("/restocks?status=WRITING");

    return useQuery({
        queryKey: ["GetBasket"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

export { GetOredered, GetBasket };

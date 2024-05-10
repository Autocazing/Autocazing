import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useQuery } from "@tanstack/react-query";

const CompanyGetApi = () => {
    const fetchGet = () => axiosInstance.get("/venders");

    return useQuery({
        queryKey: ["GetVenders"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

export { CompanyGetApi };

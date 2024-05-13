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

export { MenuGetApi };

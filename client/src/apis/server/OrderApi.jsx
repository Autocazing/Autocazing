import { axiosInstance } from "../../utils/axios/AxiosInstance";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

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

const PutRestock = (specificsId) => {
    const queryClient = useQueryClient();

    const fetchEdit = (editData) => {
        return axiosInstance.put(
            `/restocks/specifics/${specificsId}`,
            editData,
        );
    };

    const mutation = useMutation({
        mutationFn: fetchEdit,
        onSuccess: () => {
            console.log("성공");
        },
        onError: (error) => {
            console.error("실패", error);
        },
    });

    return mutation;
};

const DelRestock = (specificsId) => {
    const queryClient = useQueryClient();

    const fetchDelete = () => {
        return axiosInstance.delete(`/restocks/specifics/${specificsId}`);
    };

    const mutation = useMutation({
        mutationFn: fetchDelete,
        onSuccess: () => {
            console.log("성공");
        },
        onError: (error) => {
            console.error("실패", error);
        },
    });

    return mutation;
};

export { GetOredered, GetBasket, PutRestock, DelRestock };

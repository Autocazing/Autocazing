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

const MaterialPostApi = () => {
    const queryClient = useQueryClient();

    const fetchPost = (postData) => {
        return axiosInstance.post("/ingredients", postData);
    };

    const mutation = useMutation({
        mutationFn: fetchPost,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["GetIngredient"] });
            console.log("성공");
        },
        onError: (error) => {
            console.error("실패", error);
        },
    });

    return mutation;
};

const MaterialDeleteApi = (ingredientId) => {
    const queryClient = useQueryClient();
    const fetchDelete = () => {
        return axiosInstance.delete(`/ingredients/${ingredientId}`);
    };
    const mutation = useMutation({
        mutationFn: fetchDelete,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["GetIngredient"] });
            console.log("성공");
        },
        onError: (error) => {
            console.error("실패", error);
        },
    });
    return mutation;
};

const MaterialEditApi = (ingredientId) => {
    const queryClient = useQueryClient();

    const fetchEdit = (editData) => {
        return axiosInstance.put(`/ingredients/${ingredientId}`, editData);
    };

    const mutation = useMutation({
        mutationFn: fetchEdit,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["GetIngredient"] });
            console.log("성공");
        },
        onError: (error) => {
            console.error("실패", error);
        },
    });

    return mutation;
};

// 단위

const MaterialScaleGetApi = () => {
    const fetchGet = () => axiosInstance.get("/ingredient-scales");

    return useQuery({
        queryKey: ["GetIngredientScale"],
        queryFn: fetchGet,
        select: (data) => data.data,
    });
};

const MaterialScalePostApi = () => {
    const queryClient = useQueryClient();

    const fetchPost = (postData) => {
        return axiosInstance.post("/ingredient-scales", postData);
    };

    const mutation = useMutation({
        mutationFn: fetchPost,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["GetIngredientScale"] });
            console.log("성공");
        },
        onError: (error) => {
            console.error("실패", error);
        },
    });

    return mutation;
};

export {
    MaterialGetApi,
    MaterialScaleGetApi,
    MaterialScalePostApi,
    MaterialPostApi,
    MaterialDeleteApi,
    MaterialEditApi,
};

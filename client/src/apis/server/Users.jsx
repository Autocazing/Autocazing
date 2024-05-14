import { axiosInstance } from "../../utils/axios/AxiosInstance";

export const login = (data, success, fail) => {
    axiosInstance.post("users/login", data).then(success).catch(fail);
};

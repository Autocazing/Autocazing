import { axiosInstance } from "../../utils/axios/AxiosInstance";

export const login = (data, success, fail) => {
    axiosInstance.post("users/login", data).then(success).catch(fail);
};

export const alramConnect = (success, fail) => {
    axiosInstance.get("/alerts/connect").then(success).catch(fail);
};

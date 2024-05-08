import { axiosInstance } from "../../utils/axios/AxiosInstance";

export const getMenus = (data, success, fail) => {
    axiosInstance.get("menus").then(success).catch(fail);
};

import { axiosInstance } from "../../utils/axios/AxiosInstance";

export const getMenu = (success, fail) => {
    const token = localStorage.getItem("token"); // 로컬 스토리지에서 토큰을 가져옵니다.

    axiosInstance
        .get("menus", {
            // headers: {
            //     Authorization: `Bearer ${token}`, // Authorization 헤더에 토큰을 포함시킵니다.
            // },
        })
        .then(success)
        .catch(fail);
};

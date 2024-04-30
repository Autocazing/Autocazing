import axios from "axios";

const axiosInstance = axios.create({
    baseURL: "https://j10e202.p.ssafy.io/api",
    // timeout: 5000, // 일단 타임아웃 5초로
    withCredentials: true,
    // headers: {'our-server-token': 'not-yet'} // 우리 서버 아직 토큰 헤더 이름 안나옴...
});

// 이렇게 설정해두면 aixos 요청을 할때마다 header에 accesToken이 자동적으로 담겨서 요청이 됨
axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("accessToken");

        // 로컬에서 테스트 하고싶다면 토큰 이걸로
        // const token =
        //     "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5OHc4Z3hxVGtpV3BRXyIsImlhdCI6MTcxMTMzNzMwMSwiZXhwIjoxNzE0MDE1NzAxfQ.IMVHPA7WjwqlgOyMXf2HpW03DuHOC3FZD1F_EtmP2P8";

        if (token) {
            config.headers["access"] = `${token}`;
        }

        return config;
    },
    (error) => {
        // 요청 오류 처리
        return Promise.reject(error);
    },
);

axiosInstance.interceptors.response.use(
    (response) => {
        // 정상 응답 바로 반환
        return response;
    },
    async (error) => {
        const originalRequest = error.config;
        if (error.response.status === 403 && !originalRequest._retry) {
            originalRequest._retry = true; // 재시도 표시
            // 리프레시 토큰
            const refresh = localStorage.getItem("refresh");

            // const refresh =
            //     "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5OHc4Z3hxVGtpV3BRXyIsImlhdCI6MTcxMTMzNzMwMSwiZXhwIjoxNzE0MDE1NzAxfQ.IMVHPA7WjwqlgOyMXf2HpW03DuHOC3FZD1F_EtmP2P8";

            // refreshToken으로 새 accessToken 요청
            try {
                const response = await axios.post(
                    "https://j10e202.p.ssafy.io/api/auth/refresh",
                    {
                        refresh,
                    },
                );
                // 엑세스 토큰
                const { access } = response.data;
                localStorage.setItem("access", access);
                // 원래 요청에 새 토큰 설정하고 재시도
                axios.defaults.headers.common["access"] = `${access}`;
                return axiosInstance(originalRequest);
            } catch (refreshError) {
                console.error("Unable to refresh token:", refreshError);
                // 토큰 갱신 실패 시 로그인 페이지로 이동 등의 처리를 할 수 있습니다.
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    },
);

export { axiosInstance };

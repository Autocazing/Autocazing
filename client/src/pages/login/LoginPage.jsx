import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../../utils/axios/Users";
import cafeImage from "../../images/login/cafe-interior-design.jpg";

const LoginPage = () => {
    const [Id, setId] = useState(""); // Id
    const [Pw, setPw] = useState(""); // Pw

    const navigate = useNavigate();

    const loginHandler = (e) => {
        e.preventDefault();

        login(
            { loginId: Id, password: Pw },
            (res) => {
                if (res) {
                    // console.log(res.headers.token);
                    localStorage.setItem("accessToken", res.headers.token);
                    navigate("/dashboard"); // 메인페이지 이동
                }
            },
            (err) => {
                console.log("로그인 오류");
                console.log(err);
            },
        );
    };

    return (
        <>
            <div className="flex items-center justify-center h-screen w-full px-5 sm:px-0">
                <div className="flex bg-slate-200-lg  overflow-hidden lg:max-w-full w-full h-full">
                    <div className="w-full p-8 lg:w-1/2 flex flex-col justify-center">
                        <p className="font-bold text-center text-7xl">
                            AutoCazing
                        </p>
                        <div className="mt-20">
                            <input
                                className="mx-auto h-13 text-gray-700 border border-gray-300 rounded py-2 px-4 block w-full  focus:outline-2  max-w-150 focus:outline-blue-700"
                                type="email"
                                placeholder="아이디"
                                onChange={(e) => {
                                    setId(e.target.value);
                                }}
                            />
                        </div>
                        <div className="mt-8 flex flex-col justify-between">
                            <input
                                className="mx-auto h-13 text-gray-700 border border-gray-300 rounded py-2 px-4 block w-full focus:outline-2 max-w-150 focus:outline-blue-700"
                                type="password"
                                placeholder="비밀번호"
                                onChange={(e) => {
                                    setPw(e.target.value);
                                }}
                            />
                        </div>
                        <div className="mt-15 text-center">
                            <button
                                onClick={loginHandler}
                                className="bg-blue-700 text-white font-bold py-2 px-4 mx-auto w-full rounded max-w-100  hover:bg-blue-600"
                            >
                                Login
                            </button>
                        </div>
                        <div>
                            <hr className="mt-12 border-t-2 border-gray-300 mx-auto w-full max-w-150" />
                            <p className="text-center mt-8 text-black font-bold text-xs sm:text-base">
                                서비스 이용을 위한 회원가입은 관리자에게 문의
                                부탁드립니다.
                            </p>
                        </div>
                        <div className="mt-8 text-center">
                            <button className="bg-blue-700 text-white font-bold py-2 px-4 mx-auto w-full rounded max-w-100  hover:bg-blue-600">
                                문의하기
                            </button>
                        </div>
                    </div>
                    <div
                        className="hidden md:block lg:w-1/2 bg-cover bg-blue-700"
                        style={{
                            backgroundImage: `url(${cafeImage})`,
                        }}
                    ></div>
                </div>
            </div>
        </>
    );
};

export default LoginPage;

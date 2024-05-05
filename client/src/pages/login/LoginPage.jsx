const LoginPage = () => {
    return (
        <>
            <div className="flex items-center justify-center h-screen w-full px-5 sm:px-0">
                <div className="flex bg-white rounded-lg shadow-lg overflow-hidden lg:max-w-full w-full h-full">
                    <div className="w-full p-8 lg:w-1/2 flex flex-col justify-center">
                        <p className="font-bold text-center text-7xl">
                            AutoCazing
                        </p>
                        <div className="mt-20">
                            <label className="block text-gray-700 font-bold mb-2 text-2xl">
                                ID
                            </label>
                            <input
                                className="mx-auto text-gray-700 border border-gray-300 rounded py-2 px-4 block w-full  focus:outline-2  max-w-150 focus:outline-blue-700"
                                type="email"
                                required
                            />
                        </div>
                        <div className="mt-8 flex flex-col justify-between">
                            <div className="flex justify-between">
                                <label className="block text-gray-700 text-sm font-bold mb-2 text-2xl">
                                    Password
                                </label>
                            </div>
                            <input
                                className="mx-auto text-gray-700 border border-gray-300 rounded py-2 px-4 block w-full focus:outline-2 max-w-150 focus:outline-blue-700"
                                type="password"
                            />
                        </div>
                        <div className="mt-12">
                            <button className="bg-blue-700 text-white font-bold py-2 px-4 mx-auto w-full rounded max-w-100  hover:bg-blue-600">
                                Login
                            </button>
                        </div>
                    </div>
                    <div
                        className="hidden md:block lg:w-1/2 bg-cover bg-blue-700"
                        style={{
                            backgroundImage: `url(https://www.tailwindtap.com//assets/components/form/userlogin/login_tailwindtap.jpg)`,
                        }}
                    ></div>
                </div>
            </div>
        </>
    );
};

export default LoginPage;

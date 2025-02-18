import React, { useState, useEffect } from "react";
// import { MdRestaurantMenu } from "react-icons/md";
// import { GiTakeMyMoney } from "react-icons/gi";
// import { FaRegMoneyBillAlt } from "react-icons/fa";
import { TbTrash } from "react-icons/tb";
import { FiPlusCircle, FiMinusCircle } from "react-icons/fi";
// import { useReactToPrint } from "react-to-print";

import { GetMenu } from "../../apis/server/Pos";
import { PostOrders } from "../../apis/server/Pos";
import { MakeAlarm } from "../../apis/server/Alarm";
import Swal from "sweetalert2";

function Pos() {
    const [cart, setCart] = useState([]);
    const [paymentMode, setPaymentMode] = useState("");
    const [products, setProducts] = useState([]); // get/menus를 통해 받아온 전체 메뉴

    const [total, setTotal] = useState(0);

    const { data: Menu } = GetMenu(); // 서버 통신용 코드(Get)
    const postOrders = PostOrders(); //  서보 통신용 코드(Post)

    {
        console.log(products);
    }
    const handleSubmit = (extractedProducts) => {
        // console.log(extractedProducts);
        postOrders.mutate({
            storeId: 1,
            orderSpecifics: extractedProducts.map((product) => ({
                menuId: product.menuId,
                menuQuantity: product.quantity,
            })),
        });
        setTotal(0);
        setCart([]);
        setPaymentMode(null);
    };

    const addToCart = (productId, soldOut) => {
        if (soldOut) {
            // 만약 상품이 soldOut이면 아무것도 하지 않음
            return;
        }
        const found = cart.some((el) => el.menuId === productId);
        if (found) {
            const newProd = products.map((p) =>
                p.menuId === productId
                    ? { ...p, quantity: (p.quantity += 1) }
                    : p,
            );
            const newCart = cart.map((p) =>
                p.menuId === productId
                    ? {
                          ...p,
                          quantity: newProd.find(
                              (item) => item.menuId === productId,
                          ).quantity,
                          realPrice:
                              p.realPrice +
                              newProd.find((item) => item.menuId === productId)
                                  .realPrice,
                      }
                    : p,
            );

            let sum = newCart.reduce(function (acc, obj) {
                console.log(products);
                console.log(obj);
                return acc + obj.realPrice;
            }, 0);
            setTotal(sum);

            setCart(newCart);
        } else {
            let newProd = products.filter((p) => p.menuId === productId);
            newProd[0].quantity = 1;
            setTotal(
                total +
                    newProd.find((item) => item.menuId === productId).realPrice,
            );
            // console.log(newProd[0].realPrice);
            setCart(() => [...cart, ...newProd]);
        }
    };

    const increase = (productId) => {
        const found = cart.some((el) => el.menuId === productId);
        if (found) {
            const newProd = products.map((p) =>
                p.menuId === productId
                    ? { ...p, quantity: (p.quantity += 1) }
                    : p,
            );
            const newCart = cart.map((p) =>
                p.menuId === productId
                    ? {
                          ...p,
                          quantity: newProd.find(
                              (item) => item.menuId === productId,
                          ).quantity,
                          realPrice:
                              p.realPrice +
                              newProd.find((item) => item.menuId === productId)
                                  .realPrice,
                      }
                    : p,
            );

            let sum = newCart.reduce(function (acc, obj) {
                return acc + obj.realPrice;
            }, 0);
            setTotal(sum);

            setCart(newCart);
        } else {
            let newProd = products.filter((p) => p.menuId === productId);
            newProd[0].quantity = 1;
            setTotal(
                total +
                    newProd.find((item) => item.menuId === productId).realPrice,
            );
            // console.log(newProd[0].realPrice);
            setCart(() => [...cart, ...newProd]);
        }
    };

    const decrease = (productId) => {
        let decProd = products.filter((p) => p.menuId === productId);
        // console.log(decProd);
        const newProd = products.map((p) =>
            p.menuId === productId ? { ...p, quantity: (p.quantity -= 1) } : p,
        );
        const newCart = cart.map((p) =>
            p.menuId === productId
                ? {
                      ...p,
                      quantity: newProd.find(
                          (item) => item.menuId === productId,
                      ).quantity,
                      realPrice:
                          p.realPrice -
                          newProd.find((item) => item.menuId === productId)
                              .realPrice,
                  }
                : p,
        );

        const filtered = newCart.filter((p) => p.quantity > 0);
        let sum = filtered.reduce(function (acc, obj) {
            return acc + obj.realPrice;
        }, 0);
        setTotal(sum);

        setCart(filtered);
    };

    const clearAll = () => {
        // setProducts([]);
        setTotal(0);
        setCart([]);
        setPaymentMode(null);
    };

    const remove = (menuId) => {
        let newCart = cart.filter((p) => p.menuId !== menuId);
        setCart(newCart);
    };

    let formatCurrency = new Intl.NumberFormat("ko-KR", {
        style: "currency",
        currency: "KRW",
    });

    const order = () => {
        const extractedProducts = cart
            .filter(
                (item) => item.quantity !== undefined && item.quantity !== 0,
            ) // quantity가 undefined가 아닌 항목만 선택
            .map((item) => ({
                menuId: item.menuId,
                quantity: item.quantity,
            }));
        console.log("Extracted Products:", extractedProducts);

        if (extractedProducts.length > 0) {
            handleSubmit(extractedProducts); // 주문 데이터 서버로 전송
            Swal.fire({
                title: "주문 완료!",
                text: "주문이 성공적으로 처리되었습니다.",
                icon: "success",
                iconColor: "#3C50E0", // 아이콘 색상 설정
                confirmButtonText: "확인",
                confirmButtonColor: "#3C50E0", // 버튼 색상 설정
                buttonsStyling: true,
            });
        } else {
            Swal.fire({
                title: "주문 실패",
                text: "주문할 상품을 선택해주세요.",
                icon: "error",
                confirmButtonText: "확인",
            });
        }
    };

    useEffect(() => {
        // console.log(Menu);
        if (Menu !== undefined) {
            const updatedMenu = Menu.map((product) => ({
                ...product,
                realPrice: product.onEvent
                    ? product.menuPrice * (1 - product.discountRate / 100)
                    : product.menuPrice,
            }));
            setProducts(updatedMenu); // products 상태 업데이트
        }
    }, [Menu]);

    return (
        <section className="w-full p-4 bg-gray-200 h-screen overflow-auto">
            <div className="grid grid-cols-12 w-full h-full gap-2">
                {/* right side  */}
                <div className="col-span-6 bg-slate-50/50 rounded min-h-max w-full pt-4 px-2">
                    {/* header  */}
                    <div className="header flex items-baseline justify-between">
                        <h2 className="font-semibold text-base text-gray-800 leading-3 whitespace-nowrap ">
                            Choose Category{" "}
                        </h2>
                    </div>

                    <div className="flex my-3 px-2 justify-between items-center">
                        <h4 className="font-semibold text-gray-600 text-sm">
                            All
                        </h4>
                        <p className="font-semibold text-[0.7rem] text-gray-500">
                            {products?.length} results
                        </p>
                    </div>

                    {/* cards  */}

                    <div className="grid grid-cols-1  md:grid-cols-2 lg:grid-cols-3 gap-2 lg:gap-4">
                        {products?.map((product) => (
                            <div
                                className={`col-span-1 lg:py-8 bg-white rounded-md shadow-sm px-2 py-3 group hover:shadow-lg hover:scale-[102%] transition duration-300 ease-linear relative`}
                                onClick={() =>
                                    addToCart(product.menuId, product.soldOut)
                                }
                                key={product.menuName}
                            >
                                {/* Sold Out 레이블 추가 */}
                                {product.soldOut && (
                                    <div className="absolute top-0 left-0 w-full h-full bg-black bg-opacity-50 flex justify-center items-center">
                                        <p className="text-red-500 font-bold text-2xl transform -rotate-45">
                                            Sold Out
                                        </p>
                                    </div>
                                )}

                                <div className="px-0 h-20 lg:h-28 rounded-lg">
                                    <img
                                        src={product?.imageUrl}
                                        alt="img"
                                        className="w-full rounded h-full"
                                    />
                                </div>
                                <p className="text-center text-xs font-medium py-4 leading-tight break-all ">
                                    {product?.menuName}
                                </p>
                                <p className="font-bold py-2 bg-gray-200 text-center rounded text-base">
                                    {" "}
                                    {product.onEvent ? (
                                        <>
                                            <span className="line-through text-gray-500">
                                                {product?.menuPrice.toLocaleString()}{" "}
                                                원
                                            </span>
                                            <br />
                                            <span className="text-red-500">
                                                {product?.realPrice.toLocaleString()}{" "}
                                                원
                                            </span>
                                        </>
                                    ) : (
                                        `${product?.realPrice} 원`
                                    )}
                                </p>
                            </div>
                        ))}
                    </div>
                </div>
                {/*  
          //? CART  SECTION
        */}
                <aside className="col-span-6 bg-white rounded-lg shadow-lg min-h-max px-3 py-4">
                    {/* cart items  */}

                    <div className="flex justify-between items-center py-2">
                        <p className="font-bold text-base">Order</p>
                        <button
                            onClick={clearAll}
                            className="font-bold text-gray-600 bg-slate-100 px-2 rounded-md"
                        >
                            clear all
                        </button>
                    </div>

                    <div>
                        {cart?.map((p) => (
                            <div
                                key={p.menuId}
                                className="product flex flex-col md:flex-row justify-between items-center bg-slate-200 px-1 rounded-xl  gap-y-2 pb-3 my-2"
                            >
                                <div className="flex py-2 px-1 items-center">
                                    <div className="h-16 w-16 hidden lg:inline-block">
                                        <img
                                            src={p.imageUrl}
                                            alt="img"
                                            className="w-full rounded h-full"
                                        />
                                    </div>
                                    <div className="ml-1 px-1">
                                        <p className="text-xs md:text-sm font-bold text-gray-500 py-2">
                                            {p.menuName}
                                        </p>
                                        <p className="font-semibold text-sm  md:text-base">
                                            {formatCurrency.format(p.realPrice)}
                                        </p>
                                    </div>
                                </div>
                                <div className="flex items-center gap-3">
                                    <button
                                        onClick={() => decrease(p.menuId)}
                                        className=""
                                    >
                                        {" "}
                                        <FiMinusCircle className="text-xl md:text-2xl" />{" "}
                                    </button>
                                    <p className="font-bold">{p.quantity}</p>
                                    <button
                                        onClick={() => increase(p.menuId)}
                                        className=""
                                    >
                                        {" "}
                                        <FiPlusCircle className="text-xl md:text-2xl" />{" "}
                                    </button>
                                    <TbTrash
                                        className="mr-2 text-lg md:text-xl"
                                        onClick={() => remove(p.menuId)}
                                    />
                                </div>
                            </div>
                        ))}
                    </div>
                    {/* totals  */}
                    <div className="py-5 bg-slate-100 px-2 rounded shadow-sm">
                        <div className="flex justify-between">
                            <p className="font-bold text-sm">Totals</p>
                            <p className="font-bold text-sm">
                                {formatCurrency.format(total)}
                            </p>
                        </div>
                    </div>

                    {cart.length > 0 && (
                        <button
                            onClick={order}
                            className="w-full mt-4 bg-green-400 rounded py-1 font-bold text-gray-700"
                        >
                            주문하기
                        </button>
                    )}
                </aside>
            </div>
        </section>
    );
}

export default Pos;

import { GetBasket } from "../../apis/server/OrderApi";
import CartListTable from "../../components/order/CartListTable";

const CartList = () => {
    const { data: Basket } = GetBasket();

    return (
        <>
            <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                <h2 className="text-title-md2 font-semibold text-black dark:text-white">
                    장바구니
                </h2>
                <ol className="flex items-center gap-2">
                    <li>발주 /</li>
                    <li className="font-bold text-primary">장바구니</li>
                </ol>
            </div>
            <CartListTable Basket={Basket} />
        </>
    );
};

export default CartList;

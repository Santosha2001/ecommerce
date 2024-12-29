import { use, useContext } from "react";
import { createContext } from "react";

const CartContext = createContext();

const initialState = {
    cart: JSON.parse(localStorage.getItem("cart")) || [],
};

const cartReducer = (state, action) => {
    switch (action.type) {
        case "ADD_TO_CART":
            // identify if the item is already in the cart

            const existItem = state.cart.find((item) => item.id === action.payload.id);
            let newCart;

            if (existItem) {
                newCart = state.cart.map((item) =>
                    item.id === action.payload.id
                        ? { ...item, quantity: item.quantity + 1 }
                        : item
                );

            } else {
                newCart = [...state.cart, { ...action.payload, quantity: 1 }];
            }
            localStorage.setItem("cart", JSON.stringify(newCart));

            return { ...state, cart: newCart };

        case "REMOVE_ITEM":
            const newCartAfterRemove = state.cart.filter(
                (item) => item.id !== action.payload.id
            );
            localStorage.setItem("cart", JSON.stringify(newCartAfterRemove));

            return { ...state, cart: newCartAfterRemove };

        case "INCREASE":
            const increaseCart = state.cart.map((item) =>
                item.id === action.payload.id
                    ? { ...item, quantity: item.quantity + 1 }
                    : item
            );
            localStorage.setItem("cart", JSON.stringify(increaseCart));

            return { ...state, cart: increaseCart };

        case "DECREASE":
            const decreaseCart = state.cart.map((item) =>
                item.id === action.payload.id && item.quantity > 1
                    ? { ...item, quantity: item.quantity - 1 }
                    : item
            );
            localStorage.setItem("cart", JSON.stringify(decreaseCart));

            return { ...state, cart: decreaseCart };

        case "CLEAR":
            localStorage.removeItem("cart");
            return { ...state, cart: [] };

        default:
            return state;
    }
};

export const CartProvider = ({ children }) => {
    const [state, dispatch] = useReducer(cartReducer, initialState);

    useEffect(() => {
        localStorage.setItem("cart", JSON.stringify(state.cart));
    }, [state.cart]);

    return (
        <CartContext.Provider value={{ ...state, dispatch }}>
            {children}
        </CartContext.Provider>
    );
};

export const useCart = () => { useContext(CartContext) };

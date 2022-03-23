import React from "react";
import CartData from "../data/CartData";
import './Cart.css'
import CartItem from "../components/CartItem";
import PriceFormatter from "../utils/PriceFormatter";
import ShippingCostUtil from "../utils/ShippingCostUtil";
import {PayPalButtons, PayPalScriptProvider} from "@paypal/react-paypal-js";
import TransactionData from "../data/TransactionData";

const initialOptions = {
    "client-id": "AU2IKBTYdmMBojaEYo51063a3jCRviBQjLnUEzrTKdc6E7hTAeTJp5unflrXezPmDrxVQ13UWtC4F5fZ",
    currency: "USD",
    intent: "capture",
};

class Cart extends React.Component {
    constructor(props) {
        super(props);
        this.state = {subtotal: CartData.calculateTotalPrice()/*, details: null*/}

        this.createOrder = this.createOrder.bind(this);
        this.onApprove = this.onApprove.bind(this);
    }

    createOrder(data, actions) {
        return actions.order.create({
            purchase_units: [{
                amount: {
                    value: this.state.subtotal + ShippingCostUtil.standardCost,
                },
            },
            ],
        });
    }

    onApprove(data, actions) {
        return actions.order.capture().then((details)=> {
            //TODO
            console.log(details)
            const products = Array.from(CartData.products.values())
            const ids = []
            let i = 0
            products.forEach((product)=> {
                ids[i++]=product.id
            })
            console.log(ids)
            const td = new TransactionData(
                details.id,
                details.create_time,
                ids,
                Array.from(CartData.quantities.values()),
                details.payer.name.given_name,
                details.payer.name.surname,
                details.payer.email_address,
                details.purchase_units[0].shipping.address.address_line_1,
                details.purchase_units[0].shipping.address.address_line_2,
                details.purchase_units[0].shipping.address.admin_area_2,
                details.purchase_units[0].shipping.address.admin_area_1,
                details.purchase_units[0].shipping.address.postal_code,
                details.purchase_units[0].shipping.address.country_code,
            )
            console.log(td)
            CartData.clear()
            this.navigate("/post-checkout", td)
        })
    }

    render() {
        if (CartData.isEmpty()) return(<div>Nothing in cart!</div>)
        return (
            <div className="cart-container">
                <div className="spacer"/>
                <div className="cart-items-container">
                    <h3>Items in Cart</h3>
                    <div className="cart-item-divider"/>
                    {this.getItems()}
                </div>
                <div className="spacer"/>
                <div className="order-summary">
                    <h3>Order Summary</h3>
                    <div className="cart-item-divider"/>
                    <div className="spacer"/>
                    <div className="order-summary-subtotal">Subtotal: {PriceFormatter.asPrice(this.state.subtotal)}</div>
                    <div className="order-summary-subtotal">Shipping: {PriceFormatter.asPrice(ShippingCostUtil.standardCost)}</div>
                    <div className="cart-item-divider"/>
                    <div className="spacer"/>
                    <div className="order-summary-total">Total: {PriceFormatter.asPrice(this.state.subtotal + ShippingCostUtil.standardCost)}</div>
                    {/*<button onClick={()=>{this.navigate("/checkout")}}>Checkout</button>*/}
                    <div className="spacer"/>
                    <PayPalScriptProvider options={initialOptions}>
                        <PayPalButtons
                            className="pay-pal-button"
                            style={{ layout: "horizontal" }}
                            createOrder={this.createOrder}
                            // onApprove={this.onApprove}
                            onApprove={(data, actions)=>{this.onApprove(data, actions)}}
                        />
                    </PayPalScriptProvider>
                </div>
                <div className="spacer"/>
            </div>
        )
    }

    getItems() {
        return Array.from(CartData.products.values()).map((val, idx, arr)=>(
            <CartItem
                onPriceChange={()=>{
                    this.setState({subtotal: CartData.calculateTotalPrice()})
                }}
                onNavigate={this.props.onNavigate} product={val}/>
        ))
    }

    navigate(path, transactionData) {
        this.props.onNavigate(path, transactionData)
    }
}

export default Cart;
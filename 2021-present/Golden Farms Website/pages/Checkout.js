import React from "react";
import './Product.css'
import CartData from "../data/CartData";
import {CardElement, Elements} from "@stripe/react-stripe-js";
import {loadStripe} from '@stripe/stripe-js';
import './Checkout.css'
import PriceFormatter from "../utils/PriceFormatter";
import ShippingCostUtil from "../utils/ShippingCostUtil";
// import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
// import {faEnvelope, faUser} from "@fortawesome/free-solid-svg-icons"

import { PayPalScriptProvider, PayPalButtons } from "@paypal/react-paypal-js";

class Checkout extends React.Component {
    stripePromise = loadStripe('pk_test_TYooMQauvdEDq54NiTphI7jx');

    constructor(props) {
        super(props);
        this.state = {subtotal: CartData.calculateTotalPrice(), total: CartData.calculateTotalPrice() + ShippingCostUtil.standardCost}

        this.createOrder = this.createOrder.bind(this);
        this.onApprove = this.onApprove.bind(this);
    }

    createOrder(data, actions) {
        return actions.order.create({
            purchase_units: [{
                    amount: {
                        value: this.state.total,
                    },
                },
            ],
        });
    }

    onApprove(data, actions) {
        return actions.order.capture().then(function(details) {
            // This function shows a transaction success message to your buyer.
            // alert('Transaction completed by ' + details.payer.name.given_name);
            this.navigate("/post-checkout")
        });
    }

    render() {
        if (CartData.isEmpty()) {
            return(
                <div>
                    Nothing in cart!
                </div>
            )
        }

        // const initialOptions = {
        //     "client-id": "AU2IKBTYdmMBojaEYo51063a3jCRviBQjLnUEzrTKdc6E7hTAeTJp5unflrXezPmDrxVQ13UWtC4F5fZ",
        //     currency: "USD",
        //     intent: "capture",
        //     "data-client-token": "abc123xyz==",
        // };
        const initialOptions = {
            "client-id": "AU2IKBTYdmMBojaEYo51063a3jCRviBQjLnUEzrTKdc6E7hTAeTJp5unflrXezPmDrxVQ13UWtC4F5fZ",
            currency: "USD",
            intent: "capture",
        };

        return (
            <div className="checkout-container">
                <Elements stripe={this.stripePromise}>
                    <form id="payment-form" className="payment-information-container">
                        <h3>Payment Information</h3>
                        <div className="checkout-divider"/>
                        <div className="user-container container">
                            <div className="sub-section-title">Contact/Account Info</div>
                            <label htmlFor="first">First Name</label>
                            <input name="first" id="first"/><br/>
                            <label htmlFor="last">Last Name</label>
                            <input name="last" id="last"/><br/>
                            <div className="checkout-vertical-spacer"/>
                            <label htmlFor="email">{/*<FontAwesomeIcon icon={faEnvelope}/> */}Email</label>
                            <input name="email" id="email"/><br/>
                            <div className="checkout-vertical-spacer"/>
                            <label htmlFor="password">Create Password</label>
                            <input name="password" id="password"/><br/>
                            <label htmlFor="repeat">Confirm Password</label>
                            <input name="repeat" id="repeat"/><br/>
                        </div>
                        <div className="checkout-divider"/>
                        <div className="address-container container">
                            <div className="sub-section-title">Shipping Address</div>
                            <label htmlFor="address">Address</label>
                            <input name="address" id="address"/><br/>
                            <label htmlFor="city">City</label>
                            <input name="city" id="city"/><br/>
                            <label htmlFor="state">State</label>
                            <input name="state" id="state"/><br/>
                            {/*<label htmlFor="country">Country</label>*/}
                            {/*<input name="country" id="country"/><br/>*/}
                            <label htmlFor="zip">Zip</label>
                            <input name="zip" id="zip"/><br/>
                        </div>
                        <div className="checkout-divider"/>
                        <div className="card-container">
                            <CardElement
                                options={{
                                    style: {
                                        base: {
                                            fontSize: '16px',
                                            color: '#000000',
                                            '::placeholder': {
                                                color: '#aab7c4',
                                            },
                                        },
                                        invalid: {
                                            color: '#9e2146',
                                        },
                                    },
                                }}
                            />
                        </div>
                        <div className="checkout-divider"/>
                        <button type="submit" onClick={()=>{}}>Submit</button>
                        <PayPalScriptProvider options={initialOptions}>
                            <PayPalButtons
                                style={{ layout: "horizontal" }}
                                createOrder={this.createOrder}
                                onApprove={this.onApprove}
                            />
                        </PayPalScriptProvider>
                    </form>
                    <div className="checkout-spacer"/>
                    <div className="checkout-order-summary">
                        <h3>Order Summary</h3>
                        <div className="checkout-divider"/>
                        <div className="order-summary-subtotal">Subtotal: {PriceFormatter.asPrice(this.state.subtotal)}</div>
                        <div className="order-summary-subtotal">Shipping: {PriceFormatter.asPrice(ShippingCostUtil.standardCost)}</div>
                        <div className="cart-item-divider"/>
                        <div className="order-summary-total">Total: {PriceFormatter.asPrice(this.state.total)}</div>
                    </div>
                </Elements>
            </div>
        )
    }

    navigate(path) {
        this.props.onNavigate(path)
    }
}

export default Checkout;
import React from "react";
import './CartItem.css'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import {faTimes} from "@fortawesome/free-solid-svg-icons"
import PriceFormatter from "../utils/PriceFormatter";
import CartData from "../data/CartData";
import PlusMinusPicker from "./PlusMinusPicker";

class CartItem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {quantity: CartData.getQuantity(this.props.product.id)}

        this.onPickerValueChange = this.onPickerValueChange.bind(this);
    }

    render() {
        if (!this.state.quantity || this.state.quantity < 1) return (
            <div>
                <div className="cart-item-removed">
                    removed
                    <button onClick={()=>{CartData.addProduct(this.props.product);this.setState({quantity: 1})}}>Undo</button>
                </div>
                <div className="cart-item-divider"/>
            </div>
        )
        return (
            <div>
                <div className="cart-item">
                    <img src={this.props.product.image} alt=""/>
                    <div className="cart-item-details">
                        <div className="cart-item-title">{this.props.product.title}</div>
                        <div className="cart-item-price">{PriceFormatter.asPrice(this.props.product.price)}</div>
                        {/*<div>Quantity: {this.state.quantity}</div>*/}
                        <button onClick={()=>{this.navigate("/product")}}>View Product</button>
                    </div>
                    <FontAwesomeIcon
                        className="icon"
                        icon={faTimes}
                        onClick={()=>{
                            //TODO("fix this")
                            CartData.removeProduct(this.props.product.id);
                            this.setState({quantity: CartData.getQuantity(this.props.product.id)})
                            this.props.onPriceChange()
                        }}
                    />
                    <div className="cart-item-actions">
                        <PlusMinusPicker onValueChange={this.onPickerValueChange} initialValue={this.state.quantity}/>
                        <div className="cart-item-price">{PriceFormatter.asPrice(this.props.product.price * this.state.quantity)}</div>
                    </div>
                </div>
                <div className="cart-item-divider"/>
            </div>
        )
    }

    onPickerValueChange(value) {
        CartData.setQuantity(this.props.product, value);
        this.setState({quantity: CartData.getQuantity(this.props.product.id)})

        this.props.onPriceChange()
    }

    navigate(path) {
        this.props.onNavigate(path, this.props.product)
    }
}

export default CartItem;
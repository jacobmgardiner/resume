import React from "react";
import './CartReceipt.css'

class CartReceipt extends React.Component {
    render() {
        return (
            <div className="cart-receipt-container">
                <p>TODO placeholder</p>
            </div>
        )
    }

    // getItems() {
    //     return Array.from(CartData.products.values()).map((val, idx, arr)=>(
    //         <CartItem
    //             onPriceChange={()=>{
    //                 this.setState({subtotal: CartData.calculateTotalPrice()})
    //             }}
    //             onNavigate={this.props.onNavigate} product={val}/>
    //     ))
    // }

    navigate(path, transactionInfo) {
        this.props.onNavigate(path, transactionInfo)
    }
}

export default CartReceipt;
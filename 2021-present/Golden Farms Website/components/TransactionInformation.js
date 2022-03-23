import React from "react";
import CartReceipt from "./CartReceipt";
import './TransactionInformation.css'

class TransactionInformation extends React.Component {

    render() {
        return (
            <div className="transaction-info-container">
                <h3>Transaction information</h3>
                <div className="post-checkout-divider"/>
                <CartReceipt transactionData={this.props.transactionData}/>
                <div className="post-checkout-divider"/>
                <div className="transaction-info-header">Name: </div>
                <div className="transaction-info-text">{this.props.transactionData.first_name} {this.props.transactionData.last_name}</div>
                <div className="post-checkout-divider"/>
                <div className="transaction-info-header">Email Address: </div>
                <div className="transaction-info-text">{this.props.transactionData.email}</div>
                <div className="post-checkout-divider"/>
                <div className="transaction-info-header">Shipping Address: </div>
                <div className="transaction-info-text">{this.props.transactionData.address1}</div>
                <div className="transaction-info-text">{this.props.transactionData.address2}</div>
                <div className="transaction-info-text">{this.props.transactionData.city}, {this.props.transactionData.state} {this.props.transactionData.postal}</div>
                <div className="transaction-info-text">{this.props.transactionData.country}</div>
                {/*TODO("show map and ask for confirmation")*/}
                <div className="post-checkout-divider"/>
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

export default TransactionInformation;
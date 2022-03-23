import React from "react";
import './PostCheckout.css'
import TransactionInformation from "../components/TransactionInformation";
import { doc, setDoc, getFirestore } from "firebase/firestore";

class PostCheckout extends React.Component {
    business_email = "test@example.com"

    constructor(props) {
        super(props);

        this.sendDataToFirebase = this.sendDataToFirebase.bind(this)
    }

    componentDidMount() {
        this.sendDataToFirebase()
            // .then((value)=>{
            //     console.log(value)
            // }, (reason)=>{
            //     console.log(reason)
            // })
    }

    render() {
        if (!this.props.transactionData) {
            return (
                //TODO("")
                <div>Something went wrong! Order not received!</div>
            )
        }
        return (
            <div className="post-checkout-container">
                <h2>Thank you for your support!</h2>
                <div className="message-container">
                    <h3>Your order was received!</h3>
                    <div className="post-checkout-divider"/>
                    <p>Thank you for supporting our small, local, family business. We will work as quickly as possible to ship your order to you.</p>
                    <div className="post-checkout-divider"/>
                    <p>If any of the information to the right appears to be incorrect or you have any other questions or concerns, please let us know at: </p>
                    <p><a href = {"mailto: " + this.business_email}>{this.business_email}</a></p>
                    <div className="post-checkout-divider"/>
                </div>
                <TransactionInformation transactionData={this.props.transactionData}/>
            </div>
        )
    }

    async sendDataToFirebase() {
        if (!this.props.transactionData) return
        await setDoc(doc(getFirestore(), "orders", this.props.transactionData.id), this.props.transactionData.toMap(), { merge: true });
    }
}

export default PostCheckout
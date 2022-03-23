import React from "react";
import {Button} from "@material-ui/core";
import CartData from "../data/CartData";
import './Product.css'
import PlusMinusPicker from "../components/PlusMinusPicker";

class Product extends React.Component {
    constructor(props) {
        super(props);
        this.state = {amount: 1}
    }


    render() {
        return (
            <div className="product">
                <img src={this.props.product.image} alt=""/>
                <div className="details">
                    <div className="details-title">{this.props.product.title}</div>
                    <p>{this.props.product.description}</p>
                    <PlusMinusPicker onValueChange={(value)=>{this.setState({amount: value})}} initialValue={1}/>
                    <Button onClick={()=>{
                        CartData.addProduct(this.props.product, this.state.amount)
                        this.navigate("/cart")
                    }}>Add to Cart</Button>
                </div>
            </div>
        )
    }

    navigate(path) {
        this.props.onNavigate(path)
    }
}

export default Product;
import React from "react";
import ProductsByCategory from "../components/ProductsByCategory";

class Shop extends React.Component {
    render() {
        return (
            <ProductsByCategory onNavigate={this.props.onNavigate}/>
        )
    }

    navigate(path) {
        this.props.onNavigate(path)
    }
}

export default Shop;
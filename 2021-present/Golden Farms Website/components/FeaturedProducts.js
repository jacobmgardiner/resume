import React from "react";
import './FeaturedProducts.css'
import Loading from "./Loading";

import {
    getFirestore,
    collection,
    getDocs,
    query,
    where,
} from "firebase/firestore";
import ProductPreview from "./ProductPreview";
import ProductData from "../data/ProductData";

class FeaturedProducts extends React.Component {
    constructor(props) {
        super(props);
        this.state = {productsList: null};
    }

    componentDidMount() {
        if (this.state.productsList) return
        this.getProducts().then(r => {
            this.setState({productsList: r.docs})
        });
    }

    render() {
        if (this.state.productsList) {
            return (
                <div>
                    <div id="product-list">{this.formatProducts()}</div>
                    {/*<div className="vertical-spacer"/>*/}
                </div>
            );
        } else {
            return (
                <div>
                    <Loading/>
                </div>
            );
        }
    }

    formatProducts() {
        return this.state.productsList.map(doc => {
            const product = doc.data();
            return (
                <div key={doc.id}>
                    <ProductPreview
                        onNavigate={this.props.onNavigate}
                        product={new ProductData(doc.id, product.title, product.description, product.price, product.categoryNum, product.image)}
                    />
                </div>
            )
        })
    }

    async getProducts() {
        return await getDocs(query(collection(getFirestore(), "products"), where("featured", "==", true)))
    }
}

export default FeaturedProducts
import React from "react";
// import {Grid} from "@material-ui/core";

import {
    getFirestore,
    collection,
    query,
    where,
    getDocs
} from "firebase/firestore";

import Loading from './Loading'
import ProductPreview from "./ProductPreview";

import './CategoryList.css'
import ProductData from "../data/ProductData";

class CategoryList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {productsList: null};
    }

    componentDidMount() {
        if (this.state.productsList) return
        this.getProducts(this.props.category).then(r => {
            this.setState({productsList: r.docs})
        });
    }

    render() {
        if (this.state.productsList) {
            return (
                <div className="category-container">
                    <h1>{this.props.title}</h1>
                    <div className="product-list">{this.formatProducts()}</div>
                </div>
            );
        } else {
            return (
                <div>
                    <h1>{this.props.title}</h1>
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

    async getProducts(category) {
        return await getDocs(query(collection(getFirestore(), "products"), where("categoryNum", "==", parseInt(category))))
    }
}

export default CategoryList;
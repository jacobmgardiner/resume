import React from "react";
// import {Grid} from "@material-ui/core";

import {
    getFirestore,
    collection,
    getDocs,
    query,
    orderBy,
} from "firebase/firestore";

import Loading from './Loading'
import './ProductsByCategory.css'
import CategoryList from "./CategoryList";

class ProductsByCategory extends React.Component {
    constructor(props) {
        super(props);
        this.state = {categoriesList: null};
    }

    componentDidMount() {
        if (this.state.categoriesList) return
        this.getCategories().then(r => {
            this.setState({categoriesList: r.docs})
        });
    }

    render() {
        if (this.state.categoriesList) {
            return (
                <div id="categories-container">
                    {this.state.categoriesList.map((doc) => {
                        const category = doc.data()
                        return (
                            <div>
                                <CategoryList onNavigate={this.props.onNavigate} title={category.title} category={category.num}/>
                            </div>
                        )
                    })}
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

    async getCategories() {
        return await getDocs(query(collection(getFirestore(), "categories"), orderBy("num")))
    }
}

export default ProductsByCategory;
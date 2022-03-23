import React from "react";
import './ProductPreview.css'
import PriceFormatter from '../utils/PriceFormatter.js'
import { getStorage, ref, getDownloadURL } from "firebase/storage";

class ProductPreview extends React.Component {
    componentDidMount() {
        this.getImgUrl('image')
    }

    render() {
        return (
            <div className="product-preview" onClick={()=>{this.navigate("/product")}}>
                <img id="image" src="" alt=""/>
                <div className="text-container">
                    <div id="text-heading">
                        <div id="title">{this.props.product.title}</div>
                        <div id="price">{PriceFormatter.asPrice(this.props.product.price)}</div>
                    </div>
                    <p id="description">{this.props.product.description}</p>
                </div>
            </div>
        );
    }

    getImgUrl(imageId) {
        getDownloadURL(ref(getStorage(), this.props.product.image))
            .then((url) => {
                // `url` is the download URL for 'images/stars.jpg'
                // Or inserted into an <img> element
                const img = document.getElementById(imageId);
                img.setAttribute('src', url);
                console.log(url)
            })
            .catch((error) => {
                // Handle any errors
            });
    }

    navigate(path) {
        this.props.onNavigate(path, this.props.product)
    }
}

export default ProductPreview;
import React from "react";
import './Home.css'
import { getStorage, ref, getDownloadURL } from "firebase/storage";
import FeaturedProducts from "../components/FeaturedProducts";


class Home extends React.Component {
    constructor(props) {
        super(props);

        this.getImgUrl = this.getImgUrl.bind(this)
    }


    render() {
        return (
            <div className="home-container">
                <img id="image-main" src="" alt=""/>

                {/*<div className="home-divider"/>*/}

                <h3>Welcome to Golden Bee Gardens</h3>
                <p>Golden Bee Gardens is a small family business devoted to providing the world with delicious honey. We take pride in making our products and service as quality as possible.</p>
                {/*TODO("navigate")*/}
                <a href="/about">Learn More</a>
                <div className="vertical-spacer"/>
                <div className="home-divider"/>

                <h3>Featured Products</h3>
                <FeaturedProducts onNavigate={this.props.onNavigate}/>
                <div className="vertical-spacer"/>
                {/*TODO("navigate")*/}
                <a href="/shop">See More Products</a>
                <div className="vertical-spacer"/>
                <div className="home-divider"/>

                <h3>Recent Posts</h3>
                <div className="home-divider"/>
            </div>
        )
    }

    componentDidMount() {
        this.getImgUrl('image-main', '/home-slideshow/az.jpg')
    }

    navigate(path) {
        this.props.onNavigate(path)
    }

    getImgUrl(imageId, path) {
        getDownloadURL(ref(getStorage(), path))
            .then((url) => {
                // Or inserted into an <img> element
                const img = document.getElementById(imageId);
                img.setAttribute('src', url);
            })
            .catch((error) => {
                // Handle any errors
            });
    }
}

export default Home;
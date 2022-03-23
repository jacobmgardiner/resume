import React from "react";
import Home from "../pages/Home";
import Shop from "../pages/Shop";
import NotFound from "../pages/NotFound";
import NavBar from "./NavBar";
import About from "../pages/About";
import Contact from "../pages/Contact";
import Product from "../pages/Product";
import Cart from "../pages/Cart";
import PostCheckout from "../pages/PostCheckout";

class NavigableContent extends React.Component {
    constructor(props) {
        super(props)
        this.state = {currentPage: window.location.pathname}

        this.onPageChange = this.onPageChange.bind(this);
    }

    componentDidMount() {
        console.log(window.location.pathname)
        if (window.location.pathname === "/") {
            this.onPageChange("/home")
        }
    }

    render() {
        return (
            <div>
                <NavBar onNavigate={this.onPageChange}/>
                {this.getChangeable(this.state.currentPage)}
            </div>
        )
    }

    getChangeable(page) {
        switch (page) {
            case "/home":
                return (<Home onNavigate={this.onPageChange}/>);
            case "/shop":
                return (<Shop onNavigate={this.onPageChange}/>);
            case "/about":
              return (<About onNavigate={this.onPageChange}/>);
            case "/contact":
              return (<Contact onNavigate={this.onPageChange}/>);
            case "/product":
                return (<Product product={this.state.data} onNavigate={this.onPageChange}/>);
            case "/cart":
                return (<Cart onNavigate={this.onPageChange}/>);
            // case "/checkout":
            //     return (<Checkout onNavigate={this.onPageChange}/>);
            case "/post-checkout":
                return (<PostCheckout transactionData={this.state.data} onNavigate={this.onPageChange}/>);
            default:
                return (<NotFound onNavigate={this.onPageChange}/>)
        }
    }

    onPageChange(path, data) {
        this.setState({currentPage: path, data: data})
        window.history.replaceState(null, "New Page Title", path)
    }
}

export default NavigableContent;
import React from "react";

import './NavBar.css'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import {faFacebook, faInstagram} from "@fortawesome/free-brands-svg-icons"
import {faShoppingCart} from "@fortawesome/free-solid-svg-icons"

class NavBar extends React.Component {
    render() {
        return (
            <div>
                <ul>
                    {/*<li><a href="home">Home</a></li>*/}
                    {/*<li><a href="shop">Shop</a></li>*/}
                    {/*<li><a href="about">About</a></li>*/}
                    {/*<li><a href="contact">Contact</a></li>*/}
                    {/*<div id="nav-bar-right">*/}
                    {/*    <li><a href=""><FontAwesomeIcon icon={faInstagram} /></a></li>*/}
                    {/*    <li><a href=""><FontAwesomeIcon icon={faFacebook} /></a></li>*/}
                    {/*    <li><a href="cart"><FontAwesomeIcon icon={faShoppingCart} /></a></li>*/}
                    {/*</div>*/}
                    <li><div onClick={() => {this.navigate("/home")}}>Home</div></li>
                    <li><div onClick={() => {this.navigate("/shop")}}>Shop</div></li>
                    <li><div onClick={() => {this.navigate("/about")}}>About</div></li>
                    <li><div onClick={() => {this.navigate("/contact")}}>Contact</div></li>
                    <div id="nav-bar-right">
                        {/*TODO("use real links")*/}
                        <li><a href="https://www.instagram.com/"><FontAwesomeIcon icon={faInstagram}/></a></li>
                        <li><a href="https://www.facebook.com/"><FontAwesomeIcon icon={faFacebook}/></a></li>
                        {/*TODO("indicate items in cart, maybe even number of items")*/}
                        <li><div onClick={() => {this.navigate("/cart")}}><FontAwesomeIcon icon={faShoppingCart} /></div></li>
                    </div>
                </ul>
            </div>
        )
    }

    navigate(path) {
        this.props.onNavigate(path)
    }
}

export default NavBar;
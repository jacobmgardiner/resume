import './App.css';
import React from "react";

// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
// import { getAnalytics } from "firebase/analytics";

import NavigableContent from "./components/NavigableContent";

import logo from './images/logo_placeholder.png'

function App() {
  return (
    <div className="App">
      <img src={logo} alt="logo"/>
      <NavigableContent/>
    </div>
  );
}

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
};

// Initialize Firebase
/*const app = */initializeApp(firebaseConfig);
// const analytics = getAnalytics(app);

export default App;

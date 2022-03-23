import React from "react";
import './PlusMinusPicker.css'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import {faMinus, faPlus} from "@fortawesome/free-solid-svg-icons"

class PlusMinusPicker extends React.Component {
    constructor(props) {
        super(props);
        this.state = {value: this.props.initialValue}
    }

    render() {
        return (
            <div>
                <FontAwesomeIcon onClick={()=>{if (this.state.value === 1) return;this.props.onValueChange(this.state.value - 1);this.setState({value: this.state.value - 1})}} className="plusminus-elem plusminus-button minus" icon={faMinus}/>
                <div className="plusminus-elem">{this.state.value}</div>
                <FontAwesomeIcon onClick={()=>{this.props.onValueChange(this.state.value + 1);this.setState({value: this.state.value + 1})}} className="plusminus-elem plusminus-button plus" icon={faPlus}/>
            </div>
        )
    }
}

export default PlusMinusPicker;
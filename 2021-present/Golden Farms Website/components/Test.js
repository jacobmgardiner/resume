import React from "react";

export class Test extends React.Component {
    msg = "null";
    render() {
        return (
            <div className="test">
                {this.props.msg}
            </div>
        );
    }
}

export default Test;
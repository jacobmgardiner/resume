import React from "react";
import {CircularProgress} from "@material-ui/core";

class Loading extends React.Component {
    render() {
        return (
            <div className="loading">
                <CircularProgress />
            </div>
        );
    }
}

export default Loading;
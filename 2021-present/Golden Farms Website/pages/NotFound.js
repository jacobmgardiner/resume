import React from "react";

class NotFound extends React.Component {
    render() {
        return (
            <div>
                <p>Couldn't find the page.</p>
            </div>
        )
    }

    navigate(path) {
        this.props.onNavigate(path)
    }
}

export default NotFound;
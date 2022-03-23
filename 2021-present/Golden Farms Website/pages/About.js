import React from "react";

class About extends React.Component {
    render() {
        return (
            <div>
                <p>This will say some random stuff.</p>
            </div>
        )
    }

    navigate(path) {
        this.props.onNavigate(path)
    }
}

export default About;
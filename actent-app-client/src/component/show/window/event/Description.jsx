import React from 'react';

class Description extends React.Component {

    render() {

        return (
            <div>
                <h1>{this.props.description}</h1>
            </div>
        );
    }
}

export default Description;

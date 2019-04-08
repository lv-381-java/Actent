import React from 'react';

class Chat extends React.Component {

    render() {

        return (
            <div>
                <h1>{this.props.chat}</h1>
            </div>
        );
    }
}

export default Chat;

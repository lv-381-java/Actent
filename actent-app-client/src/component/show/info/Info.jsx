import React from 'react';
import './Info.css';

class Info extends React.Component {

    render() {

        var duration = ((this.props.duration / 1000) / 60);
        var min = duration.toFixed(2);
        var date = this.props.startDate;

        if (date) {
            var d = date.replace("T", ", ");
        }

        return (

            <div className='tet1'>
                <h1>{this.props.info}</h1>
            
                <h4 >Start date: {d}</h4>
                <h4>Duration: {min} minutes</h4>
                <h4>Capacity: {this.props.capacity}</h4>
                <h4>Category: {this.props.category}</h4>
                <h4>Creator:</h4>
                <h4> First name: {this.props.creatorFirstName}</h4>
                <h4> Last name: {this.props.creatorLastName}</h4>
            
            </div>
        );
    }
}

export default Info;

import React from 'react';
import Description from './Description.jsx';
import Image from './Image.jsx';
import './Event.css';

class Event extends React.Component {

    render() {

        return (

            <div className='event-container'>

                <div className='box-1 box'>
                    <Image image={this.props.image}/>
                </div>

                <div className='box-2 box'>
                    <Description description={this.props.description}/>
                </div>

            </div>
        );
    }
}

export default Event;

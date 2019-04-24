import React from 'react';
import Description from './Description.jsx';
import './Event.css';
import { getImageUrl } from '../../../profile/ProfileView.js';

class Event extends React.Component {

    render() {

        let image = undefined;

        if  (this.props.image) {
            image =(
                
                    <img src={getImageUrl(this.props.image.filePath)} className='imageEvent'></img>
                
            );
            console.log(this.props.image.filePath);
        } else {
            image = (
                
                    <img src="https://www.kaskademusic.com/wp-content/uploads/2018/06/sunsoaked-2018.jpg" className='img-responsive my-pic'></img>
                
            )
        }

        return (

            <div className='event-container'>

                <div className='box-1 box'>
                    {image}
                </div>

                <div className='box-2 box tet'>
                    <Description description={this.props.description}/>
                </div>

            </div>
        );
    }
}

export default Event;

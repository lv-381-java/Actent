import React from 'react';
import './Review.css';

class ReviewItem extends React.Component {

    render() {

        return (
            <div className='container'>
                <div className='hb box'>
                    <h3>{this.props.review}</h3>
                </div>
            </div>
        );
    }
}

export default ReviewItem;

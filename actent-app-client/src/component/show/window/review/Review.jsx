import React from 'react';
import ReviewList from './ReviewList.jsx';

class Review extends React.Component {

    render() {

        return (
            <div>
                <ReviewList reviews={this.props.reviews}/>
            </div>
        );
    }
}

export default Review;

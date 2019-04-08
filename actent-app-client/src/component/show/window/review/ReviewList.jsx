import React from 'react';
import ReviewItem from './ReviewItem.jsx';
import './ReviewList.css';

export default function ReviewList({ reviews }) {

    const reviewElements = reviews.map(review =>
        <li key = {review} className='list'>
            <ReviewItem review={review}/>
        </li>
    )

    return (
        <ul>
            {reviewElements}
        </ul>
    );
}

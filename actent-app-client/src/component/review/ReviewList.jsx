import React, { Component } from 'react';
import Review from './Review';
import Grid from '@material-ui/core/Grid';
import { Divider } from '@material-ui/core';
import { getAllUserReviews } from '../../util/apiUtils';

export default class ReviewList extends Component {
    state = {
        reviews: [],
    };

    constructor(props) {
        super(props);
        this.getUserReviews(props.match.params.userId)
            .then(res =>
                this.setState({
                    ...this.state,
                    reviews: res.data,
                }),
            )
            .catch(e => console.log(e));
    }

    getUserReviews = async userId => getAllUserReviews(userId);

    render() {
        return (
            <Grid container direction='column'>
                {this.state.reviews.map(currentReview => (
                    <Grid item key={currentReview.id}>
                        <Divider style={{ marginTop: 5 }} />
                        <Review review={currentReview} />
                        <Divider style={{ marginBottom: 5 }} />
                    </Grid>
                ))}
            </Grid>
        );
    }
}

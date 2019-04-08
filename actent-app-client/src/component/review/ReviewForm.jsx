import React, { Component } from 'react';
import TextField from '@material-ui/core/TextField';
import { Grid, Typography, Divider, Button } from '@material-ui/core';
import Ratings from 'react-ratings-declarative';
import { addReviewToUser, getUserById } from '../../util/apiUtils';
import { Redirect } from 'react-router-dom';

export default class ReviewForm extends Component {
    state = {
        review: {
            text: '',
            score: 0,
        },
        target: {},
    };

    constructor(props) {
        super(props);
        this.state.target.type = props.targetType ? props.targetType : 'user';
        this.state.target.id = props.match.params.targetId;
        getUserById(props.match.params.targetId).then(res =>
            this.setState({
                ...this.state,
                target: {
                    ...this.state.target,
                    name: res.data.firstName,
                },
            }),
        );
    }

    handleChangeRating = newRating => {
        this.setState({
            ...this.state,
            review: {
                ...this.state.review,
                score: newRating,
            },
        });
    };

    handleChangeReviewText = event =>
        this.setState({
            ...this.state,
            review: {
                ...this.state.review,
                text: event.target.value,
            },
        });

    submitUserReview = async userId => {
        const review = this.state.review;

        if (review.score < 1 || review.score > 5) {
            alert(`Bad score: ${review.score}. Score must be in range from 1 to 5.`);
            return Promise.reject(`Bad score: ${review.score}`);
        }

        if (review.text.length < 10 || review.text.length > 500) {
            alert(`Bad text length, must be in range from 10 to 500.`);
            return Promise.reject('Bad text length.');
        }

        let redirect;

        addReviewToUser(
            {
                score: review.score,
                text: review.text,
            },
            this.state.target.id,
        ).then(alert(`Review successfully added.`));
    };

    render() {
        const name = this.state.target.name;
        const targetId = this.state.target.id;

        return (
            <React.Fragment>
                {name && targetId ? (
                    <Grid container direction='column' style={{ padding: 24 }}>
                        <Grid item>
                            <Typography variant='h5'>Write a review for {name}</Typography>
                        </Grid>
                        <Divider variant='fullWidth' style={{ marginTop: 10, marginBottom: 10 }} />
                        <Grid item>
                            <Typography variant='subtitle1' style={{ marginBottom: 5 }}>
                                How would you recommend {name}?
                            </Typography>
                        </Grid>
                        <Grid item>
                            <Ratings
                                rating={this.state.review.score}
                                widgetHoverColors='rgb(145, 158, 166)'
                                changeRating={this.handleChangeRating}
                            >
                                <Ratings.Widget widgetDimension='24px' />
                                <Ratings.Widget widgetDimension='24px' />
                                <Ratings.Widget widgetDimension='24px' />
                                <Ratings.Widget widgetDimension='24px' />
                                <Ratings.Widget widgetDimension='24px' />
                            </Ratings>
                        </Grid>
                        <Grid item>
                            <Typography variant='body1' style={{ marginTop: 5 }}>
                                Your review will appear on {name}'s profile, so be sure that you're only sharing words
                                you're comfortable saying publicly.{' '}
                                <b>Once you will submit a review, you won't be able to edit or delete it.</b>
                            </Typography>
                        </Grid>
                        <Grid item>
                            <TextField
                                label={`What you can say about ${name}?`}
                                multiline
                                rows='5'
                                margin='normal'
                                variant='outlined'
                                fullWidth
                                onChange={this.handleChangeReviewText}
                            />
                        </Grid>
                        <Divider variant='fullWidth' style={{ marginTop: 10, marginBottom: 10 }} />
                        <Grid container item alignItems='flex-start' justify='flex-end' direction='row'>
                            <Button
                                variant='outlined'
                                style={{ marginTop: 5 }}
                                onClick={_ => this.submitUserReview(targetId)}
                            >
                                Submit review
                            </Button>
                        </Grid>
                    </Grid>
                ) : null}
            </React.Fragment>
        );
    }
}

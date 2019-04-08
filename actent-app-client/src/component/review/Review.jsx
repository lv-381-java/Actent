import React from 'react';
import Grid from '@material-ui/core/Grid';
import { Typography, Avatar } from '@material-ui/core';
import Ratings from 'react-ratings-declarative';

const Review = props => {
    return (
        <React.Fragment>
            {props.review ? (
                <Grid container direction='row' spacing={24} style={{ padding: 24 }}>
                    <Grid item>
                        <Avatar
                            src={props.review.avatar}
                            style={{ margin: 10, width: 70, height: 70, borderRadius: 3 }}
                        />
                    </Grid>
                    <Grid item container direction='column' sm style={{ paddingTop: 15 }}>
                        <Grid item sm>
                            <Typography variant='h5'>{props.review.first_name}</Typography>
                        </Grid>
                        <Grid item sm>
                            <Typography variant='subtitle1'>{`${props.review.city}, ${props.review.region}, ${
                                props.review.country
                            }`}</Typography>
                        </Grid>
                        <Grid item sm style={{ paddingBottom: 12, paddingTop: 12 }}>
                            <Ratings rating={props.review.score}>
                                <Ratings.Widget widgetDimension='24px' />
                                <Ratings.Widget widgetDimension='24px' />
                                <Ratings.Widget widgetDimension='24px' />
                                <Ratings.Widget widgetDimension='24px' />
                                <Ratings.Widget widgetDimension='24px' />
                            </Ratings>
                        </Grid>
                        <Grid item sm>
                            <Typography variant='body1'>{props.review.text}</Typography>
                        </Grid>
                    </Grid>
                </Grid>
            ) : (
                console.error('Bad review format.')
            )}
        </React.Fragment>
    );
};

export default Review;

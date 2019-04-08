import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import PresentEventsList from './PresentEventsList';
import PastEventsList from './PastEventsList';
import AllEventsList from './AllEventsList';

function TabContainer(props) {
    return (
        <Typography component='div' style={{padding: 8 * 3}}>
            {props.children}
        </Typography>
    );
}

TabContainer.propTypes = {
    children: PropTypes.node.isRequired,
};

const styles = theme => ({
    root: {
        flexGrow: 1,
        backgroundColor: theme.palette.background.paper,
    },
});

class SimpleTabs extends React.Component {
    handleChange = (event, value) => {
        this.props.setSelectTab(value);
    };

    render() {
        const {classes} = this.props;
        return (
            <div className={classes.root} >
                <AppBar position='static' color='default'>
                    <Tabs
                        indicatorColor={'primary'}
                        variant='fullWidth'
                        value={this.props.selectTab}
                        onChange={this.handleChange}>
                        <Tab label='All events'/>
                        <Tab label='Future events'/>
                        <Tab label='Past events'/>
                    </Tabs>
                </AppBar>
                {this.props.selectTab === 0 && (
                    <TabContainer>
                        <AllEventsList events={this.props.events}/>
                    </TabContainer>
                )}
                {this.props.selectTab === 1 && (
                    <TabContainer>
                        <PresentEventsList events={this.props.events}/>
                    </TabContainer>
                )}
                {this.props.selectTab === 2 && (
                    <TabContainer>
                        <PastEventsList events={this.props.events}/>
                    </TabContainer>
                )}
            </div>
        );
    }
}

SimpleTabs.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SimpleTabs);

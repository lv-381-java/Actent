import React from "react";
import ReactDOM from "react-dom";
import Profile from "./component/profile/Profile";
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {BrowserRouter as Router} from 'react-router-dom';
import {getMuiTheme} from "material-ui/styles";

const muiTheme = getMuiTheme();

class App extends React.Component {
    render() {
        return (
            <div>
                <Profile/>
            </div>
        );
    }
}

ReactDOM.render(
    <Router>
        <MuiThemeProvider>
            <App/>
        </MuiThemeProvider>
    </Router>,

    window.document.getElementById('root'));

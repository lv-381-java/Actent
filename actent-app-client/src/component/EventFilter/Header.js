import React from 'react';
import { MDBView, MDBMask } from 'mdbreact';
import HeaderInput from './HeaderInput';
import './pagination.css';

export default class Header extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            collapse: false,
            isWideEnough: false,
        };
        this.onClick = this.onClick.bind(this);
    }

    onClick() {
        this.setState({
            collapse: !this.state.collapse,
        });
    }

    render() {
        return (
            <div>
                <header>
                    <MDBView className='header-height' src='https://mdbootstrap.com/img/Photos/Others/img%20(40).jpg'>
                        <MDBMask overlay='black-strong' className='flex-center flex-column text-white text-center'>
                            <HeaderInput setTitle={this.props.setTitle} />
                        </MDBMask>
                    </MDBView>
                </header>
            </div>
        );
    }
}

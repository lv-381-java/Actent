import React from 'react';
import { MDBCol, MDBContainer, MDBRow, MDBFooter } from 'mdbreact';

const FooterPage = () => {
    return (
        <MDBFooter color='blue' className='font-small pt-4 mt-4'>
            <MDBContainer fluid className='text-center '>
                <MDBRow>
                    <MDBCol md='12' className='text-center '>
                        <h4 className='title'>Actent</h4>
                        <p>Specialized on your activities</p>
                    </MDBCol>
                </MDBRow>
            </MDBContainer>
            <div className='footer-copyright text-center py-3'>
                <MDBContainer fluid>
                    <a href='https://github.com/lv-381-java/Actent'> GitHub lv-381-java </a>
                </MDBContainer>
            </div>
        </MDBFooter>
    );
};

export default FooterPage;

import React from 'react';
import Dropzone from 'react-dropzone';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import { NotificationContainer, NotificationManager } from 'react-notifications';
import {AMAZON_S3_URL} from "../../constants/apiConstants";

const maxSize = 1048576;

export default class DownloadImage extends React.Component{

    constructor(props){
        super(props);
        this.state = {
            imageName: '',
            imageUrl: AMAZON_S3_URL + 'actent-images',
            open: false,
        };
    }

    dialogOpen = () => {
        this.setState({open: true});
    };

    dialogClose = () => {
        this.setState({open: false});
    };

    handleOnDrop = (files, rejectedFiles) => {

        if(rejectedFiles && rejectedFiles.length > 0){
            const currentRejectedFile = rejectedFiles[0];
            const currentRejectedFileSize = currentRejectedFile.size;
            if(currentRejectedFileSize > maxSize){
                NotificationManager.error('This file is too big!', 'Error!', 5000);
            }
        }

        if(files && files.length > 0) {
            const image = files[0];
            if(image.size > maxSize){
                NotificationManager.error('This file is too big!', 'Error!', 5000);
            }
            else{
                const imageData = new FormData();
                imageData.append('image', image);
                this.props.fetchData(imageData, image['name']);
                this.dialogClose();
            }
        }
    };

    render() {

        return(
            <div className='downloadImage'>
                <Button className="FormField__Button" variant="contained" color="primary" onClick={this.dialogOpen}>
                    Image
                </Button>
                <Dialog
                    className='dropzone'
                    open={this.state.open}
                    onClose={this.dialogClose}
                >
                    <DialogContent>
                        <Dropzone onDrop={this.handleOnDrop} multiple={false} accept='image/*' maxSize={maxSize}>
                            {({getRootProps, getInputProps}) => (
                                <div {...getRootProps()}>
                                    <input {...getInputProps()} />
                                    <p>Drag 'n' drop some files here, or click to select files</p>
                                </div>
                            )}
                        </Dropzone>
                    </DialogContent>
                </Dialog>

                <NotificationContainer />

            </div>


        );
    }

}
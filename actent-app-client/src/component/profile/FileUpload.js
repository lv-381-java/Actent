import React from 'react';
import Dialog from '@material-ui/core/Dialog';
import Dropzone from 'react-dropzone';
import Button from '@material-ui/core/Button';
import Snackbar from '@material-ui/core/Snackbar';
import {imageValidator} from './FileUploadValidator';
import {s3Root} from './ProfileView'

export default class FileUpload extends React.Component{

    constructor(props){
        super(props);
        this.state = {
            imageName: '',
            imageUrl: s3Root + 'actent-res',
            open: false,
            snackbarOpen: false,
            snackbarMessage: ''
        };
    }

    dialogOpen = () => {
        this.setState({open: true});
    };

    dialogClose = () => {
        this.setState({open: false});
    };

    showSnackbar = () => {

        this.setState({
            snackbarOpen: true,
        });
    };

    hideSnackbar = () => {
        this.setState({
            snackbarOpen: false,
        });
    };

    updateImageName = (imageName) => {
        // this.props.updateImageNameInDb(imageName);
    };

    onImageDrop = (files) => {
        this.setState({'snackbarMessage': ''});
        let image = files[0];
        let imageData = new FormData();
        let imageType = image.type.slice(6);
        let imageSize = image.size;
        if (imageValidator(imageType, imageSize)) {
            imageData.append('image', image);
            this.props.fetchData(imageData, image['name']);
            this.dialogClose();
        } else {
            this.setState({'snackbarMessage': 'Please choose another file'});
            this.showSnackbar();
        }
    };

    render() {
        const actions = [
            <Button
                label='Cancel'
                color="primary"
                variant="contained"
                onClick={this.dialogClose}
            >Cancel
            </Button>
        ];

        const maxSize = 5242880;

        return (
            <div className="buttonStyle">
                <Button
                    label="Upload image"
                    color="primary"
                    variant="contained"
                    onClick={this.dialogOpen}
                >Upload Image
                </Button>
                <Dialog
                    title='File Upload'
                    actions={actions}
                    modal={false}
                    open={this.state.open}
                    className="dialogStyle"
                    onRequestClose={this.dialogClose}>
                    <Dropzone
                        className="dropzoneStyle"
                        onDrop={this.onImageDrop}
                        minSize={0}
                        maxSize={maxSize}>
                        {({getRootProps, getInputProps, isDragActive, isDragReject, rejectedFiles}) => {
                            const isFileTooLarge = rejectedFiles.length > 0 && rejectedFiles[0].size > maxSize;
                            return (
                                <div {...getRootProps()}>
                                    <input {...getInputProps()} />
                                    {!isDragActive && 'Click here or drop a file to upload!'}
                                    {isDragActive && !isDragReject && "Drop it like it's hot!"}
                                    {isDragReject && "File type not accepted, sorry!"}
                                    {isFileTooLarge && (
                                        <div className="text-danger mt-2">
                                            File is too large.
                                        </div>
                                    )}
                                </div>
                            )}
                        }
                    </Dropzone>
                </Dialog>
                <Snackbar
                    style={{textAlign:'center'}}
                    open={this.state.snackbarOpen}
                    message={this.state.snackbarMessage}
                    autoHideDuration={5000}
                    onRequestClose={this.hideSnackbar}
                />
            </div>
        );
    }
}

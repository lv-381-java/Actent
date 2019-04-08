import React from 'react';
import './Event.css';

class Image extends React.Component {

    render() {

        if  (this.props.image) {
            return (
                <div>
                    <h1>{this.props.image}</h1>
                </div>
            );
        } else {
            return (
                <div>
                    {/* <img src={"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkuMDBpYUKMxK9VxkfZSNgZdHvP4xueFcGoBHKx5h-4ykw_cm2"} ></img> */}
                </div>
            )
        }
    }
}

export default Image;

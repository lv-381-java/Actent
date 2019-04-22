import React from "react";
import './styles.css';

const Input = props => {
    return (
        <div className='form-group'>
            <label htmlFor={props.name} className="form-label">
                {props.title}
            </label>
            <input
                className="form-control"
                id={props.name}
                name={props.name}
                type={props.type}
                min={props.min}
                max={props.min}
                value={props.value}
                onChange={props.handleChange}
                placeholder={props.placeholder}
                {...props}
            />
            <span>{props.errorMessage}</span>
        </div>
    );
};

export default Input;

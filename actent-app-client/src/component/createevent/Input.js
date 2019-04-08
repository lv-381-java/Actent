import React from "react";

const Input = props => {
    //console.log(props.value);
    return (
        <div className={'form-group' + (props.isError ? ' has-error' : '')}>
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
            {props.testVal}
        </div>
    );
};

export default Input;

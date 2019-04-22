import React from "react";

const TextArea = props => (
    <div className="form-group">
        <label className="form-label">{props.title}</label>
        <textarea
            className="form-control"
            name={props.name}
            rows={props.rows}
            cols={props.cols}
            value={props.value}
            error={props.error}
            onChange={props.handleChange}
            placeholder={props.placeholder}
        />
        <span>{props.errorMessage}</span>
    </div>
);

export default TextArea;

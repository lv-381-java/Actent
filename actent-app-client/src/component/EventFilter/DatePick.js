import React from 'react';
import DayPicker, { DateUtils } from 'react-day-picker';
import 'react-day-picker/lib/style.css';

export default class DatePicker extends React.Component {
    static defaultProps = {
        numberOfMonths: 2,
    };
    constructor(props) {
        super(props);
        this.handleDayClick = this.handleDayClick.bind(this);
        this.handleResetClick = this.handleResetClick.bind(this);
        this.state = this.getInitialState();
    }
    getInitialState() {
        return {
            from: undefined,
            to: undefined,
        };
    }
    handleDayClick(day) {
        const range = DateUtils.addDayToRange(day, this.state);
        console.log(day);
        this.setState(range, () => this.convertToLong());
        this.props.setButtonColor('success');
    }
    handleResetClick() {
        this.setState(this.getInitialState(), () => this.convertToLong());
        this.props.setButtonColor('info');
    }

    convertToLong = () => {
        if (this.state.from != undefined || this.state.from != null) {
            console.log(this.state.from);
            this.props.setDateRange(this.state.from, null);
            if (this.state.to != undefined || this.state.to != null) {
                this.props.setDateRange(this.state.from, this.state.to);
            }
        } else {
            console.log('null');
            this.props.setButtonColor('info');
            this.props.setDateRange(null, null);
        }
    };

    render() {
        const { from, to } = this.state;
        const modifiers = { start: from, end: to };
        const past = {
            before: new Date(),
        };
        return (
            <div>
                <div className='RangeExample'>
                    <p>
                        {!from && !to && 'Please select the first day.'}
                        {from && !to && `Selected from ${from.toLocaleDateString()}`}
                        {from &&
                            to &&
                            `Selected from ${from.toLocaleDateString()} to
                ${to.toLocaleDateString()}`}{' '}
                        {from && to && (
                            <button className='link' onClick={this.handleResetClick}>
                                Reset
                            </button>
                        )}
                    </p>
                    <DayPicker
                        className='Selectable'
                        numberOfMonths={this.props.numberOfMonths}
                        selectedDays={[from, { from, to }]}
                        modifiers={modifiers}
                        onDayClick={this.handleDayClick}
                        disabledDays={past}
                    />
                    <style>{`
  .Selectable .DayPicker-Day--selected:not(.DayPicker-Day--start):not(.DayPicker-Day--end):not(.DayPicker-Day--outside) {
    background-color: #f0f8ff !important;
    color: #4a90e2;
  }
  .Selectable .DayPicker-Day {
    border-radius: 0 !important;
  }
  .Selectable .DayPicker-Day--start {
    border-top-left-radius: 50% !important;
    border-bottom-left-radius: 50% !important;
  }
  .Selectable .DayPicker-Day--end {
    border-top-right-radius: 50% !important;
    border-bottom-right-radius: 50% !important;
  }
`}</style>
                </div>
            </div>
        );
    }
}

import React from 'react';
import DayPicker, { DateUtils } from 'react-day-picker';
import 'react-day-picker/lib/style.css';

export default class DatePicker extends React.Component {
    static defaultProps = {
        numberOfMonths: 2,
    };
    constructor(props) {
        super(props);
        this.state = {
            from: this.props.dateFrom,
            to: this.props.dateTo,
        };
    }

    handleDayClick = day => {
        const range = DateUtils.addDayToRange(day, this.state);
        this.setState(range, () => this.convertToLong());
        this.props.setButtonColor('success');
    };

    handleResetClick = () => {
        this.setState(
            {
                from: undefined,
                to: undefined,
            },
            () => this.convertToLong(),
        );
    };

    convertToLong = () => {
        if (this.state.from) {
            this.props.setFilterDateFrom(this.state.from);
            if (this.state.to) {
                this.props.setFilterDateTo(this.state.to);
            }
        } else {
            this.props.setButtonColor('info');
            this.props.setFilterDateFrom(undefined);
            this.props.setFilterDateTo(undefined);
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

import React from 'react';
import SelectCity from './SelectCity';
import UserTypeFilter from './UserTypeFilter';
import CategoryFilter from './CategoryFilter';

const style = {
  margin: '30px',
};

export default class MainFilter extends React.Component {
  render() {
    return (
      <div style={style}>
        <div className='row'>
          <div className='col-md-4'>
            <h5>Select city</h5>
            <SelectCity setCity={this.props.setCity} />
          </div>
          <div className='col-md-4'>
            <h5>Select user type</h5>
            <UserTypeFilter setUserType={this.props.setUserType} />
          </div>
          <div className='col-md-4'>
            <h5>Select category</h5>
            <CategoryFilter setCategory={this.props.setCategory} />
          </div>
        </div>
      </div>
    );
  }
}

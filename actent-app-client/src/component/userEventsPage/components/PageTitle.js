import React from 'react';

const style = {
  margin: '30px',
};
export default class PageTitle extends React.Component {
  render() {
    return (
      <div style={style}>
        <h2 className='text-center font-italic'>Events list</h2>
      </div>
    );
  }
}

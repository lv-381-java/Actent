import React from 'react';
import Event from './Event';
import Pagination from "react-js-pagination";
import '../../EventFilter/pagination.css';

export default class PresentEventList extends React.Component {
    render() {
        return (
            <div>
                {this.props.events.map(event => (
                    <Event key={event.eventId.toString()} eventId={event.eventId} title={event.eventTitle}
                           userId={event.userId}
                           description={event.eventDescription}/>
                ))}
                <div className='row ' style={{margin: 'auto', marginTop: '50px'}}>
                    <nav aria-label='Page navigation example'>
                        <Pagination
                            activePage={this.props.activePage}
                            itemsCountPerPage={4}
                            totalItemsCount={this.props.pageCountFuture}
                            pageRangeDisplayed={5}
                            onChange={this.props.handlePageChange}
                            innerClass={'pagination'}
                            itemClass={'page-item'}
                            linkClass={'page-link pagination-border'}
                        />
                    </nav>
                </div>
            </div>
        );
    }
}

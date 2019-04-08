import React from "react";
import Equipment from './Equipment.jsx'
import axios from 'axios';
import '@fortawesome/fontawesome-free/css/all.min.css';
import 'bootstrap-css-only/css/bootstrap.min.css';
import 'mdbreact/dist/css/mdb.css';
import CreateEquipment from './CreateEquipment.jsx'

class EquipmentList extends React.Component {

    state = {
        equipments: [],
    }

    componentDidMount() {
        this.getEquipments();
    };

    getEquipments = () => {

        axios.get(`http://localhost:8080/api/v1/equipments/event/${this.props.eventId}`)
                .then(res => {
                    const equipments = res.data;
                    console.log(res.data);
                    this.setState({
                        equipments: equipments,
                    });
                })
                .catch(function (error) {
                    console.log(error);
                });
    };

    handleUpdateEquipment = (equipment_id, equipment) => {

        console.log("In handleUpdateEquipment equipment_id = ");
        console.log(equipment_id);
        console.log("In handleUpdateEquipment equipment = ");
        console.log(equipment);

        axios.put(`http://localhost:8080/api/v1/equipments/${equipment_id}`, equipment)
                .then(res => {
                    const updatedEquipment = res.data;
                    console.log(updatedEquipment);
                    this.getEquipments();
                })
                .catch(function (error) {
                    console.log(error);
                });
    };

    handleCreateEquipment = (equipment) => {

        console.log("In handleCreateEquipment equipment = ");
        console.log(equipment);

        axios.post(`http://localhost:8080/api/v1/equipments/`, equipment)
                .then(res => {
                    const createdEquipment = res.data;
                    console.log(createdEquipment);
                    this.getEquipments();
                })
                .catch(function (error) {
                    console.log(error);
                });
    };

    handleDeleteEquipment = (equipment_id) => {

        axios.delete(`http://localhost:8080/api/v1/equipments/${equipment_id}`)
                .then(res => {
                    this.getEquipments();
                })
                .catch(function (error) {
                    console.log(error);
                });
    };

    render() {
        return (
                <div className="container-fluid">
                    <CreateEquipment handleCreateEquipment={this.handleCreateEquipment} style={{position: 'fixed'}}
                        eventId={this.props.eventId}
                        creatorId={this.props.creatorId}
                    />
                    <div className="row ">
                        {
                            this.state.equipments.map(item => {
                                        return (
                                                <div key={item.id} className="col-md-12 col-sm-12 ">
                                                    <Equipment
                                                            equipment={item}
                                                            handleUpdateEquipment={this.handleUpdateEquipment}
                                                            handleDeleteEquipment={this.handleDeleteEquipment}
                                                            creatorId={this.props.creatorId}
                                                    />
                                                </div>
                                        )
                                    }
                            )
                        }
                    </div>
                </div>
        );
    }
}

export default EquipmentList;

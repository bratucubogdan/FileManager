import React from 'react';
import {Card, Form, Button} from "react-bootstrap";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faPlusSquare, faSave, faUndo} from '@fortawesome/free-solid-svg-icons'
import MyToast   from "./MyToast";
import axiosInstance from "./Axios";
import 'react-datepicker/dist/react-datepicker.css'
import {DatePicker} from "@syncfusion/ej2-react-calendars";


export default class AddFile extends React.Component {
    constructor(props) {
        super(props);
        this.state = this.initialState
        this.state.show = false;
        this.fileChange = this.fileChange.bind(this);
        this.submitFile = this.submitFile.bind(this);
    }

    initialState = {
        mainFieldOfInterest: '',
        secondaryFieldOfInterest: '',
        registrationNumber: '',
        numberDate: '',
        fNumber:'',
        fName:'',
        fDate:'',
        fValue:'',
        fileUpload: ''
    }

    resetFile = () => {
        this.setState(() => this.initialState)
    }

    submitFile = event => {
        event.preventDefault();

        const formData = new FormData();
        formData.append('mainFieldOfInterest', this.state.mainFieldOfInterest);
        formData.append('secondaryFieldOfInterest', this.state.secondaryFieldOfInterest);
        formData.append('registrationNumber', this.state.registrationNumber);
        formData.append('numberDate', this.state.numberDate);
        formData.append('fileUpload', this.state.fileUpload);
        formData.append('fNumber', this.state.fNumber);
        formData.append('fName', this.state.fName);
        formData.append('fDate', this.state.fDate);
        formData.append('fValue', this.state.fValue);


        axiosInstance.post("/upload", formData)
            .then(response => {
                console.log(response);
                if (response.data != null) {
                    this.setState({"show" : true});
                    setTimeout(() => this.setState({"show" : false}), 3000);
                }else{
                    this.setState({"show" : false});
                }
            })
        this.setState(this.initialState);
    }
    handleFileChange = (event) => {
        const file = event.target.files[0];
        this.setState({
            fileUpload: file
        });
    }

    fileChange = event => {
       this.setState({
           [event.target.name] : event.target.value
       })
    }

    render() {
        const {mainFieldOfInterest, secondaryFieldOfInterest, registrationNumber, numberDate, data, fNumber, fName, fDate, fValue } = this.state


        return (
            <div>
                <div style={{"display":this.state.show ? "block" : "none"}}>
                    <MyToast children =  {{show:this.state.show, message : "File saved successfully.", type:"success"}}/>
                </div>
                <Card className="border border-dark bg-dark text-white">
                    <Card.Header><FontAwesomeIcon icon={faPlusSquare}/> Add File</Card.Header>
                    <Form onReset= {this.resetFile} onSubmit={this.submitFile} id="fileFormId"  encType="multipart/form-data">
                        <Card.Body>
                            <Form.Label>Provenienta</Form.Label>
                            <Form.Group className="mb-3" controlId="formGridMainFieldOfInterest">
                                <Form.Select
                                    autoComplete={"off"}
                                    aria-label="Provenienta"
                                    name="mainFieldOfInterest"
                                    value={mainFieldOfInterest}
                                    onChange={this.fileChange}>
                                    <option>Provenienta</option>
                                    <option value="CASA">CASA</option>
                                    <option value="CJI">CJI</option>
                                    <option value="DSP">DSP</option>
                                    <option value="MS">MS</option>
                                    <option value="REGISTRATURA">REGISTRATURA</option>

                                </Form.Select>
                            </Form.Group>

                            <Form.Label>Tipul Documentului</Form.Label>
                            <Form.Group className="mb-3" controlId="formGridSecondaryFieldOfInterest">
                                <Form.Select
                                    autoComplete={"off"}
                                    aria-label="Default select example"
                                    name="secondaryFieldOfInterest"
                                    value={secondaryFieldOfInterest}
                                    onChange={this.fileChange}>
                                    <option>Tipul Documentului</option>
                                    <option value="FACTURA">FACTURA</option>
                                    <option value="CONTRACT">CONTRACT</option>
                                    <option value="NOTIFICARE">NOTIFICARE</option>
                                    <option value="CITATIE">CITATIE</option>
                                </Form.Select>
                            </Form.Group>

                            <Form.Label>Numar de inregistrare REGISTRATURA</Form.Label>
                            <Form.Group className="mb-3" controlId="formGridRegistrationNumber">
                                <Form.Control
                                    autoComplete={"off"}
                                    type="registrationNumber"
                                    placeholder="Numar de inregistrare"
                                    name="registrationNumber"
                                    value={registrationNumber}
                                    onChange={this.fileChange}/>
                            </Form.Group>

                            <Form.Label>Data numar de inregistrare REGISTRATURA</Form.Label>
                            <Form.Group className="mb-3" controlId="formGridNumberDate">
                                <Form.Control
                                    autoComplete={"off"}
                                    type="date"
                                    name="numberDate"
                                    value={numberDate}
                                    onChange={this.fileChange}/>
                            </Form.Group>
                            {
                                secondaryFieldOfInterest === 'FACTURA' && (
                                    <>
                                        <Form.Label>Numar Factura</Form.Label>
                                        <Form.Group className="mb-3" controlId="formGridfNumber">
                                <Form.Control
                                    autoComplete={"off"}
                                    type="fNumber"
                                    placeholder="Numar Factura"
                                    name="fNumber"
                                    value={fNumber}
                                    onChange={this.fileChange}/>

                            </Form.Group>

                                        <Form.Label>Data Factura</Form.Label>
                            <Form.Group className="mb-3" controlId="formGridfDate">
                                <Form.Control
                                    autoComplete={"off"}
                                    type="date"
                                    placeholder="Data Factura"
                                    name="fDate"
                                    value={fDate}
                                    onChange={this.fileChange}/>

                            </Form.Group>

                                        <Form.Label>Furnizor</Form.Label>
                            <Form.Group className="mb-3" controlId="formGridfName">
                                <Form.Control
                                    autoComplete={"off"}
                                    type="fName"
                                    placeholder="Furnizor"
                                    name="fName"
                                    value={fName}
                                    onChange={this.fileChange}/>

                            </Form.Group>
                                        <Form.Label>Total Factura cu TVA</Form.Label>
                            <Form.Group className="mb-3" controlId="formGridfValue">
                                <Form.Control
                                    autoComplete={"off"}
                                    type="fValue"
                                    placeholder="Total Factura"
                                    name="fValue"
                                    value={fValue}
                                    onChange={this.fileChange}/>
                            </Form.Group>
                                    </>
                    )
                    }
                            <Form.Label>Incarcare Fisier</Form.Label>
                            <Form.Group controlId="formGridFile" className="mb-3">
                                <Form.Control
                                    autoComplete={"off"}
                                    type="file"
                                    name="fileUpload"
                                    value={data}
                                    onChange={this.handleFileChange}/>
                            </Form.Group>
                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button  size = "sm" variant="success" type="submit">
                                <FontAwesomeIcon icon={faSave} /> Submit
                            </Button>{' '}
                            <Button  size = "sm" variant="info" type="reset">
                                <FontAwesomeIcon icon={faUndo} /> Reset
                            </Button>
                        </Card.Footer>
                    </Form>
                </Card>
            </div>


        )
    }
}

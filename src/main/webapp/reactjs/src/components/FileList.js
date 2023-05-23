import React from 'react';
import {Button, ButtonGroup, Card, Col, Form, Row, Table} from "react-bootstrap";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faDownload, faEdit, faList, faRedo, faSearch, faTrash} from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import MyToast from "./MyToast";
import axiosInstance from "./Axios";
import { format } from 'date-fns'
class FileList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            files: [],
            mainSearch: '',
            secondarySearch: '',
            numberSearch: '',
            dateSearch: ''
        }
        this.searchChange = this.searchChange.bind(this);
        this.resetSearch = this.resetSearch.bind(this);
    }

    componentDidMount() {
        axiosInstance.get("/allFiles")
            .then(response => response.data)
            .then((data) => {
                this.setState({files: data});
            });
    }

    submitSearch = event => {
        event.preventDefault();
        const {mainSearch, secondarySearch, numberSearch, dateSearch} = this.state;
        const params = {
            mainSearch: mainSearch,
            secondarySearch: secondarySearch,
            numberSearch: numberSearch,
            dateSearch: dateSearch
        };
        axiosInstance.get("/allFiles", {params})
            .then(response => response.data)
            .then((data) => {
                this.setState({files: data});
            });
    }

    resetSearch = () => {
        axiosInstance.get("/allFiles")
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    files: data,
                    mainSearch: '',
                    secondarySearch: '',
                    numberSearch: '',
                    dateSearch: '',
                });
            });
    }

    searchChange = event => {
        this.setState({
            [event.target.name]: event.target.value
        })
    }
    deleteFile = (fileId) => {
        axiosInstance.delete("/junk/" + fileId)
            .then(response => {
                if (response.data !== null) {
                    this.setState({"show": true});
                    setTimeout(() => this.setState({"show": false}), 3000);
                    this.setState({
                        files: this.state.files.filter(file => file.id !== fileId)
                    })

                }
            })
    }

    downloadFile = (file) => {
        axiosInstance({
            url: "/download/" + file.id,
            method: 'GET',
            responseType: 'blob'
        }).then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', file.name);
            document.body.appendChild(link);
            link.click();
        });

    }

    render() {
        const {mainSearch, secondarySearch, numberSearch, dateSearch} = this.state;


        return (
            <div>

                {/*Search*/}
                <div>

                    <Card className={"border border-dark bg-dark text-white"} style={{width: 1410}}>
                        <Card.Header><FontAwesomeIcon icon={faSearch}/> Search </Card.Header>
                        <Form onSubmit={this.submitSearch} id="fileSearchId">
                            <Card.Body>
                                <Row className={"justify-content-end"}>
                                    <Col>
                                        <Form.Group className="mb-3" controlId="formGridMainFieldOfInterest"
                                                    style={{"width": "240px"}}>
                                            <Form.Select
                                                autoComplete={"off"}
                                                aria-label="Default select example"
                                                name="mainSearch"
                                                value={mainSearch}
                                                onChange={this.searchChange}>
                                                <option value={""}>Institutia de Provenienta</option>
                                                <option value="CASA">CASA</option>
                                                <option value="CJI">CJI</option>
                                                <option value="DSP">DSP</option>
                                                <option value="MS">MS</option>
                                            </Form.Select>
                                        </Form.Group>
                                    </Col>
                                    <Col>
                                        <Form.Group className="mb-3" controlId="formGridSecondaryFieldOfInterest"
                                                    style={{"width": "240px"}}>
                                            <Form.Select
                                                autoComplete={"off"}
                                                aria-label="Default select example"
                                                name="secondarySearch"
                                                value={secondarySearch}
                                                onChange={this.searchChange}>
                                                <option value={""}>Tipul Documentului</option>
                                                <option value="FACTURA">FACTURA</option>
                                                <option value="CONTRACT">CONTRACT</option>
                                                <option value="NOTIFICARE">NOTIFICARE</option>
                                                <option value="CITATIE">CITATIE</option>
                                            </Form.Select>
                                        </Form.Group>
                                    </Col>
                                    <Col>
                                        <Form.Group className="mb-3" controlId="formGridRegistrationNumber"
                                                    style={{"width": "230px"}}>
                                            <Form.Control
                                                autoComplete={"off"}
                                                type="registrationNumber"
                                                placeholder="Numar de inregistrare"
                                                name="numberSearch"
                                                value={numberSearch}
                                                onChange={this.searchChange}/>
                                        </Form.Group>
                                    </Col>
                                    <Col>
                                        <Form.Group className="mb-3" controlId="formGridNumberDate"
                                                    style={{"width": "230px"}}>
                                            <Form.Control
                                                autoComplete={"off"}
                                                type="date"
                                                name="dateSearch"
                                                value={dateSearch}
                                                placeholder={"Data inceput"}
                                                onChange={this.searchChange}/>
                                        </Form.Group>
                                    </Col>
                                    <Col>
                                        <Button size="bg"
                                                variant="info"
                                                type="submit">
                                            <FontAwesomeIcon icon={faSearch}/> Submit
                                        </Button>
                                    </Col>
                                    <Col>
                                        <Button size="bg"
                                                variant="warning"
                                                type="button"
                                                onClick={this.resetSearch}>
                                            <FontAwesomeIcon icon={faRedo}/> Reset
                                        </Button>
                                    </Col>
                                </Row>
                            </Card.Body>
                        </Form>
                    </Card>
                </div>
                {/*Toast Pop Up Delete*/}
                <div style={{"display": this.state.show ? "block" : "none"}}>
                    <MyToast children={{show: this.state.show, message: "File deleted successfully.", type: "danger"}}/>
                </div>
                {/* File List Table */}
                <div>
                <Card className="border border-dark bg-dark text-white" style={{width: 1410}}>
                    <Card.Header><FontAwesomeIcon icon={faList}/> File List </Card.Header>
                    <Card.Body>
                        <Table bordered hover striped variant={"dark"}>
                            <thead>
                            <tr>
                                <th className=" text-center">Numar Registrul Unic pentru Facturi - Contabile</th>
                                <th className=" text-center">Nume</th>
                                <th className=" text-center">Provenienta</th>
                                <th className=" text-center">Tipul documentului</th>
                                <th className=" text-center">Numar de inregistrare REGISTRATURA</th>
                                <th className=" text-center">Data  REGISTRATURA</th>
                                <th className=" text-center">Data  Registrul Unic pentru Facturi - Contabile</th>
                                <th className=" text-center">Numar Factura</th>
                                <th className=" text-center">Data Emiterii Facturii</th>
                                <th className=" text-center">Furnziorul</th>
                                <th className=" text-center">Valoare totala factura cu TVA</th>
                                <th className=" text-center">Comenzi</th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.state.files.length === 0 ?
                                <tr align="center">
                                    <td colSpan="12">{this.state.files.length} Files Available.</td>
                                </tr> :
                                this.state.files.map((file) => (
                                    <tr key={file.id}>
                                        <td>{file.id}</td>
                                        <td>{file.name}</td>
                                        <td>{file.mainFieldOfInterest}</td>
                                        <td>{file.secondaryFieldOfInterest}</td>
                                        <td>{file.registrationNumber}</td>
                                        <td>{format(new Date(file.numberDate), 'dd/MM/yyyy')}</td>
                                        <td>{file.date}</td>
                                        <td>{file.fNumber !== null ? file.fNumber : 'N/A'}</td>
                                        <td>{file.fDate !== null ? format(new Date(file.fDate), 'dd/MM/yyyy') : 'N/A'}</td>
                                        <td>{file.fName !== null ? file.fName : 'N/A'}</td>
                                        <td>{file.fValue !== null ? file.fValue.toLocaleString() : 'N/A'}</td>
                                        <td>
                                            <ButtonGroup>
                                                <Button size={"sm"} variant={"outline-primary"}
                                                        onClick={this.downloadFile.bind(this, file)}><FontAwesomeIcon
                                                    icon={faDownload}/></Button>
                                                <Button size={"sm"} variant={"outline-danger"}
                                                        onClick={this.deleteFile.bind(this, file.id)}><FontAwesomeIcon
                                                    icon={faTrash}/></Button>
                                            </ButtonGroup>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    </Card.Body>
                </Card>
                </div>
            </div>

        )
    }
}

export default FileList;

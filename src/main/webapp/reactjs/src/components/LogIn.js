import React from "react";
import {Alert, Button, Card, Col, Form, FormControl, InputGroup, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome"
import {} from "@fortawesome/fontawesome-svg-core"
import {faIdCard, faLockOpen, faSignInAlt, faUndo} from "@fortawesome/free-solid-svg-icons";
import axios from "axios";



 export  default class LogIn extends React.Component {
    constructor(props) {
        super(props);
        this.state = this.initialState
    }

    initialState = {
        email: '',
        password: '',
        error: ''
    }


credentialChange = event => {
        this.setState({
            [event.target.name]: event.target.value
        })
    }



    validateUser = event => {
        event.preventDefault();

        const formData = new FormData();
        formData.append('email', this.state.email);
        formData.append('password', this.state.password);

        axios.post("http://localhost:8080/api/v1/auth/authenticate", formData)
            .then(response => {
                const token = response.data;
                localStorage.setItem('token', token);
                this.setState({ token });
                if(localStorage.length !== null){
                    window.location.href = 'http://localhost:3000/';
                }

            });
    }


    resetLogInForm = () => {
        this.setState(() => this.initialState)
    }

    render() {
        const {email, password, error} = this.state;

        return (
            <Row className="justify-content-md-center">
                <Col xs={5}>
                    {this.state.error && <Alert variant = "danger"></Alert>}
                    <Card className={"border border-dark bg-dark text-white"}>
                        <Card.Header>
                            <FontAwesomeIcon icon={faSignInAlt}/> Log In
                        </Card.Header>
                        <Card.Body>
                            <Form>
                                <Form.Group>
                                    <InputGroup>
                                        <InputGroup.Text><FontAwesomeIcon icon={faIdCard}/></InputGroup.Text>
                                    <FormControl required
                                                 autoComplete={"off"}
                                                 type={"text"}
                                                 name={"email"}
                                                 value={email}
                                                 onChange={this.credentialChange}
                                                 className={"bg-dark text-white"}
                                                 placeholder={"Enter Username"}/>
                                    </InputGroup>
                               </Form.Group>

                                <Form.Group>
                                    <InputGroup style={{ "marginTop": "10px" }}>
                                        <InputGroup.Text><FontAwesomeIcon icon={faLockOpen}/></InputGroup.Text>
                                    <FormControl required
                                                 autoComplete={"off"}
                                                 type={"password"}
                                                 name={"password"}
                                                 value={password}
                                                 onChange={this.credentialChange}
                                                 className={"bg-dark text-white"}
                                                 placeholder={"Enter Password"}/>
                                    </InputGroup>
                                </Form.Group>
                            </Form>
                        </Card.Body>
                        <Card.Footer style={{"textAlign": "right"}}>
                            <Button size={"sm"} type={"submit"} variant={"success"} onClick={this.validateUser}
                                    disabled={this.state.email.length === 0 || this.state.password.length === 0}>
                                <FontAwesomeIcon icon={faSignInAlt}/> Log In
                            </Button>{' '}
                            <Button size={"sm"} type={"button"} variant={"info"} onClick={this.resetLogInForm}
                                    disabled={this.state.email.length === 0 && this.state.password.length === 0}>
                                <FontAwesomeIcon icon={faUndo}/> Reset
                            </Button>

                        </Card.Footer>
                    </Card>
                </Col>
            </Row>

        )
    }

}

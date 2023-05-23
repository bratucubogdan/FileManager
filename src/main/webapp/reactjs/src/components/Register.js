import React from "react";
import {Button, Card, Col, Form, FormControl, InputGroup, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome"
import {} from "@fortawesome/fontawesome-svg-core"
import {
    faEnvelope,
    faIdCard,
    faLock,
    faLockOpen,
    faSignInAlt,
    faUndo,
    faUserPlus
} from "@fortawesome/free-solid-svg-icons";
import FormRange from "react-bootstrap/FormRange";
import axios from "axios";

export default class LogIn extends React.Component {
    constructor(props) {
        super(props);
        this.state = this.initialState
    }

    initialState = {
        email: '',
        password: ''
    }
    registerUser = event =>{
        event.preventDefault();

        const formData = new FormData();
        formData.append('email', this.state.email);
        formData.append('password', this.state.password);

        axios.post("http://10.100.0.114:8080/api/v1/auth/register", formData)
            .then(response =>{
                console.log(response.data)
                window.location.href= 'http://10.100.0.114:3000/login'
            })
    }
    credentialChange = event => {
        this.setState({
            [event.target.name]: event.target.value
        })
    }
    resetRegisterForm = () => {
        this.setState(() => this.initialState)
    }

    render() {
        const {email, password} = this.state;

        return (
            <Row className="justify-content-md-center">
                <Col xs={5}>
                    <Card className={"border border-dark bg-dark text-white"}>
                        <Card.Header>
                            <FontAwesomeIcon icon={faUserPlus}/> Register
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
                            <Button size={"sm"} type={"button"} variant={"success"}
                                    onClick={this.registerUser}
                                    disabled={this.state.email.length === 0 || this.state.password.length === 0}>
                                <FontAwesomeIcon icon={faUserPlus}/> Register
                            </Button>{' '}
                            <Button size={"sm"} type={"button"} variant={"info"} onClick={this.resetRegisterForm}
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
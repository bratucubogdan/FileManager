import React from "react";
import {Navbar, Nav} from "react-bootstrap";
import {Link} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faSignIn, faSignOut, faUserPlus} from "@fortawesome/free-solid-svg-icons";

class NavigationBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn: false // default state for isLoggedIn
        };
    }

    componentDidMount() {
        const token = localStorage.getItem('token');
        if (token) {
            this.setState({ isLoggedIn: true }); // update state if token is present
        }
    }

    handleLogout = () => {
        localStorage.removeItem('token'); // remove token from local storage
        this.setState({ isLoggedIn: false }); // update state
        window.location.href = 'http://10.100.0.114:3000/';
    };
    render() {
        return (
            <Navbar bg="dark" variant="dark">
                <Link to={""} className="navbar-brand">
                    <img
                    src="https://upload.wikimedia.org/wikipedia/commons/7/74/Folder-front-gradient.png"
                    width="25"
                    height="25"
                    alt="brand"/>
                    FileManager
                </Link>
                <Nav className="me-auto">
                    <Link to={"addFile"} className="nav-link">Add File</Link>
                    <Link to={"allFiles"} className="nav-link">File List</Link>
                </Nav>

                <Nav className={"navbar-right"}>
                    {this.state.isLoggedIn ? (
                    <Link to="/logout" className="nav-link" onClick={this.handleLogout}>
                        <FontAwesomeIcon icon={faSignOut} />
                        Logout
                    </Link>
                    ) : (
                    <>
                        <Link to="/login" className="nav-link">
                            <FontAwesomeIcon icon={faSignIn} />
                            Login
                        </Link>
                    <Link to={"register"} className={"nav-link"}> <FontAwesomeIcon icon={faUserPlus} /> Register</Link>
                    </>
                    )}
                </Nav>
            </Navbar>
        )
    }
}

export default NavigationBar;
import React from 'react';
import './App.css';
import NavigationBar from "./components/NavigationBar"
import {Col, Container, Row} from "react-bootstrap";
import Welcome from "./components/Welcome";
import Footer from "./components/Footer"
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import AddFile from "./components/AddFile";
import FileList from "./components/FileList";
import Register from "./components/Register"
import LogIn from "./components/LogIn"
function App() {

    const marginTop = {
        marginTop: "20px"
    };
    return (
        <Router>
            <NavigationBar/>
            <Container>
                <Row>
                    <Col lg={12} style={marginTop}>
                    <Routes>
                        <Route path= "/" element={<Welcome/>}/>
                        <Route path= "/addFile" element={<AddFile/>}/>
                        <Route path="/allFiles" element={<FileList/>}/>
                        <Route path={"/register"} element={<Register/>}/>
                        <Route path={"/login"} element={<LogIn/>}/>
                    </Routes>
                    </Col>
                </Row>
            </Container>
            <Footer/>
        </Router>
    );
}

export default App;

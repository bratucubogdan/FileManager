import React from 'react';
import {Col, Container, Row} from "react-bootstrap";

class Welcome extends React.Component{

    render() {
        return(
            <Container>
                <Row>
                    <Col lg={12}>
                        <div className="mt-4 p-5 bg-dark text-white rounded">
                            <h1 className="display-4">Welcome to FileManager</h1>
                        </div>
                    </Col>
                </Row>
            </Container>
        );
    }
}

export default Welcome;
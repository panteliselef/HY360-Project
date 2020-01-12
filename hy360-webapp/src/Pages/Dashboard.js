import React, { Component } from 'react';
import { Button } from 'antd';
import { List, Typography } from 'antd';
import TestLayout from '../Components/TestLayout';
import TestLayout2 from '../Components/TestLayout2';
import '../App.css';
const data = [
	'Racing car sprays burning fuel into crowd.',
	'Japanese princess to wed commoner.',
	'Australian walks 100km after outback crash.',
	'Man charged over missing wedding girl.',
	'Los Angeles battles huge wildfires.'
];
class Dashboard extends Component {
	render() {
		return (
			<div className="App">
				{/* <Button type="primary">Button</Button> */}
				<TestLayout2 routeLocation={this.props.location}></TestLayout2>
			</div>
		);
	}
}

export default Dashboard;

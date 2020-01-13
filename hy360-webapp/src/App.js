import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Dashboard from './Pages/Dashboard';
import './App.css';
function App(props) {
	console.log(props);
	return (
		<>
			<Router>
				<Route path="/" component={Dashboard}></Route>
			</Router>
		</>
	);
}

export default App;

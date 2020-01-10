import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Dashboard from './Pages/Dashboard';
import './App.css';
function App() {
	return (
		<>
			<Router>
				<Route path="/" component={Dashboard} />
			</Router>
		</>
	);
}

export default App;

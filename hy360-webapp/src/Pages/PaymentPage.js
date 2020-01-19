import React, { useEffect, useState } from 'react';
import { Form, Input, Icon, Typography, Row, Col, Button, Radio, Table } from 'antd';
import ajaxRequest from '../utils/ajax';

const { Title } = Typography;
function PaymentPage() {
	const [ employees, setEmployees ] = useState([]);
	const [ selectedEmployee, setSelectedEmployee ] = useState({});
	const [ response, setResponse ] = useState('');

	const handlePay = () => {
		ajaxRequest('POST', `http://localhost:8085/hy360/payments`, null, ({ statusCode }) => {
			statusCode === 200 ? setResponse('Success') : setResponse('Failed');
		});
	};

	useEffect(
		() => {
				setTimeout(function() {
          setResponse('');
          console.log("HEY")
				}, 5000);

			// function myStopFunction() {
			// 	clearTimeout(myVar);
			// }
		},
		[ response ]
	);

	return (
		<div>
			<Title level={2}>Would you like to submit a payment for this month ?</Title>

			<Title level={4}>{response}</Title>
			<Button onClick={handlePay} type="primary">
				Pay Salaries
			</Button>
		</div>
	);
}

export default PaymentPage;

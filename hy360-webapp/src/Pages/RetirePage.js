import React, { useEffect, useState } from 'react';
import { Form, Input, Icon, Typography, Row, Col, Button, Radio, Table } from 'antd';
import ajaxRequest from '../utils/ajax';

const { Title } = Typography;
function RetirePage() {
	const [ employees, setEmployees ] = useState([]);
	const [ selectedEmployee, setSelectedEmployee ] = useState({});
	const [ response, setResponse ] = useState('');

	const columns = [
		{
			title: 'First Name',
			dataIndex: 'fname'
			// render: (text) => <a>{text}</a>
		},
		{
			title: 'Last Name',
			dataIndex: 'lname'
		}
	];

	useEffect(() => {
		ajaxRequest('GET', `http://localhost:8085/hy360/employee`, null, ({ result }) => {
			const data = result.map((element) => {
				return {
					key: element.id,
					fname: element.fname,
					lname: element.lname
				};
			});
			setEmployees(data);
		});
	}, []);

	const rowSelection = {
		onChange: (selectedRowKeys, selectedRows) => {
			console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);

			const d = employees.filter((employee) => employee.key === selectedRowKeys[0]);
			console.log(d[0]);
			setSelectedEmployee(d[0]);
		},
		getCheckboxProps: (record) => ({
			disabled: record.name === 'Disabled User', // Column configuration not to be checked
			name: record.name
		}),
		type: 'radio'
	};

	const handleRetirement = () => {
		ajaxRequest('DELETE', `http://localhost:8085/hy360/employee?empId=${selectedEmployee.key}`, null, ({ statusCode }) => {
			statusCode === 200 ? setResponse('Success') : setResponse('Failed');
		});
	};

	return (
		<div>
			<Title level={2}>Select an employee to retire/fire</Title>
			<Table rowSelection={rowSelection} columns={columns} dataSource={employees} />
			<Title level={4}>{response}</Title>
			<Button
				disabled={Object.keys(selectedEmployee).length <= 0 ? true : false}
				onClick={handleRetirement}
				type="primary"
			>
				Perform Retirement
			</Button>
		</div>
	);
}

export default RetirePage;

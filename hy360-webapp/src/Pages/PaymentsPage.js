import React, { useEffect, useState } from 'react';
import { Form, Input, Icon, Typography, Row, Col, Button, Radio, Table, Select } from 'antd';
import ajaxRequest from '../utils/ajax';

const { Title, Text } = Typography;
const { Option } = Select;
function PaymentsPage() {
	const [ category, setCategory ] = useState('perm_admin');

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

	// useEffect(() => {
	// 	ajaxRequest('GET', `http://localhost:8085/hy360/employee?categories=perm`, null, ({ result }) => {
	// 		const data = result.map((element) => {
	// 			return {
	// 				key: element.id,
	// 				fname: element.fname,
	// 				lname: element.lname
	// 			};
	// 		});
	// 		setEmployees(data);
	// 	});
	// }, []);

	// const rowSelection = {
	// 	onChange: (selectedRowKeys, selectedRows) => {
	// 		console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);

	// 		const d = employees.filter((employee) => employee.key === selectedRowKeys[0]);
	// 		console.log(d[0]);
	// 		setSelectedEmployee(d[0]);
	// 	},
	// 	getCheckboxProps: (record) => ({
	// 		disabled: record.name === 'Disabled User', // Column configuration not to be checked
	// 		name: record.name
	// 	}),
	// 	type: 'radio'
	// };

	// const handleRetirement = () => {
	// 	ajaxRequest(
	// 		'DELETE',
	// 		`http://localhost:8085/hy360/employee?empId=${selectedEmployee.key}`,
	// 		null,
	// 		({ statusCode }) => {
	// 			statusCode === 200 ? setResponse('Success') : setResponse('Failed');
	// 		}
	// 	);
	// };

	const handleCategoryChange = (e) => {
		setCategory(e.target.value);
	};

	return (
		<div>
			<Title level={2}>Select a category</Title>
			<Text>View payment info</Text>

			<Select defaultValue={category} style={{ width: 300, display: 'block' }} onChange={handleCategoryChange}>
				<Option value="perm_admin">Permanent Admin</Option>
				<Option value="temp_admin">Temporary Admin</Option>
				<Option value="perm_teach">Permanent Teach</Option>
				<Option value="temp_teach">Temporary Teach</Option>
			</Select>

			<Table rowSelection={{}} columns={columns} />
		</div>
	);
}

export default PaymentsPage;

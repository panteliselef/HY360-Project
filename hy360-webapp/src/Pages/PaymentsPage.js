import React, { useEffect, useState } from 'react';
import { Form, Input, Icon, Typography, Row, Col, Button, Radio, Table, Select, DatePicker } from 'antd';
import ajaxRequest from '../utils/ajax';

const { Title, Text } = Typography;
const { Option } = Select;
const { RangePicker } = DatePicker;
function PaymentsPage() {
	const [ category, setCategory ] = useState('perm_admin');
	const [ dateFrom, setDateFrom ] = useState('');
	const [ dateUntil, setDateUntil ] = useState('');

	const [ payments, setPayments ] = useState([]);

	const columns = [
		{
			title: 'Name',
			dataIndex: 'emp_names'
			// render: (text) => <a>{text}</a>
		},
		{
			title: 'Amount',
			dataIndex: 'salary'
		},
		{
			title: 'Reseach Bonus',
			dataIndex: 'research'
		},
		{
			title: 'Annual Bonus',
			dataIndex: 'annual'
		},
		{
			title: 'Library Bonus',
			dataIndex: 'library'
		},
		{
			title: 'Family Bonus',
			dataIndex: 'family'
		}
	];

	useEffect(
		() => {
			if (category === '' || dateFrom === '' || dateUntil === '') return;
			ajaxRequest(
				'GET',
				`http://localhost:8085/hy360/payments?category=${category}&from=${dateFrom}&until=${dateUntil}`,
				null,
				({ statusCode, result }) => {
					if (statusCode !== 200) return;

					const data = result.map((element, i) => {
						return {
							...element,
							key: i
						};
					});
					// setEmployees(data);
					setPayments(data);
				}
			);
		},
		[ category, dateFrom, dateUntil ]
	);

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

	const handleCategoryChange = (value) => {
		setCategory(value);
	};

	const onChange = (date, dateString) => {
    console.log(date, dateString);
    if(dateString[0]===''||dateString[1]==='')return;
		setDateFrom(new Date(dateString[0]).getTime());
		setDateUntil(new Date(dateString[1]).getTime());
		console.log(new Date(dateString[1]).getTime());
	};

	return (
		<div>
			<Title level={2}>Select a category</Title>
			<Text>View payment info</Text>

			<br />
			<Select defaultValue={category} style={{ width: 300, display: 'block' }} onChange={handleCategoryChange}>
				<Option value="perm_admin">Permanent Admin</Option>
				<Option value="temp_admin">Temporary Admin</Option>
				<Option value="perm_teach">Permanent Teach</Option>
				<Option value="temp_teach">Temporary Teach</Option>
			</Select>
			<br />
			<RangePicker onChange={onChange} />
			<br />
			<br />

			<Table columns={columns} dataSource={payments} />
		</div>
	);
}

export default PaymentsPage;

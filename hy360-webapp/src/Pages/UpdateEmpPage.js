import React, { useEffect, useState } from 'react';
import {
	Form,
	Input,
	Tooltip,
	Icon,
	Typography,
	Cascader,
	Select,
	Row,
	Col,
	Checkbox,
	Button,
	AutoComplete,
	Steps,
	Radio,
	message,
	InputNumber,
	DatePicker,
	Table
} from 'antd';

import '../Style/register-page.css';

import { Row as FlexRow, Col as FlexCol } from 'react-flexbox-grid';

import DepartmentSelect from '../Components/DepartmentSelect';
import ajaxRequest from '../utils/ajax';
const { Step } = Steps;
const { Title, Text } = Typography;
const { RangePicker } = DatePicker;
const { Option } = Select;

function UpdateEmpPage(props) {
	const { getFieldDecorator, validateFields } = props.form;
	const [ employees, setEmployees ] = useState([]);
	const [ allDataEmployees, setAllDataEmployees ] = useState([]);

	const [ selectedEmployee, setSelectedEmployee ] = useState({});
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
	const data = [
		{
			key: '1',
			name: 'John Brown',
			age: 32,
			address: 'New York No. 1 Lake Park'
		},
		{
			key: '2',
			name: 'Jim Green',
			age: 42,
			address: 'London No. 1 Lake Park'
		},
		{
			key: '3',
			name: 'Joe Black',
			age: 32,
			address: 'Sidney No. 1 Lake Park'
		},
		{
			key: '4',
			name: 'Disabled User',
			age: 99,
			address: 'Sidney No. 1 Lake Park'
		}
	];

	const rowSelection = {
		onChange: (selectedRowKeys, selectedRows) => {
			console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);

			const d = allDataEmployees.filter((employee) => employee.id === selectedRowKeys[0]);
			setSelectedEmployee(d[0]);
		},
		getCheckboxProps: (record) => ({
			disabled: record.name === 'Disabled User', // Column configuration not to be checked
			name: record.name
		}),
		type: 'radio'
	};

	useEffect(() => {
		ajaxRequest('GET', `http://localhost:8085/hy360/employee`, null, ({ result }) => {
			setAllDataEmployees(result);
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

	const formItemLayout = {
		labelCol: {
			xs: { span: 24 },
			sm: { span: 8 }
		},
		wrapperCol: {
			xs: { span: 24 },
			sm: { span: 16 }
		}
	};

	const handleIsMarried = (e) => {
		setSelectedEmployee({
			...selectedEmployee,
			isMarried: e.target.value
		});
	};

	const handleUpdate = (e) => {
		e.preventDefault();

		validateFields((error, values) => {
			if (error) return;
			const updatedData = {
				...selectedEmployee,
				...values,
				depId: values.department.depId
			};
			setSelectedEmployee(updatedData);

			ajaxRequest('PUT', `http://localhost:8085/hy360/employee`, JSON.stringify(updatedData), (res) => {
				console.log(res);
			});
		});
	};
	return (
		<React.Fragment>
			<Title>Select a employee</Title>
			<Table rowSelection={rowSelection} columns={columns} dataSource={employees} />

			{Object.keys(selectedEmployee).length > 0 ? (
				<Form {...formItemLayout} onSubmit={handleUpdate}>
					<Form.Item label="First Name" hasFeedback>
						{getFieldDecorator('fname', {
							initialValue: selectedEmployee.fname,
							rules: [
								{
									required: true,
									message: 'Please input your First Name'
								}
							]
						})(<Input />)}
					</Form.Item>
					<Form.Item label="Last Name" hasFeedback>
						{getFieldDecorator('lname', {
							initialValue: selectedEmployee.lname,
							rules: [
								{
									required: true,
									message: 'Please input your Last Name'
								}
							]
						})(<Input />)}
					</Form.Item>
					<Form.Item label="Address" hasFeedback>
						{getFieldDecorator('address', {
							initialValue: selectedEmployee.address,
							rules: [
								{
									required: true,
									message: 'Please input your Address'
								}
							]
						})(<Input />)}
					</Form.Item>
					<Form.Item label="Phone Number" hasFeedback>
						{getFieldDecorator('phone', {
							initialValue: selectedEmployee.phone,
							rules: [ { required: true, message: 'Please input your phone number!' } ]
						})(<Input style={{ width: '100%' }} />)}
					</Form.Item>
					<Form.Item label="IBAN" hasFeedback>
						{getFieldDecorator('iban', {
							initialValue: selectedEmployee.iban,
							rules: [
								{
									required: true,
									message: 'Please input your IBAN'
								}
							]
						})(<Input />)}
					</Form.Item>
					<Form.Item label="Bank Name" hasFeedback>
						{getFieldDecorator('bankName', {
							initialValue: selectedEmployee.bankName,
							rules: [
								{
									required: true,
									message: 'Please input your Bank '
								}
							]
						})(<Input />)}
					</Form.Item>
					<Form.Item label="Univ. Department" hasFeedback>
						{getFieldDecorator('department', {
							initialValue: selectedEmployee.department,
							initialValue: { depId: '5' },
							rules: [ { required: true, message: 'Please select your habitual residence!' } ]
						})(<DepartmentSelect />)}
					</Form.Item>
					<Form.Item label="Family Status">
						<Radio.Group onChange={handleIsMarried} value={selectedEmployee.isMarried}>
							<Radio value="yes">Married</Radio>
							<Radio value="no">Single</Radio>
						</Radio.Group>
					</Form.Item>
					<Form.Item>
						<Button type="primary" htmlType="submit">
							submit
						</Button>
					</Form.Item>
				</Form>
			) : (
				''
			)}
		</React.Fragment>
	);
}
UpdateEmpPage = Form.create({})(UpdateEmpPage);
export default UpdateEmpPage;

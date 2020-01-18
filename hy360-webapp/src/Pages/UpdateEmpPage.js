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


import DepartmentSelect from '../Components/DepartmentSelect';
import ajaxRequest from '../utils/ajax';
const { Step } = Steps;
const { Title, Text } = Typography;
const { RangePicker } = DatePicker;
const { Option } = Select;

function UpdateEmpPage(props) {
	const { form } = props;
	const { getFieldDecorator, getFieldsValue, validateFields, getFieldValue } = form;
	const [ employees, setEmployees ] = useState([]);
	const [ allDataEmployees, setAllDataEmployees ] = useState([]);
	const [ children, setChildren ] = useState([]);

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

			ajaxRequest('GET', `http://localhost:8085/hy360/employee?id=${d[0].id}`, null, ({ result }) => {
				setChildren(result.children);
				console.log(result);
			});
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

	const formItemLayoutWithOutLabel = {
		wrapperCol: {
			xs: { span: 24, offset: 0 },
			sm: { span: 20, offset: 4 }
		}
	};

	// const fetchChildren = () => {
	// 	const formItems = keys.map((k, index) => (
	// 		<Form.Item {...formItemLayout} label={`Children (${index + 1})`} required={false} key={k}>
	// 			{getFieldDecorator(`ages[${k}]`, {
	// 				validateTrigger: [ 'onChange', 'onBlur' ],
	// 				rules: [
	// 					{
	// 						required: true,
	// 						message: "Please input child's age or delete this field."
	// 					}
	// 				]
	// 			})(<InputNumber min={1} max={50} placeholder="Child's age" style={{ width: '60%', marginRight: 8 }} />)}
	// 			{keys.length > 1 ? (
	// 				<Icon className="dynamic-delete-button" type="minus-circle-o" onClick={() => remove(k)} />
	// 			) : null}
	// 		</Form.Item>
	// 	));
	// };

	// const add = () => {
	// 	const { form } = this.props;
	// 	// can use data-binding to get
	// 	const keys = form.getFieldValue('keys');
	// 	let id = this.state.child_num;
	// 	const nextKeys = keys.concat(id);
	// 	this.setState({ child_num: id + 1 });
	// 	// can use data-binding to set
	// 	// important! notify form to detect changes
	// 	form.setFieldsValue({
	// 		keys: nextKeys
	// 	});
	// };
	const handleAddChild = () => {
		// console.log(keys, getFieldsValue());

		setChildren([
			...children,
			{
				age: 0
			}
		]);
	};

	const remove = (k) => {    
    setChildren(children.filter((item,i)=>i !==k));

    

	};

	const handleUpdate = (e) => {
		e.preventDefault();

		validateFields((error, values) => {
			if (error) return;

			const _children = values.ages.map((age, i) => {
				console.log(children);
				return {
					...children[i],
					age
				};
			});
			const updatedData = {
				...selectedEmployee,
				...values,
				depId: values.department.depId,
				children: _children
			};
			setSelectedEmployee(updatedData);

			console.log(updatedData);

			ajaxRequest('PUT', `http://localhost:8085/hy360/employee`, JSON.stringify(updatedData), (res) => {
				console.log(res);
			});
		});
	};

	// getFieldDecorator('keys', { initialValue: [] });
	// const keys = getFieldValue('keys');
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

					{Object.keys(children).length > 0 ? (
						children.map((child, i) => (
							<Form.Item {...formItemLayout} label={`Child (${i + 1})`} required={false} key={i}>
								{getFieldDecorator(`ages[${i}]`, {
									validateTrigger: [ 'onChange', 'onBlur' ],
									initialValue: child.age,
									rules: [
										{
											required: true,
											message: "Please input child's age or delete this field."
										}
									]
								})(
									<InputNumber
										min={1}
										max={50}
										placeholder="Child's age"
										style={{ width: '60%', marginRight: 8 }}
									/>
								)}
								{/* <Icon
									className="dynamic-delete-button"
									type="minus-circle-o"
									onClick={() => remove(i)}
								/> */}
							</Form.Item>
						))
					) : (
						''
					)}

					<Form.Item {...formItemLayoutWithOutLabel} style={{ textAlign: 'center' }}>
						<Button type="dashed" onClick={handleAddChild}>
							<Icon type="plus" /> Add field
						</Button>
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

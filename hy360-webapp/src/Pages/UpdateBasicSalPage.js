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

import UpdateEmpPage from './UpdateEmpPage';
import ajaxRequest from '../utils/ajax';

const { Title, Text } = Typography;
function UpdateBasicSalPage(props) {
	const { form } = props;
	const { getFieldDecorator, getFieldsValue, validateFields, getFieldValue } = form;

	const inputStyle = { textAlign: 'right' };

	const [ basicSalaries, setBasicSalaries ] = useState({});
	useEffect(() => {
		ajaxRequest('GET', 'http://localhost:8085/hy360/basicSalaries', null, ({ result }) => {
			setBasicSalaries(result);
		});
	}, []);

	const handleUpdate = (e) => {
		e.preventDefault();
		validateFields((error, values) => {
			if (error) return;
			console.log(values, basicSalaries);

			ajaxRequest('PUT', 'http://localhost:8085/hy360/basicSalaries', JSON.stringify(values), (res) => {
				console.log(res);
			});
		});
	};

	const fiels = [
		{
			label: 'Permanent Admin Salary',
			name: 'perm_admin_salary',
			suffix: '€'
		},
		{
			label: 'Permanent Teach Salary',
			name: 'perm_teach_salary',
			suffix: '€'
		},
		{
			label: 'Family Bonus',
			name: 'family_bonus',
			suffix: '%'
		},
		{
			label: 'Annual Bonus',
			name: 'annual_bonus',
			suffix: '%'
		},
		{
			label: 'Library Bonus',
			name: 'library_bonus',
			suffix: '€'
		},
		{
			label: 'Research Bonus',
			name: 'research_bonus',
			suffix: '€'
		}
	];
	return (
		<div>
			<Title>Update basic salaries</Title>
			<Form layout="vertical" onSubmit={handleUpdate}>
				<Row gutter={16} type="flex" justify="center">
					{fiels.map((field, i) => {
						return (
							<Col key={i} span={6}>
								<Form.Item label={field.label} hasFeedback>
									{getFieldDecorator(field.name, {
										initialValue: basicSalaries[field.name],
										rules: [
											{
												required: true,
												message: `This is required. `
											},
											{
												validator: async (rule, value, callback) => {
													if (value < basicSalaries[field.name]) {
														callback('Bonus and salaries cannot be decreased');
													} else {
														callback();
													}
												}
											}
										]
									})(<Input style={inputStyle} suffix={field.suffix} />)}
								</Form.Item>
							</Col>
						);
					})}
					{/* <Col span={6}>
						<Form.Item label="Permanent Admin Salary" hasFeedback>
							{getFieldDecorator('perm_admin_salary', {
								initialValue: basicSalaries.perm_admin_salary,
								rules: [
									{
										required: true,
										message: 'Please input your Address'
									}
								]
							})(<Input style={inputStyle} suffix="€" />)}
						</Form.Item>
					</Col>
					<Col span={6}>
						<Form.Item label="Permanent Teach Salary" hasFeedback>
							{getFieldDecorator('perm_teach_salary', {
								initialValue: basicSalaries.perm_teach_salary,
								rules: [
									{
										required: true,
										message: 'Please input your Address'
									}
								]
							})(<Input style={inputStyle} suffix="€" />)}
						</Form.Item>
					</Col>
					<Col span={6}>
						<Form.Item label="Family Bonus" hasFeedback>
							{getFieldDecorator('family_bonus', {
								initialValue: basicSalaries.family_bonus,
								rules: [
									{
										required: true,
										message: 'Please input your Address'
									}
								]
							})(<Input style={inputStyle} suffix="%" />)}
						</Form.Item>
					</Col>
					<Col span={6}>
						<Form.Item label="Research Bonus" hasFeedback>
							{getFieldDecorator('research_bonus', {
								initialValue: basicSalaries.research_bonus,
								rules: [
									{
										required: true,
										message: 'Please input your Address'
									}
								]
							})(<Input style={inputStyle} suffix="%" />)}
						</Form.Item>
					</Col>
					<Col span={6}>
						{' '}
						<Form.Item label="Library Bonus" hasFeedback>
							{getFieldDecorator('library_bonus', {
								initialValue: basicSalaries.library_bonus,
								rules: [
									{
										required: true,
										message: 'Please input your Address'
									}
								]
							})(<Input style={inputStyle} suffix="%" />)}
						</Form.Item>
					</Col>
					<Col span={6}>
						{' '}
						<Form.Item label="Annual Bonus" hasFeedback>
							{getFieldDecorator('anual_bonus', {
								initialValue: basicSalaries.annual_bonus,
                validateTrigger: [ 'onChange', 'onBlur' ],
								rules: [
									{
										required: true,
										message: 'Please input your Address'
									},
									{
										validator: async (rule, value, callback) => {
                      console.log("HEY");
                      console.log(value,basicSalaries['annual_bonus']);
											if (value < basicSalaries['annual_bonus']) {
												callback('Bonus and salaries cannot be decreased');
											} else {
												callback();
											}
										}
									}
								]
							})(<Input style={inputStyle} suffix="%" />)}
						</Form.Item>
					</Col> */}
				</Row>
				<Form.Item>
					<Button htmlType="submit">Update</Button>
				</Form.Item>
			</Form>
		</div>
	);
}

UpdateBasicSalPage = Form.create({})(UpdateBasicSalPage);
export default UpdateBasicSalPage;

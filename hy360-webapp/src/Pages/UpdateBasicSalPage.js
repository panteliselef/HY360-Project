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

const { Title, Text } = Typography;
function UpdateBasicSalPage(props) {
	const { form } = props;
	const { getFieldDecorator, getFieldsValue, validateFields, getFieldValue } = form;

	const inputStyle = { textAlign: 'right' };
	return (
		<div>
			<Title>Update basic salaries</Title>
			<Form layout="vertical">
				<Row gutter={16} type="flex" justify="center">
					<Col span={6}>
						<Form.Item label="Permanent Admin Salary" hasFeedback>
							{getFieldDecorator('perm_admin_salary', {
								initialValue: '',
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
								initialValue: '',
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
								initialValue: '',
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
								initialValue: '',
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
								initialValue: '',
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
								initialValue: '',
								rules: [
									{
										required: true,
										message: 'Please input your Address'
									}
								]
							})(<Input style={inputStyle} suffix="%" />)}
						</Form.Item>
					</Col>
				</Row>
			</Form>
		</div>
	);
}

UpdateBasicSalPage = Form.create({})(UpdateBasicSalPage);
export default UpdateBasicSalPage;

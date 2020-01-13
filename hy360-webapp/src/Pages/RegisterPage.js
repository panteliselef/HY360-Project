import React from 'react';
import {
	Form,
	Input,
	Tooltip,
	Icon,
	Cascader,
	Select,
	Row,
	Col,
	Checkbox,
	Button,
	AutoComplete,
	Steps,
	message,
	InputNumber
} from 'antd';
import '../Style/register-page.css';

import DepartmentSelect from '../Components/DepartmentSelect';
const { Step } = Steps;

const { Option } = Select;

class RegisterPage extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			current: 0,
			child_num :0
		};
	}

	next() {
		const current = this.state.current + 1;
		this.setState({ current });
	}

	prev() {
		const current = this.state.current - 1;
		this.setState({ current });
	}

	handleSuccess = (getFieldsValue) => {
		// console.log(getFieldsValue);
		message.success('Processing complete!');
	};

	render() {
		// console.log(this.props);
		const { current } = this.state;
		const { getFieldDecorator, getFieldsValue, getFieldValue } = this.props.form;
		const tailFormItemLayout = {
			wrapperCol: {
				xs: {
					span: 24,
					offset: 0
				},
				sm: {
					span: 16,
					offset: 8
				}
			}
		};

		const prefixSelector = getFieldDecorator('prefix', {
			initialValue: '30'
		})(
			<Select style={{ width: 70 }}>
				<Option value="30">+30</Option>
			</Select>
		);

		const stepOneSubmit = (e) => {
			e.preventDefault();
			console.log(getFieldsValue());
		};

		const stepTwoSubmit = (e) => {
			e.preventDefault();
			console.log('Step 2', getFieldsValue());
		};

		const steps = [
			{
				title: 'Employee Info',
				getContent: ({ formItemLayout, handleSubmit }) => (
					<Form {...formItemLayout} onSubmit={stepOneSubmit}>
						<Form.Item label="First Name">
							{getFieldDecorator('fname', {
								rules: [
									{
										required: true,
										message: 'Please input your First Name'
									},
									{
										validator: () => {}
									}
								]
							})(<Input />)}
						</Form.Item>
						<Form.Item label="Last Name">
							{getFieldDecorator('lname', {
								rules: [
									{
										type: 'string',
										required: true,
										message: 'Please input your Last Name'
									},
									{
										validator: () => {}
									}
								]
							})(<Input />)}
						</Form.Item>
						<Form.Item label="Address">
							{getFieldDecorator('address', {
								rules: [
									{
										type: 'string',
										required: true,
										message: 'Please input your Address'
									},
									{
										validator: () => {}
									}
								]
							})(<Input />)}
						</Form.Item>
						<Form.Item label="Phone Number">
							{getFieldDecorator('phone', {
								rules: [ { required: true, message: 'Please input your phone number!' } ]
							})(<Input addonBefore={prefixSelector} style={{ width: '100%' }} />)}
						</Form.Item>
						<Form.Item label="IBAN">
							{getFieldDecorator('iban', {
								rules: [
									{
										type: 'string',
										required: true,
										message: 'Please input your IBAN'
									},
									{
										validator: () => {}
									}
								]
							})(<Input />)}
						</Form.Item>
						<Form.Item label="Bank Name">
							{getFieldDecorator('bankName', {
								rules: [
									{
										type: 'string',
										required: true,
										message: 'Please input your Bank '
									},
									{
										validator: () => {}
									}
								]
							})(<Input />)}
						</Form.Item>
						<Form.Item label="Univ. Department">
							{getFieldDecorator('department', {
								initialValue: { depId: '5' },
								rules: [
									{ required: true, message: 'Please select your habitual residence!' },
									{ validator: () => {} }
								]
							})(<DepartmentSelect />)}
						</Form.Item>
						<Form.Item {...tailFormItemLayout}>
							<Button type="primary" htmlType="submit">
								Hire
							</Button>
						</Form.Item>
					</Form>
				)
			},
			{
				title: 'Add Children',
				getContent: () => {
					const remove = (k) => {
						const { form } = this.props;
						// can use data-binding to get
						const keys = form.getFieldValue('keys');
						// We need at least one passenger
						if (keys.length === 1) {
							return;
						}

						// can use data-binding to set
						form.setFieldsValue({
							keys: keys.filter((key) => key !== k)
						});
					};

					const add = () => {
						const { form } = this.props;
						// can use data-binding to get
						const keys = form.getFieldValue('keys');
						let id = this.state.child_num;
						const nextKeys = keys.concat(id);
						this.setState({child_num:id+1});
						// can use data-binding to set
						// important! notify form to detect changes
						form.setFieldsValue({
							keys: nextKeys
						});
					};
					const formItemLayoutWithOutLabel = {
						wrapperCol: {
							xs: { span: 24, offset: 0 },
							sm: { span: 20, offset: 4 }
						}
					};
					getFieldDecorator('keys', { initialValue: [] });
					const keys = getFieldValue('keys');
					const formItems = keys.map((k, index) => (
						<Form.Item
							{...(index === 0 ? formItemLayout : formItemLayoutWithOutLabel)}
							label={index === 0 ? 'Children' : ''}
							required={false}
							key={k}
						>
							{getFieldDecorator(`ages[${k}]`, {
								validateTrigger: [ 'onChange', 'onBlur' ],
								rules: [
									{
										required: true,
										message: "Please input child's age or delete this field."
									}
								]
							})(<InputNumber min={1} max={50} placeholder="Child's age" style={{ width: '60%', marginRight: 8 }} />)}
							{keys.length > 1 ? (
								<Icon
									className="dynamic-delete-button"
									type="minus-circle-o"
									onClick={() => remove(k)}
								/>
							) : null}
						</Form.Item>
					));
					return (
						<Form onSubmit={stepTwoSubmit}>
							{formItems}
							<Form.Item {...formItemLayoutWithOutLabel}>
								<Button type="dashed" onClick={add} style={{ width: '60%' }}>
									<Icon type="plus" /> Add field
								</Button>
							</Form.Item>
							<Form.Item {...tailFormItemLayout}>
								<Button type="primary" htmlType="submit">
									Continue
								</Button>
							</Form.Item>
						</Form>
					);
				}
			},
			{
				title: 'Salary',
				getContent: () => 'Last-content'
			}
		];
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

		return (
			<div>
				<Steps current={current}>{steps.map((item) => <Step key={item.title} title={item.title} />)}</Steps>
				<div className="steps-content">
					<Row>
						<Col xs={{ span: 12, offset: 6 }}>
							{steps[current].getContent({ formItemLayout, handleSubmit: this.handleSubmit })}
						</Col>
					</Row>
				</div>
				<div className="steps-action">
					{current < steps.length - 1 && (
						<Button type="primary" onClick={() => this.next()}>
							Next
						</Button>
					)}
					{current === steps.length - 1 && (
						<Button type="primary" onClick={this.handleSuccess}>
							Done
						</Button>
					)}
					{current > 0 && (
						<Button style={{ marginLeft: 8 }} onClick={() => this.prev()}>
							Previous
						</Button>
					)}
				</div>
			</div>
		);
	}
}

RegisterPage = Form.create({})(RegisterPage);
export default RegisterPage;

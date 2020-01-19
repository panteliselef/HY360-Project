import React from 'react';
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
	DatePicker
} from 'antd';
import '../Style/register-page.css';

import { Row as FlexRow, Col as FlexCol } from 'react-flexbox-grid';

import DepartmentSelect from '../Components/DepartmentSelect';
import ajaxRequest from '../utils/ajax';
const { Step } = Steps;
const { Title, Text } = Typography;
const { RangePicker } = DatePicker;
const { Option } = Select;

class RegisterPage extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			current: 2,
			child_num: 0,
			hasChild: 'no',
			hiringInfo: {
				salaryType: 'temp_admin',
				isMarried: 'no'
			}
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

	hanldeSalaryTypeChange = (e) => {
		this.setState({ hiringInfo: { ...this.state.hiringInfo, salaryType: e.target.value } });
	};

	handleIsMarried = (e) => {
		this.setState({ hiringInfo: { ...this.state.hiringInfo, isMarried: e.target.value } });
	};
	handleChildrenRadioChange = (e) => {
		this.setState({ hasChild: e.target.value });
	};
	render() {
		// console.log(this.props);
		const { current } = this.state;
		const { validateFields, getFieldDecorator, getFieldsValue, getFieldValue, setFields } = this.props.form;
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
			validateFields([ 'fname', 'lname', 'address', 'phone', 'iban', 'bankName' ], (error, values) => {
				if (!error) {
					const { fname, lname } = values;

					ajaxRequest(
						'GET',
						`http://localhost:8085/hy360/checkExistance?fname=${fname}&lname=${lname}`,
						null,
						({ result }) => {
							if (result) {
								setFields({
									fname: {
										value: values.fname,
										errors: [ new Error('An employee already exists') ]
									},
									lname: {
										value: values.lname,
										errors: [ new Error('An employee already exists') ]
									}
								});
							} else {
								this.next();
							}
						}
					);
					console.log('ok');
					this.setState({ hiringInfo: { ...this.state.hiringInfo, ...getFieldsValue() } });
				} else {
					console.log('error');
				}
			});
		};

		const stepTwoSubmit = (e) => {
			e.preventDefault();

			validateFields((error, values) => {
				if (!error) {
					console.log('ok');
					const info = {
						...getFieldsValue(),
						...this.state.hiringInfo,
						departmentId: this.state.hiringInfo?.univDepartment?.depId,
						un_children: getFieldsValue()?.ages
					};
					this.setState({ hiringInfo: info });
					this.next();
				} else {
					console.log('error');
				}
			});
		};

		const stepThreeSubmit = (e) => {
			e.preventDefault();

			validateFields((error, values) => {
				if (!error) {
					console.log(values);
					const rangeValue = values['range-picker'];
					let info = {};
					if (rangeValue) {
						const ranges = [ rangeValue[0].format('YYYY-MM-DD'), rangeValue[1].format('YYYY-MM-DD') ];

						console.log(ranges);
						info = {
							...this.state.hiringInfo,
							tempSalary: values.temp_salary,
							starts_at: new Date(ranges[0]).getTime(),
							ends_at: new Date(ranges[1]).getTime()
						};
					} else {
						info = {
							...this.state.hiringInfo
						};
					}
					this.setState({ hiringInfo: info });

					// ajaxRequest('POST', 'http://localhost:8085/hy360/hiring', JSON.stringify(info), (res) => {
					// 	console.log(res);
					// });
					console.log(info);
				} else {
					console.log('error');
				}
			});
		};

		const steps = [
			{
				title: 'Employee Info',
				getContent: ({ formItemLayout, handleSubmit }) => (
					<div>
						<Title style={{ textAlign: 'center' }} level={2}>
							Fill in the neccessary info
						</Title>
						<Form {...formItemLayout} onSubmit={stepOneSubmit}>
							<Form.Item label="First Name" hasFeedback>
								{getFieldDecorator('fname', {
									rules: [
										{
											required: true,
											message: 'Please input your First Name'
										},
										{
											/* {
											validator: async (rule, value) => {
												throw new Error('NOWWw');
											}
										} */
										}
									]
								})(<Input />)}
							</Form.Item>
							<Form.Item label="Last Name" hasFeedback>
								{getFieldDecorator('lname', {
									rules: [
										{
											required: true,
											message: 'Please input your Last Name'
										},
										{
											/* {
											validator: async() => {
												throw new Error('yeas');
											}
										} */
										}
									]
								})(<Input />)}
							</Form.Item>
							<Form.Item label="Address" hasFeedback>
								{getFieldDecorator('address', {
									rules: [
										{
											required: true,
											message: 'Please input your Address'
										},
										{
											/* {
											validator: () => {}
										} */
										}
									]
								})(<Input />)}
							</Form.Item>
							<Form.Item label="Phone Number" hasFeedback>
								{getFieldDecorator('phone', {
									rules: [ { required: true, message: 'Please input your phone number!' } ]
								})(<Input addonBefore={prefixSelector} style={{ width: '100%' }} />)}
							</Form.Item>
							<Form.Item label="IBAN" hasFeedback>
								{getFieldDecorator('iban', {
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
									rules: [
										{
											required: true,
											message: 'Please input your Bank '
										},
										{
											/* {
											validator: () => {}
										} */
										}
									]
								})(<Input />)}
							</Form.Item>
							<Form.Item label="Univ. Department" hasFeedback>
								{getFieldDecorator('univDepartment', {
									initialValue: { depId: '5' },
									rules: [ { required: true, message: 'Please select your habitual residence!' } ]
								})(<DepartmentSelect />)}
							</Form.Item>
							<Form.Item label="Salary Type">
								<Radio.Group
									onChange={this.hanldeSalaryTypeChange}
									value={this.state.hiringInfo.salaryType}
								>
									<Radio value="perm_admin">Permanent Admin</Radio>
									<Radio value="temp_admin">Temporary Admin</Radio>
									<Radio value="perm_teach">Permanent Teacher</Radio>
									<Radio value="temp_teach">Temporary Teacher</Radio>
								</Radio.Group>
							</Form.Item>
							<Form.Item label="Family Status">
								<Radio.Group onChange={this.handleIsMarried} value={this.state.hiringInfo.isMarried}>
									<Radio value="yes">Married</Radio>
									<Radio value="no">Single</Radio>
								</Radio.Group>
							</Form.Item>
							<Form.Item {...tailFormItemLayout}>
								<Button type="primary" htmlType="submit">
									Continue
								</Button>
							</Form.Item>
						</Form>
					</div>
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
						this.setState({ child_num: id + 1 });
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
						<Form.Item {...formItemLayout} label={`Children (${index + 1})`} required={false} key={k}>
							{getFieldDecorator(`ages[${k}]`, {
								validateTrigger: [ 'onChange', 'onBlur' ],
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
						<div>
							<FlexRow between="xs" middle="xs">
								<FlexCol xs={8}>
									<Title level={2}>Do you have children ?</Title>
								</FlexCol>
								<FlexCol xs={4}>
									<Radio.Group onChange={this.handleChildrenRadioChange} value={this.state.hasChild}>
										<Radio value={'yes'}>yes</Radio>
										<Radio value={'no'}>no</Radio>
									</Radio.Group>
								</FlexCol>
							</FlexRow>
							<Form onSubmit={stepTwoSubmit}>
								{this.state.hasChild === 'yes' ? (
									<React.Fragment>
										{formItems}
										<Form.Item {...formItemLayoutWithOutLabel}>
											<Button type="dashed" onClick={add} style={{ width: '60%' }}>
												<Icon type="plus" /> Add field
											</Button>
										</Form.Item>
									</React.Fragment>
								) : (
									''
								)}
								<Form.Item {...tailFormItemLayout}>
									<Button type="primary" htmlType="submit">
										Continue
									</Button>
								</Form.Item>
							</Form>
						</div>
					);
				}
			},
			{
				title: 'Salary',
				getContent: () => {
					const { salaryType } = this.state.hiringInfo;
					return (
						<div>
							<Form {...formItemLayout} onSubmit={stepThreeSubmit}>
								<Title level={2}>We're almost done !</Title>
								{salaryType.startsWith('perm') ? (
									<Text>Press Submit to complete signup</Text>
								) : (
									<React.Fragment>
										<Text>Fill this info and press Submit</Text>
										<Form.Item label="Contract Salary" required={true}>
											{getFieldDecorator('temp_salary', {
												validateTrigger: [ 'onChange', 'onBlur' ],
												rules: [
													{
														required: true,
														message: 'Please input the salary !'
													}
												]
											})(
												<InputNumber
													min={300}
													placeholder="Salary €"
													style={{ width: '100%', marginRight: 8 }}
												/>
											)}
										</Form.Item>
										<Form.Item label="Employment dates">
											{getFieldDecorator('range-picker', {
												rules: [
													{ type: 'array', required: true, message: 'Please select time!' }
												]
											})(
												<RangePicker
													
												/>
											)}
										</Form.Item>
									</React.Fragment>
								)}
								<Form.Item>
									<Button type="primary" htmlType="submit">
										submit
									</Button>
								</Form.Item>
							</Form>
						</div>
					);
				}
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
					<Row style={{ maxWidth: '600px', width: '100%', margin: 'auto' }}>
						<Col xs={{ span: 20, offset: 2 }}>
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

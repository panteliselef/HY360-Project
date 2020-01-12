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
	message
} from 'antd';
import '../Style/register-page.css';
const { Step } = Steps;

const steps = [
	{
		title: 'Employee Info',
		content: <div>One</div>
	},
	{
		title: 'Add Children',
		content: 'Second-content'
	},
	{
		title: 'Salary',
		content: 'Last-content'
	}
];

const { Option } = Select;

class RegisterPage extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			current: 0
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

	render() {
		const { current } = this.state;
		const { getFieldDecorator } = this.props.form;
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
				<Steps current={current}>
          {steps.map(item => (
            <Step key={item.title} title={item.title} />
          ))}
        </Steps>
				<div className="steps-content">{steps[current].content}</div>
				<div className="steps-action">
					{current < steps.length - 1 && (
						<Button type="primary" onClick={() => this.next()}>
							Next
						</Button>
					)}
					{current === steps.length - 1 && (
						<Button type="primary" onClick={() => message.success('Processing complete!')}>
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

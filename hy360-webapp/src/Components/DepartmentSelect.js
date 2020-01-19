import React, { Component } from 'react';
import { Select } from 'antd';
import ajaxRequest from '../utils/ajax';

class DepartmentSelect extends Component {
	constructor(props) {
		super(props);
		this.state = {
			departments: []
		};
	}

	componentDidMount() {
		ajaxRequest('GET', `http://localhost:8085/hy360/department`, null, ({ result }) => {
			this.setState({ departments: result });
		});
	}

	componentDidUpdate() {
		console.log('VAL', this.props.value);
	}

	handleCurrencyChange = (id) => {
		console.log('DEP', id);
		this.triggerChange({ id });
	};

	triggerChange = (changedValue) => {
		const { onChange, value } = this.props;
		if (onChange) {
			onChange({
				...value,
				...changedValue
			});
		}
	};

	render() {
		const { value } = this.props;
		const { Option } = Select;
		return (
			<Select value={value.id} style={{ width: '100%' }} onChange={this.handleCurrencyChange}>
				{this.state.departments.map((dep) => {
					return (
						<Option key={dep.id} value={dep.id}>
							{dep.name}
						</Option>
					);
				})}
				{/* <Option value="6">Department of Mathematics</Option> */}
			</Select>
		);
	}
}

export default DepartmentSelect;

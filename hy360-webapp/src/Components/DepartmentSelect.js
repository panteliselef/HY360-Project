import React, { Component } from 'react';
import { Select } from 'antd';

class DepartmentSelect extends Component {
	constructor(props) {
		super(props);
	}

	handleCurrencyChange = (depId) => {
		this.triggerChange({ depId });
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
			<Select value={value.depId} style={{ width: '100%' }} onChange={this.handleCurrencyChange}>
				<Option value="5">Department of Computer Science</Option>
				<Option value="6">Department of Mathematics</Option>
			</Select>
		);
	}
}

export default DepartmentSelect;

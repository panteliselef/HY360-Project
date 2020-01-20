import React, { useEffect, useState } from 'react';
import { Form, Input, Icon, Typography, Row, Col, Button, Radio, Table, Select, DatePicker } from 'antd';
import ajaxRequest from '../utils/ajax';

const { Title } = Typography;
const { Option } = Select;
const { RangePicker } = DatePicker;
function StatisticsPage(props) {
	const [ categoriesStats, setCategoriesStats ] = useState([]);
	const [ sumSalaries, setSumSalaries ] = useState([]);

	const [ data, setDate ] = useState([]);

	const [ category, setCategory ] = useState('perm_admin');
	const [ bonusType,setBonusType ] = useState('payments');

	const [ increasePercent,setIncreasePercent ] = useState([]);

	const columns = [
		{
			title: 'Category',
			dataIndex: 'categoryName'
		},
		{
			title: 'MAX',
			dataIndex: 'max'
		},
		{
			title: 'MIN',
			dataIndex: 'min'
		},
		{
			title: 'AVG',
			dataIndex: 'avg'
		}
	];

	const sumCols = [
		{
			title: 'Year',
			dataIndex: 'year'
		},
		{
			title: 'Increase %',
			dataIndex: 'value'
		}
	];

	const requests = [
		{
			categoryName: 'perm_admin',
			modes: [ 'max', 'min', 'avg' ]
		},
		{
			categoryName: 'perm_teach',
			modes: [ 'max', 'min', 'avg' ]
		},
		{
			categoryName: 'temp_admin',
			modes: [ 'max', 'min', 'avg' ]
		},
		{
			categoryName: 'temp_teach',
			modes: [ 'max', 'min', 'avg' ]
		}
	];

	useEffect(
		() => {
			// console.log("E",sumSalaries.filter((item) => item.categoryName === category));

			const d = sumSalaries.filter((item) => item.categoryName === category);
			const d1 = d[0]?.total_salaries.map((item, i) => {
				return {
					...item,
					key: i
				};
			});
			setDate(d1);
		},
		[ category ]
	);

	useEffect(
		() => {
			console.log(categoriesStats);
		},
		[ categoriesStats ]
	);


	useEffect(
		() => {
			ajaxRequest(
			'GET',
			`http://localhost:8085/hy360/stats?mode=avg_increase&category=${bonusType}`,
			null,
			(res) => {
				console.log(res);
				setIncreasePercent(res);
				// setSumSalaries(
				// 	res.map((r, i) => {
				// 		return {
				// 			...r,
				// 			key: i
				// 		};
				// 	})
				// );
			}
		);
		},
		[ bonusType ]
	);

	useEffect(() => {
		const d = [];
		ajaxRequest('GET', `http://localhost:8085/hy360/sal_emp_type`, null, (res) => {
			setCategoriesStats(
				res.map((r, i) => {
					return {
						...r,
						key: i
					};
				})
			);
		});

		ajaxRequest('GET', `http://localhost:8085/hy360/stats?mode=sum`, null, (res) => {
			console.log(res);
			setSumSalaries(res);

			// setSumSalaries(
			// 	res.map((r, i) => {
			// 		return {
			// 			...r,
			// 			key: i
			// 		};
			// 	})
			// );
		});
	}, []);

	const handleCategoryChange = (value) => {
		setCategory(value);
	};

	const handleBonusTypeChange = (value) => {
		setBonusType(value);
	};

	const handleRangeChange = (date, dateString) => {
		console.log(date, dateString);

		ajaxRequest(
			'GET',
			`http://localhost:8085/hy360/stats?mode=avg_increase&from=${dateString[0]}&until=${dateString[1]}`,
			null,
			(res) => {
				console.log(res);
				// setSumSalaries(
				// 	res.map((r, i) => {
				// 		return {
				// 			...r,
				// 			key: i
				// 		};
				// 	})
				// );
			}
		);
	};
	return (
		<div>
			<Table columns={columns} dataSource={categoriesStats} />

			<Title level={2}>Sum of Salaries by Category</Title>

			<Select defaultValue={category} style={{ width: 300, display: 'block' }} onChange={handleCategoryChange}>
				<Option value="perm_admin">Permanent Admin</Option>
				<Option value="temp_admin">Temporary Admin</Option>
				<Option value="perm_teach">Permanent Teach</Option>
				<Option value="temp_teach">Temporary Teach</Option>
			</Select>
			<Table columns={sumCols} dataSource={data} />

			<br />
			<br />
			<Title level={2}>AVG increase of salaries and bonuses</Title>

			<Select defaultValue={bonusType} style={{ width: 300, display: 'block' }} onChange={handleBonusTypeChange}>
				<Option value="payments">Salaries</Option>
				<Option value="research_bonus">Reseach Bonus</Option>
				<Option value="library_bonus">Library Bonus</Option>
				<Option value="family_bonus">Family Bonus</Option>
				<Option value="annual_bonus">Annual Bonus</Option>
			</Select>
			<br />
			<br />

			<Table columns={sumCols}  dataSource={increasePercent}/>
			<br />
		</div>
	);
}

export default StatisticsPage;

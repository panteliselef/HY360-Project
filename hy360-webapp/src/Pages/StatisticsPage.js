import React, { useEffect, useState } from 'react';
import { Form, Input, Icon, Typography, Row, Col, Button, Radio, Table, Select, DatePicker } from 'antd';
import ajaxRequest from '../utils/ajax';

const { Title } = Typography;
const { Option } = Select;
const {RangePicker} = DatePicker;
function StatisticsPage(props) {
	const [ categoriesStats, setCategoriesStats ] = useState([]);
	const [ sumSalaries, setSumSalaries ] = useState([]);

	const [data,setDate] = useState([]);

	const [ category, setCategory ] = useState('perm_admin');

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
			title: 'Total Salaries',
			dataIndex: 'amount'
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

			const d = sumSalaries.filter(item => item.categoryName === category);
			const d1 = d[0]?.total_salaries.map((item,i)=>{
				return {
					...item,key:i
				}
			})
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

	const handleRangeChange = (date,dateString) => {
		console.log(date,dateString);


		ajaxRequest('GET', `http://localhost:8085/hy360/stats?mode=avg_increase&from=${dateString[0]}&until=${dateString[1]}`, null, (res) => {
			console.log(res);
			// setSumSalaries(
			// 	res.map((r, i) => {
			// 		return {
			// 			...r,
			// 			key: i
			// 		};
			// 	})
			// );
		});
	}
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

			<br/>
			<br/>
			<Title level={2}>AVG increase of salaries and bonuses</Title>
			
			<RangePicker onChange={handleRangeChange}></RangePicker>
			<br/>
			<br/>

			<Table columns={sumCols} />
			<br/>
		</div>
	);
}

export default StatisticsPage;

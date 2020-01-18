import React, { useEffect, useState } from 'react';
import { Form, Input, Icon, Typography, Row, Col, Button, Radio, Table } from 'antd';
import ajaxRequest from '../utils/ajax';

const { Title } = Typography;

function StatisticsPage(props) {
	const [ categoriesStats, setCategoriesStats ] = useState([]);
	const [ sumSalaries, setSumSalaries ] = useState([]);

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
			title: 'Category',
			dataIndex: 'categoryName'
		},
		{
			title: 'Total Salaries',
			dataIndex: 'total_salaries'
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

		ajaxRequest('GET', `http://localhost:8085/hy360/stats`, null, (res) => {
			setSumSalaries(
				res.map((r, i) => {
					return {
						...r,
						key: i
					};
				})
			);
		});
	}, []);

	return (
		<div>
			<Table columns={columns} dataSource={categoriesStats} />

			<Title level={2}>Sum of Salaries by Category</Title>

			<Table columns={sumCols} dataSource={sumSalaries} />
		</div>
	);
}

export default StatisticsPage;

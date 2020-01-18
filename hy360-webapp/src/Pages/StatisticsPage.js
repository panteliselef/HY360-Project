import React, { useEffect, useState } from 'react';
import { Form, Input, Icon, Typography, Row, Col, Button, Radio, Table } from 'antd';
import ajaxRequest from '../utils/ajax';

function StatisticsPage(props) {
	const [ categoriesStats, setCategoriesStats ] = useState([]);

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
		ajaxRequest(
			'GET',
			`http://localhost:8085/hy360/sal_emp_type`,
			null,
			(res) => {
        console.log(res);
        setCategoriesStats(res);
			}
		);
	}, []);

	return (
		<div>
			<Table columns={columns} dataSource={categoriesStats} />
		</div>
	);
}

export default StatisticsPage;

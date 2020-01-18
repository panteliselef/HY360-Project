import React, { useEffect, useState } from 'react';
import { Form, Input, Icon, Typography, Row, Col, Button, Radio, Table } from 'antd';
import ajaxRequest from '../utils/ajax';

const { Title } = Typography;

function AllDataEmployeePage() {
	const [ employees, setEmployees ] = useState([]);
	const [ selectedEmployee, setSelectedEmployee ] = useState({});

	const [ fullInfoEmp, setFullInfoEmp ] = useState([]);
	const [children, setChildren] = useState([]);

	const [ allColumns, setAllColumns ] = useState([]);

	useEffect(() => {
		ajaxRequest('GET', `http://localhost:8085/hy360/employee`, null, ({ result }) => {
			// setAllDataEmployees(result);
			const data = result.map((element) => {
				return {
					key: element.id,
					fname: element.fname,
					lname: element.lname
				};
			});
			setEmployees(data);
		});

		// ajaxRequest('GET',`http://localhost:8085/hy360/fullinfo?id=${45}`,null,(res)=>{
		//   console.log(res);
		// })
	}, []);

	const columns = [
		{
			title: 'First Name',
			dataIndex: 'fname'
			// render: (text) => <a>{text}</a>
		},
		{
			title: 'Last Name',
			dataIndex: 'lname'
		}
	];

		


	const rowSelection = {
		onChange: (selectedRowKeys, selectedRows) => {
			console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);

			const d = employees.filter((employee) => employee.key === selectedRowKeys[0]);
			console.log(employees, d[0]);
			setSelectedEmployee(d[0]);

			ajaxRequest('GET', `http://localhost:8085/hy360/fullinfo?id=${d[0].key}`, null, ({ result }) => {
				const data = [ result ].map((element) => {
					return {
						...element,
						key: element.id,
						children: [],
						un_children: []
					};
				});

				setChildren(result.children);
				setFullInfoEmp(data);
				console.log(data);

				let cols = [];
				Object.entries(result).map((entry,i) => {

          let d = {}
          if(i < 3){
            d = {fixed:'left'}
					}
					cols = [
						...cols,
						{
							title: entry[0],
              dataIndex: entry[0],
              ...d,
						}
					];
				});

				setAllColumns(cols);
				console.log(cols);
			});
		},
		getCheckboxProps: (record) => ({
			disabled: record.name === 'Disabled User', // Column configuration not to be checked
			name: record.name
		}),
		type: 'radio'
	};


	const expandedRowRender = () => {
    const childColumns = [
      { title: `Child's age`, dataIndex: 'age', key: 'age' },
    ];

		console.log(fullInfoEmp.children);
    return <Table columns={childColumns} dataSource={children} pagination={false} />;
  };
	return (
		<div>
			<Title>Select Employee</Title>

			<Table rowSelection={rowSelection} columns={columns} dataSource={employees} />

			{console.log(Array.isArray(employees))}
			{console.log(Array.isArray(fullInfoEmp))}

				<Table columns={allColumns} scroll={{ x: true }} expandedRowRender={expandedRowRender} dataSource={fullInfoEmp} />
			{/* {Object.keys(fullInfoEmp).length > 0 ? (
			) : (
				''
			)} */}
			{/* <Table columns={allColumns} scroll={{x:true}} dataSource={fullInfoEmp} /> */}
		</div>
	);
}

export default AllDataEmployeePage;

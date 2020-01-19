import React from 'react';
import { Layout, Menu, Breadcrumb, Icon, Slider, Typography, Row, Col } from 'antd';
import RegisterPage from '../Pages/RegisterPage';
import { Route, Link } from 'react-router-dom';
import UpdateEmpPage from '../Pages/UpdateEmpPage';
import UpdateBasicSalPage from '../Pages/UpdateBasicSalPage';
import AllDataEmployeePage from '../Pages/AllDataEmployeePage';
import StatisticsPage from '../Pages/StatisticsPage';
import RetirePage from '../Pages/RetirePage';
import PromotePage from '../Pages/PromotePage';

const { Header, Content, Footer, Sider } = Layout;
const { SubMenu } = Menu;
const { Title } = Typography;

class TestLayout2 extends React.Component {
	state = {
		collapsed: false
	};

	onCollapse = (collapsed) => {
		console.log(collapsed);
		this.setState({ collapsed });
	};

	render() {
		return (
			<Layout style={{ minHeight: '100vh' }}>
				<Header className="header">
					<Row type="flex" justify="start" align="middle" style={{ height: '100%' }}>
						<Col span={24}>
							<Title level={2} style={{ textAlign: 'left', margin: 'auto', color: '#fff' }}>
								HY360 - Project
							</Title>
						</Col>
					</Row>
					{/* <div className="logo" /> */}
					{/* <Menu theme="dark" mode="horizontal" defaultSelectedKeys={[ '2' ]} style={{ lineHeight: '64px' }}>
						<Menu.Item key="1">nav 1</Menu.Item>
						<Menu.Item key="2">nav 2</Menu.Item>
						<Menu.Item key="3">nav 3</Menu.Item>
					</Menu> */}
				</Header>
				<Layout>
					<Sider width={200} style={{ background: '#fff' }}>
						<Menu
							mode="inline"
							defaultSelectedKeys={[ '1' ]}
							defaultOpenKeys={[ 'sub1' ]}
							selectedKeys={[ this.props.routeLocation.pathname ]}
							style={{ height: '100%', borderRight: 0 }}
						>
							<Menu.Item key="/hiring">
								<Link to="/hiring">Hiring</Link>
							</Menu.Item>
							<SubMenu
								key="/update"
								title={
									<span>
										<Icon type="user" />
										Update
									</span>
								}
							>
								<Menu.Item key="/update-employee">
									<Link to="/update-employee">Employee</Link>
								</Menu.Item>
								<Menu.Item key="/update-basic-salaries">
									<Link to="/update-basic-salaries">Basic Salaries</Link>
								</Menu.Item>
								{/* <Menu.Item key="2">option2</Menu.Item> */}
								{/* <Menu.Item key="3">option3</Menu.Item> */}
								{/* <Menu.Item key="4">option4</Menu.Item> */}
							</SubMenu>
							<SubMenu
								key="/data"
								title={
									<span>
										<Icon type="laptop" />
										Questions
									</span>
								}
							>
								<Menu.Item key="/data-employee">
									<Link to="/data-employee">Employee</Link>
								</Menu.Item>
								<Menu.Item key="/data-stats">
									<Link to="/data-stats">Statistics</Link>
								</Menu.Item>
								<Menu.Item key="6">option6</Menu.Item>
								<Menu.Item key="7">option7</Menu.Item>
								<Menu.Item key="8">option8</Menu.Item>
							</SubMenu>
							<SubMenu
								key="/action"
								title={
									<span>
										<Icon type="notification" />
										Actions
									</span>
								}
							>
								<Menu.Item key="/action-retire">
									<Link to="/action-retire">Retire</Link>
								</Menu.Item>
								<Menu.Item key="/action-promote">
									<Link to="/action-promote">Promote</Link>
								</Menu.Item>
							</SubMenu>
						</Menu>
					</Sider>
					<Layout style={{ padding: '24px 24px' }}>
						{/* <Breadcrumb style={{ margin: '16px 0' }}>
							<Breadcrumb.Item>Home</Breadcrumb.Item>
							<Breadcrumb.Item>List</Breadcrumb.Item>
							<Breadcrumb.Item>App</Breadcrumb.Item>
						</Breadcrumb> */}
						<Content
							style={{
								background: '#fff',
								padding: 24,
								margin: 0,
								minHeight: 280
							}}
						>
							<Route exact path="/hiring" component={RegisterPage} />
							<Route exact path="/update-employee" component={UpdateEmpPage} />
							<Route exact path="/update-basic-salaries" component={UpdateBasicSalPage} />
							<Route exact path="/data-employee" component={AllDataEmployeePage} />
							<Route exact path="/data-stats" component={StatisticsPage} />
							<Route exact path="/action-retire" component={RetirePage} />
							<Route exact path="/action-promote" component={PromotePage} />
							{/* <RegisterPage /> */}
						</Content>
					</Layout>
				</Layout>
			</Layout>
		);
	}
}

export default TestLayout2;

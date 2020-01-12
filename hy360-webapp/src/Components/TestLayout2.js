import React from 'react';
import { Layout, Menu, Breadcrumb, Icon, Slider, Typography, Row, Col } from 'antd';
import RegisterPage from '../Pages/RegisterPage';
import { Link } from 'react-router-dom';

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
							selectedKeys={[this.props.routeLocation.pathname ]}
							style={{ height: '100%', borderRight: 0 }}
						>
							<Menu.Item key="/about">
								<Link to="/about">About</Link>
							</Menu.Item>
							<SubMenu
								key="sub1"
								title={
									<span>
										<Icon type="user" />
										subnav 1
									</span>
								}
							>
								<Menu.Item key="/option1">
									<Link to="/option1">option1</Link>
								</Menu.Item>
								<Menu.Item key="2">option2</Menu.Item>
								<Menu.Item key="3">option3</Menu.Item>
								<Menu.Item key="4">option4</Menu.Item>
							</SubMenu>
							<SubMenu
								key="sub2"
								title={
									<span>
										<Icon type="laptop" />
										subnav 2
									</span>
								}
							>
								<Menu.Item key="5">option5</Menu.Item>
								<Menu.Item key="6">option6</Menu.Item>
								<Menu.Item key="7">option7</Menu.Item>
								<Menu.Item key="8">option8</Menu.Item>
							</SubMenu>
							<SubMenu
								key="sub3"
								title={
									<span>
										<Icon type="notification" />
										subnav 3
									</span>
								}
							>
								<Menu.Item key="9">option9</Menu.Item>
								<Menu.Item key="10">option10</Menu.Item>
								<Menu.Item key="11">option11</Menu.Item>
								<Menu.Item key="12">option12</Menu.Item>
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
							<RegisterPage />
						</Content>
					</Layout>
				</Layout>
			</Layout>
		);
	}
}

export default TestLayout2;

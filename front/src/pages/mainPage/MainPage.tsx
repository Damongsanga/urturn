import { Button, Card, Grid, Image, GridRow, GridColumn } from 'semantic-ui-react';
import 'semantic-ui-css/semantic.min.css';
import './MainPage.css';

const MainPage: React.FC = () => {
	const cardStyle = {
		width: '22vw',
		height: '55vh',
		backgroundColor: '#f8d1c2', // Replace with the exact color from the image
		color: '#4f4f4f', // Replace with the exact text color from the image
		border: '3px solid white',
		paddingTop: '3em',
		paddingBottom: '3em',
		paddingLeft: '4em',
		paddingRight: '4em',
		borderRadius: '20px', // Adjust as needed to match the border-radius in the image
	};

	const buttonStyle = {
		backgroundColor: 'white', // Or another color that matches your design
		color: '#4f4f4f', // Button text color
		width: '100%', // Make the button fill the card width
	};

	return (
		<div style={{ width: '100vw', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
			<div style={{}}>
				<Grid columns={2}>
					<GridRow>
						<GridColumn>
							<Card className='EntryCard'>
								<Card style={cardStyle}>
									<Image
										src='https://avatars.githubusercontent.com/u/19562994?v=4'
										circular
										size='medium'
									/>
									<Card.Content textAlign='center'>
										<Card.Description>직접 방을 만들어주세요</Card.Description>
									</Card.Content>
									<Card.Content extra>
										<Button style={buttonStyle}>방 만들기</Button>
									</Card.Content>
								</Card>
							</Card>
						</GridColumn>
						<GridColumn>
							<Card className='EntryCard'>
								<Card style={cardStyle} centered>
									<Image
										src='https://avatars.githubusercontent.com/u/19562994?v=4'
										circular
										size='medium'
										centered
									/>
									<Card.Content textAlign='center'>
										<Card.Description>참여하세요</Card.Description>
									</Card.Content>
									<Card.Content extra>
										<Button style={buttonStyle}>입장하기</Button>
									</Card.Content>
								</Card>
							</Card>
						</GridColumn>
					</GridRow>
				</Grid>
			</div>
		</div>
	);
};

export default MainPage;

import React, { useEffect } from 'react';
import { Button, Card, Grid, Image, GridRow, GridColumn, Modal, Header, CardGroup } from 'semantic-ui-react';
import { useRoomStore } from '../../stores/room';
import 'semantic-ui-css/semantic.min.css';
import { HeaderBar } from '../../components/header/HeaderBar.tsx';
import './MainPage.css';

import { WaitingPage } from '../waitingPage/WaitingPage';
import { EntryCodeModal } from '../../components/modal/EntryCodeModal.tsx';

import { useAuthStore } from '../../stores/useAuthStore.ts';

const MainPage: React.FC = () => {
	const roomStore = useRoomStore();
	const [open, setOpen] = React.useState(false);
	const [openModal, setOpenModal] = React.useState(false);

	const authStore = useAuthStore();

    useEffect(() => {
        roomStore.clearRoom();
    }, []);

	const cardStyle = {
		width: '28vw',
		height: '60vh',
		backgroundColor: '#DBB39A', // Replace with the exact color from the image
		color: '#4f4f4f', // Replace with the exact text color from the image
		border: '3px solid white',
		paddingTop: '3em',
		paddingBottom: '3em',
		paddingLeft: '4em',
		paddingRight: '4em',
		borderRadius: '10px', // Adjust as needed to match the border-radius in the image
	};

	const buttonStyle = {
		backgroundColor: 'white', // Or another color that matches your design
		color: '#4f4f4f', // Button text color
		width: '100%', // Make the button fill the card width
	};

    const createRoom = () => {
		if(authStore.accessToken===undefined || authStore.accessToken===null || authStore.memberId === undefined)  { console.log("로그인 해야합니다."); return;}
        roomStore.createRoom(authStore.accessToken, authStore.memberId);
        setOpen(true);
    }

	const enterEntryCode = () => {
		setOpenModal(true);
	};
	return (
		<>
			<div className='MainPage'>
				<div>
					<HeaderBar $main={true} />
				</div>
				<div className='EntrySection'>
					<Grid columns={2}>
						<GridRow>
							<GridColumn>
								<Card className='EntryCard'>
									<CardGroup>
										<Card style={cardStyle}>
											<Image
												src='https://avatars.githubusercontent.com/u/19562994?v=4'
												circular
												size='medium'
											/>
											<Card.Content textAlign='center'>
												<Card.Description style={{ marginTop: '2vh', color: 'white' }}>
													직접 방을 만들어보세요
												</Card.Description>
											</Card.Content>
											<Card.Content style={{ borderTop: 'none' }}>
												<Button
													style={buttonStyle}
													onClick={(_e) => {
														createRoom();
													}}
												>
													방 만들기
												</Button>
											</Card.Content>
										</Card>
									</CardGroup>
								</Card>
							</GridColumn>
							<GridColumn>
								<Card className='EntryCard'>
									<CardGroup>
										<Card style={cardStyle}>
											<Image
												src='https://avatars.githubusercontent.com/u/19562994?v=4'
												circular
												size='medium'
											/>
											<Card.Content textAlign='center'>
												<Card.Description style={{ marginTop: '2vh', color: 'white' }}>
													만들어진 방에 참여해보세요
												</Card.Description>
											</Card.Content>
											<Card.Content style={{ borderTop: 'none' }}>
												<Button
													style={buttonStyle}
													onClick={(_e) => {
														enterEntryCode();
													}}
												>
													입장하기
												</Button>
											</Card.Content>
										</Card>
									</CardGroup>
								</Card>
							</GridColumn>
						</GridRow>
					</Grid>
				</div>
			</div>

			<Modal open={open}>
				<Header icon='archive' content='준비 중' />
				<Modal.Content>
					<WaitingPage
						changeModal={() => {
							setOpen(false);
						}}
					/>
				</Modal.Content>
			</Modal>

			<Modal open={openModal} size='mini'>
				<Header content='입장코드 입력' />
				<Modal.Content>
					<EntryCodeModal
						changeModal={() => {
							setOpenModal(false);
						}}
					/>
				</Modal.Content>
			</Modal>
		</>
	);
};

export default MainPage;

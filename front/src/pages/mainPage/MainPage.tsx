import React, { useEffect, useState } from 'react';
import { Button, Card, Grid, Image, GridRow, GridColumn, Modal, Header } from 'semantic-ui-react';
import { useRoomStore } from '../../stores/room';
import { useRtcStore } from "../../stores/rtc.ts";
import { HeaderBar } from '../../components/header/HeaderBar.tsx';
import { WaitingPage } from '../waitingPage/WaitingPage';
import { EntryCodeModal } from '../../components/modal/EntryCodeModal.tsx';
import { useAuthStore } from '../../stores/useAuthStore.ts';
import { useWebSocket } from '../../hooks/webSocket.ts';
import { useAxios } from '../../utils/useAxios.ts';

import 'semantic-ui-css/semantic.min.css';
import './MainPage.css';

import joinTeam from '../../assets/images/join_team.png'
import createTeam from '../../assets/images/create_team.png'

const MainPage: React.FC = () => {
	const roomStore = useRoomStore();
	const rtcStore = useRtcStore();
	const [open, setOpen] = useState(false);
	const [openModal, setOpenModal] = useState(false);
	const webSocket = useWebSocket();

	const authStore = useAuthStore();
	const ax = useAxios(true);

	const clearMainLogic = () => {
		if(roomStore.client || roomStore.userInfo?.myUserNickName || roomStore.roomInfo?.roomId){
			roomStore.clearRoom();
		}
		if(rtcStore.getOpenVidu() || rtcStore.getSessionId() || rtcStore.getConnectionId()){
			ax.delete('/sessions/'+rtcStore.getSessionId() + '/connection/' + rtcStore.getConnectionId());
			ax.delete('/sessions/'+rtcStore.getSessionId());
			rtcStore.clearRtc();
		}
	}

	useEffect(() => {
		clearMainLogic();
	}, [])

	useEffect(() => {
		if(open==false){
			clearMainLogic();
		}
	}, [open]);

	const buttonStyle = {};

	const createRoom = () => {
		if (authStore.accessToken === undefined || authStore.accessToken === null || authStore.memberId === undefined) {
			console.log('로그인 해야합니다.');
			return;
		}
		webSocket.connect();
		setOpen(true);
	};

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
					<Grid columns={2} style={{ width: '70vw' }}>
						<GridRow>
							<GridColumn className='MainGridColumn'>
								<Card className='EntryCard'>
									<Card.Content style={{ paddingBottom: '0px', paddingLeft: '0px', paddingRight: '0px'}}>
										<Image
											src={createTeam}
											size='medium'
											style={{ width: '21vw', height: 'auto' }}
										/>
									</Card.Content>
									<Card.Content textAlign='center' style={{ borderTop: 'none' }}>
										<Card.Description
											style={{
												display: 'flex',
												alignItems: 'center',
												justifyContent: 'center',
												marginTop: '2vh',
												color: 'black',
											}}
										>
											직접 방을 만들어보세요
										</Card.Description>
									</Card.Content>
									<Card.Content
										style={{
											display: 'flex',
											alignItems: 'center',
											justifyContent: 'center',
											borderTop: 'none',
											paddingLeft: '0px',
											paddingRight: '0px',
											paddingBottom: '0px'
										}}
									>
										<Button
											className='EntryButtons'
											style={buttonStyle}
											onClick={(_e) => {
												createRoom();
											}}
										>
											방 만들기
										</Button>
									</Card.Content>
								</Card>
							</GridColumn>
							<GridColumn className='MainGridColumn'>
								<Card className='EntryCard'>
									<Card.Content style={{ paddingBottom: '0px', paddingLeft: '0px', paddingRight: '0px'}}>
										<Image
											src={joinTeam}
											size='medium'
											style={{ width: '21vw', height: 'auto' }}
										/>
									</Card.Content>
									<Card.Content textAlign='center' style={{ borderTop: 'none' }}>
										<Card.Description
											style={{
												display: 'flex',
												alignItems: 'center',
												justifyContent: 'center',
												textAlign: 'center',
												borderTop: 'none',
												marginTop: '2vh',
												color: 'black',
											}}
										>
											만들어진 방에 합류해보세요
										</Card.Description>
									</Card.Content>
									<Card.Content
										style={{
											display: 'flex',
											alignItems: 'center',
											justifyContent: 'center',
											borderTop: 'none',
											paddingLeft: '0px',
											paddingRight: '0px',
											paddingBottom: '0px'
										}}
									>
										<Button
											className='EntryButtons'
											style={buttonStyle}
											onClick={(_e) => {
												enterEntryCode();
											}}
										>
											입장하기
										</Button>
									</Card.Content>
								</Card>
							</GridColumn>
						</GridRow>
					</Grid>

					<Button onClick={
						() => {
							ax.get('/test/test').then(res => {console.log(res.data)})
						}
					}></Button>
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
						successConnect={() => {
							setOpenModal(false);
							setOpen(true);
						}}
					/>
				</Modal.Content>
			</Modal>
		</>
	);
};

export default MainPage;

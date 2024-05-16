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

import joinRoomImg from '../../assets/images/join_room.png'
import createRoomImg from '../../assets/images/create_room.png'

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
			if(roomStore.roomInfo?.roomId){
				roomStore.client?.publish({
					destination: '/app/leaveRoom',
					body: JSON.stringify({
						roomId: roomStore.roomInfo.roomId,
						isHost: roomStore.roomInfo.host
					}),
				})
			}
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
											src={createRoomImg}
											size='medium'
											style={{ width: '21vw', height: 'auto' }}
										/>
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
											src={joinRoomImg}
											size='medium'
											style={{ width: '21vw', height: 'auto' }}
										/>
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
				</div>
			</div>

			<Modal open={open} size='tiny'>
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

import {
	MenuItem,
	Menu,
	Card,
	CardContent,
	CardHeader,
	CardGroup,
	Header,
	Image,
	Divider,
	Form,
	FormInput,
	Icon,
	GridColumn,
	Grid,
	GridRow,
	FormField,
	Segment,
	Button,
	Radio,
} from 'semantic-ui-react';
import logo from '../../assets/images/logo.svg';
import './WaitingPage.css';
import {ChangeEvent, FormEvent, useEffect, useRef, useState} from 'react';
import { useRoomStore } from '../../stores/room';
import {useRtcStore} from "../../stores/rtc.ts";
//import { useRtcStore } from "../../stores/rtc.ts";

interface ModalProps {
	changeModal: () => void;
	// 모달을 닫는 함수
}

// const langOptions = [
// 	{ key: 'C++', text: 'C++', value: 'C++' },
// 	{ key: 'Java', text: 'Java', value: 'Java' },
// 	{ key: 'Python', text: 'Python', value: 'Python' },
// 	{ key: 'JavaScript', text: 'JavaScript', value: 'JavaScript' },
// ];

export const WaitingPage = ({ changeModal }: ModalProps) => {
	const roomStore = useRoomStore();
	const rtcStore = useRtcStore();
	const myVideoRef = useRef<HTMLVideoElement | null>(null);
	const otherVideoRef = useRef<HTMLVideoElement | null>(null);
	const [volume, setVolume] = useState({ speaker: 50, microphone: 50 });
	const { speaker, microphone } = volume;
	// 스피커 볼륨, 마이크 볼륨
	const [difficulty, setDifficulty] = useState('LEVEL1');
	// 난이도
	//const rtcStore = useRtcStore();
	const difficulties = [
		{ label: '100m 달리기', value: 'LEVEL1', color: '#AAD79F'},
		{ label: '1km 달리기', value: 'LEVEL2', color: '#A9D9DC' },
		{ label: '10km 달리기', value: 'LEVEL3', color: '#E5ACAC' },
		{ label: '하프 마라톤', value: 'LEVEL4', color: '#C1ABE4' },
		{ label: '풀 마라톤', value: 'LEVEL5', color: '#9B9B9B' },
	];
	// 난이도 목록

	useEffect(() => {
		if (rtcStore.streamManager && myVideoRef.current) {
			rtcStore.streamManager.addVideoElement(myVideoRef.current);
		}
	}, [rtcStore.streamManager]);

	useEffect(() => {
		if(rtcStore.subscriber && otherVideoRef.current) {
			rtcStore.subscriber.addVideoElement(otherVideoRef.current);
		}
	}, [rtcStore.subscriber]);

	const handleVolume = (e: ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setVolume((prevState) => ({ ...prevState, [name]: value }));
	};
	// 슬라이더 수치 조정 함수

	const handleDifficulty = (_e: FormEvent<HTMLInputElement>, data: any) => {
		setDifficulty(data.value);
	}; // 난이도 설정 조정 함수

	const selectDifficulty = () => {
		if (roomStore.client === undefined || roomStore.client === null) {
			alert('문제가 발생했습니다. 재접속 해주세요.');
			return;
		}
		if (roomStore.roomInfo === undefined || roomStore.roomInfo === null) {
			alert('문제가 발생했습니다. 재접속 해주세요.');
			return;
		}

		roomStore.client.publish({
			destination: '/app/selectLevel',
			body: JSON.stringify({
				roomId: roomStore.roomInfo.roomId,
				level: difficulty,
			}),
		});
	};

	return (
		<>
			<div className='WaitingRoomBackground'>
				<div className='WaitingRoom'>
					<div className='Content'>
						<div className='Header HeaderSection'>
							{/* 상위 정보 메뉴 */}
							<Menu secondary size='large'>
								<MenuItem className='Header'>
									<img alt='URTurn' src={logo} style={{ width: '100px' }} />
								</MenuItem>
								<MenuItem name='Waiting Room'>
									<Header as='h3' textAlign='center'>
										Waiting Room
									</Header>
								</MenuItem>
								<MenuItem name='Entry Code'>
									<Header className='EntryCode' as='h3' textAlign='center'>
										입장 코드 : {roomStore.roomInfo?.entryCode}
									</Header>
								</MenuItem>
								{/* <MenuItem>
									<Header className='EntryCode' as='h3' textAlign='center'>
										선택 언어  :{' '}
										<Dropdown
											search
											defaultValue={langOptions[langOptions.length - 1].value}
											searchInput={{ type: 'string' }}
											options={langOptions}
										/>
									</Header>
								</MenuItem> */}
								<MenuItem name='close' position='right' onClick={changeModal}>
									<Icon className='Icon' name='close' size='large' />
								</MenuItem>
							</Menu>
						</div>
						<div className='AllSection'>
							<div className='RightSection'>
								{/* 프로필 영역 */}
								<CardGroup className='FitContent'>
									{/* 방장 */}
									<Card className='Card-without-border'>
										<CardContent
											style={{
												display: 'flex',
												flexDirection: 'column',
												alignItems: 'center',
												justifyContent: 'center',
											}}
										>
											<Image
												src={
													roomStore.userInfo?.myUserProfileUrl
														? roomStore.userInfo!.myUserProfileUrl
														: 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png'
												}
												size='small'
												rounded
											/>
											<Divider hidden />
											<CardHeader textAlign='center'>
												{roomStore.userInfo?.myUserNickName
													? roomStore.userInfo.myUserNickName
													: '내 정보 로딩 에러'}
											</CardHeader>
										</CardContent>
									</Card>
									{/* 입장 파트너 */}
									<Card className='Card-without-border'>
										<CardContent
											style={{
												display: 'flex',
												flexDirection: 'column',
												alignItems: 'center',
												justifyContent: 'center',
											}}
										>
											<Image
												src={
													roomStore.userInfo?.relativeUserProfileUrl
														? roomStore.userInfo.relativeUserProfileUrl
														: 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png'
												}
												size='small'
												rounded
											/>
											<Divider hidden />
											<CardHeader textAlign='center'>
												{roomStore.userInfo?.relativeUserNickName
													? roomStore.userInfo.relativeUserNickName
													: '미접속'}
											</CardHeader>
										</CardContent>
									</Card>
								</CardGroup>
								{/* 볼륨 조정 영역 */}
								<Header as='h2' textAlign='left' className='FitContent'>
									볼륨
								</Header>
								<Grid columns={2}>
									<GridRow>
										<GridColumn as={Form} width={9} verticalAlign={'middle'}>
											{/* 스피커 볼륨 */}
											<Form className='Volume'>
												<FormInput
													label={`스피커 볼륨: ${speaker}`}
													min={0}
													max={100}
													name='speaker'
													onChange={handleVolume}
													step={1}
													type='range'
												/>
											</Form>
										</GridColumn>
										<GridColumn width={2} verticalAlign={'middle'}>
											{/* 스피커 아이콘 */}
											<Icon className='Icon' name='volume up' size='big' />
										</GridColumn>
									</GridRow>
									<GridRow>
										<GridColumn as={Form} width={9} verticalAlign={'middle'}>
											{/* 마이크 볼륨 */}
											<Form className='Volume'>
												<FormInput
													label={`마이크 볼륨: ${microphone}`}
													min={0}
													max={100}
													name='microphone'
													onChange={handleVolume}
													step={1}
													type='range'
													value={microphone}
												/>
											</Form>
										</GridColumn>
										<GridColumn width={2} verticalAlign={'middle'}>
											{/* 마이크 아이콘 */}
											<Icon className='Icon' name='microphone' size='big' />
										</GridColumn>
									</GridRow>
								</Grid>
							</div>
							<div className='LeftSection'>
								<div>
									<Form>
										{/* 난이도 선택 헤더 */}
										<Header as='h2' textAlign='center'>
											난이도 선택
										</Header>
										{/* 난이도 버튼 그룹 */}
										{difficulties.map((item) => (
											<FormField key={item.value}>
												<Segment
													size='small'
													style={{
														backgroundColor:
															difficulty === item.value ? item.color : 'transparent',
														border: difficulty === item.value ? 'none' : undefined,
														borderRadius: '10px',
													}}
												>
													<Radio
														label={item.label}
														name='difficulties group'
														value={item.value}
														checked={difficulty === item.value}
														onChange={handleDifficulty}
														className={difficulty === item.value ? 'RadioChecked' : ''}
													/>
												</Segment>
											</FormField>
										))}
									</Form>
								</div>
								{/* 시작 버튼 */}
								<div className='StartButton'>
									<Button
										onClick={selectDifficulty}
										className='StartButtonStyle'
										style={{ width: '11.5vw', height: '8vh', fontSize: '1.1rem' }}
									>
										시작하기
									</Button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<video autoPlay={true} ref={myVideoRef} style={{display: 'none'}}/>
				<video autoPlay={true} ref={otherVideoRef} style={{display: 'none'}}/>
			</div>
		</>
	);
};

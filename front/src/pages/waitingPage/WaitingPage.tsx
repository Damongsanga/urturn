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
	Checkbox,
	FormField,
	Segment,
	Button,
} from 'semantic-ui-react';
import { ChangeEvent, FormEvent, useState } from 'react';
import logo from '../../assets/images/logo.svg';
import './WaitingPage.css';

import { useRoomStore } from '../../stores/room';

interface ModalProps {
	changeModal: () => void;
	// 모달을 닫는 함수
} 

export const WaitingPage = ({changeModal} : ModalProps ) => {

	const roomStore = useRoomStore();

	const [volume, setVolume] = useState({ speaker: 50, microphone: 50 });
	const { speaker, microphone } = volume;
	// 스피커 볼륨, 마이크 볼륨
	const [difficulty , setDifficulty] = useState('100m');
	// 난이도

	const difficulties = [
		{ label: '100m 달리기', value: '100m', color: '#AAD79F'},
		{ label: '1km 달리기', value: '1km', color: '#A9D9DC' },
		{ label: '10km 달리기', value: '10km', color: '#E5ACAC' },
		{ label: '하프 마라톤', value: '하프 마라톤', color: '#C1ABE4' },
		{ label: '풀 마라톤', value: '풀 마라톤', color: '#9B9B9B' },
	];
	// 난이도 목록

	const handleVolume = (e: ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setVolume((prevState) => ({ ...prevState, [name]: value }));
	};
	// 슬라이더 수치 조정 함수

	const handleDifficulty = (_e: FormEvent<HTMLInputElement>, data: any) => {
		setDifficulty(data.value);
	}; // 난이도 설정 조정 함수

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
									<Header as='h3' textAlign='center'>
										입장 코드 : {'A206'}
									</Header>
								</MenuItem>
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
										<CardContent>
											<Image
												src={
													roomStore.players[0].profileImgUrl?
													roomStore.players[0].profileImgUrl
													:
													'https://avatars.githubusercontent.com/u/19562994?v=4'
												}
												size='small'
												rounded
											/>
											<Divider hidden />
											<CardHeader textAlign='center'>페르디</CardHeader>
										</CardContent>
									</Card>
									{/* 입장 파트너 */}
									<Card className='Card-without-border'>
										<CardContent>
											<Image
												src='https://shiftpsh-blog.s3.amazonaws.com/uploads/2022/04/listing216.png'
												size='small'
												rounded
											/>
											<Divider hidden />
											<CardHeader textAlign='center'>한별이</CardHeader>
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
													min={1}
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
													min={1}
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
												<Segment size='small' style={{ backgroundColor:item.color }}>
													<Checkbox
														label={item.label}
														name='difficulties group'
														value={item.value}
														checked={difficulty === item.value}
														onChange={handleDifficulty}
													/>
												</Segment>
											</FormField>
										))}
									</Form>
								</div>
								{/* 시작 버튼 */}
								<div className='StartButton'>
									<Button size='massive' className='StartButtonStyle'>시작하기</Button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</>
	);
};

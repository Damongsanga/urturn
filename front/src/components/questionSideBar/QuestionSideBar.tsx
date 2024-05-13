import { useState, ChangeEvent } from "react";
import { Menu, Popup, Header, Grid, GridRow, GridColumn, FormInput, Icon, MenuItem, Form } from "semantic-ui-react";
import './QuestionSideBar.css'
import {useRoomStore} from "../../stores/room.ts";

export const QuestionSideBar = () => {
  const [volume, setVolume] = useState({ speaker: 50, microphone: 50 });
	const { speaker, microphone } = volume;
	const room = useRoomStore();
	const handleVolume = (e: ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setVolume((prevState) => ({ ...prevState, [name]: value }));
	};
  
	return (
		<>
			<Menu secondary icon vertical borderless style={{ marginTop: 'auto', backgroundColor: 'transparent' }}>
				<Popup
					className='PopUp'
					position='right center'
					// pop 내용
					content={
						<>
							<Header as='h2' textAlign='left' className='FitContent'>
								볼륨
							</Header>
							<Grid columns={2}>
								<GridRow className='Row'>
									<GridColumn as={Form} width={9} verticalAlign={'middle'}>
										{/* 스피커 볼륨 */}
										<Form className='FooterVolume'>
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
								<GridRow className='Row'>
									<GridColumn as={Form} width={9} verticalAlign={'middle'}>
										{/* 마이크 볼륨 */}
										<Form className='FooterVolume'>
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
						</>
					}
					on='click'
					pinned
					// pop을 띄울 버튼
					trigger={
						<MenuItem name='PopupVolume'>
							<Icon
								className='Mic'
								name='microphone'
								size='big'
								color='blue'
								onClick={() => console.log('test')}
							/>
						</MenuItem>
					}
				/>
				<MenuItem name='Profile'>
					<img alt='profile' src={room.userInfo?.myUserProfileUrl} />
				</MenuItem>
				<MenuItem name='Profile'>
					<img
						alt='profile'
						src={room.userInfo?.relativeUserProfileUrl}
						style={{ marginBottom: '5vh' }}
					/>
				</MenuItem>
			</Menu>
		</>
	);
};

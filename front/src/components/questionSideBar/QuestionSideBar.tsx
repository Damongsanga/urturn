import { Menu, Popup, Header, Grid, GridRow, GridColumn,  Icon, MenuItem  } from "semantic-ui-react";
import './QuestionSideBar.css'
import { useRoomStore } from "../../stores/room";
//import { useRtcStore } from "../../stores/rtc";

// const options = [
// 	{ key: 1, text: 'Option 1', value: 1 },
// 	{ key: 2, text: 'Option 2', value: 2 },
// 	// 추가 옵션...
//   ];

export const QuestionSideBar = () => {
	const roomStore = useRoomStore();
	//const rtcStore = useRtcStore();

	// const sendEmoji = (emoji: number) => {
	// 	rtcStore.getOpenVidu()?.session.signal({
	// 		type: 'emoji',
	// 		data: String(emoji),
	// 	})
	// }
  
	return (
		<>
			<Menu secondary icon vertical borderless style={{ marginTop: 'auto' ,backgroundColor: 'transparent' }}>
				<Popup
					className='PopUp'
					position='right center'
					// pop 내용
					content={
						<>
							<Header as='h2' textAlign='left' className='FitContent'>
								볼륨
							</Header>
							<Grid columns={1} style={{ marginLeft : '-2em'}}>
								<GridRow className='Row'>
									<GridColumn width={4} verticalAlign={'middle'} >
										{/* 스피커 아이콘 */}
										<Icon className='Icon' name='volume up' size='big' />
									</GridColumn>
									<GridColumn width={4} verticalAlign={'middle'} style={{ marginLeft : '2em'}}>
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
								color='white'
							/>
						</MenuItem>
					}
				/>
				<MenuItem name='Profile'>
					<img alt='profile' src={roomStore.getUserInfo()?.myUserProfileUrl} />
				</MenuItem>
				<MenuItem name='Profile'>
					<img
						alt='profile'
						src={roomStore.getUserInfo()?.relativeUserProfileUrl}
						style={{ marginBottom: '5vh' }}
					/>
				</MenuItem>
			</Menu>
		</>
	);
};

import {
	Button,
	Form,
	FormInput,
	Grid,
	GridColumn,
	GridRow,
	Header,
	Icon,
	Menu,
	MenuItem,
	Popup,
} from 'semantic-ui-react';
import './FooterBar.css';
import { useState, ChangeEvent } from 'react';

interface FooterProp {
	$mode?: number;
	$switch?: boolean;
}
/* 
헤더 규칙 prop
mode : 1이면 ide / 2이면 회고
$switch : 스위칭 모드이면 true, 아니면 false
*/
export const FooterBar = ({ $mode, $switch }: FooterProp) => {
	const [volume, setVolume] = useState({ speaker: 50, microphone: 50 });
	const { speaker, microphone } = volume;

	const handleVolume = (e: ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setVolume((prevState) => ({ ...prevState, [name]: value }));
	};
	// 슬라이더 수치 조정 함수

	return (
		<>
			<Menu className='FooterBar' borderless>
				{/* 프로필 사진 영역 */}
				{/* <MenuItem className='ProfileSection' name='Rounds' position='left'> */}
					{/* 유저 */}
					{/* <MenuItem name='Profile'>
						<img alt='profile' src='https://avatars.githubusercontent.com/u/19562994?v=4' />
					</MenuItem> */}
					{/* 파트너 유저 */}
					{/* <MenuItem name='Profile'>
						<img
							alt='profile'
							src='https://shiftpsh-blog.s3.amazonaws.com/uploads/2022/04/listing216.png'
						/>
					</MenuItem>
				</MenuItem> */}
				{/* 볼륨 조절 영역 */}
				{/* 스위치 상태가 아니고, ide에 필요한 음량 조절 버튼 */}
				{!$switch && $mode === 1 && (
					<Popup
						className='PopUp'
						position='top center'
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
							<MenuItem name='Rounds' style={{ marginLeft: '11vw' }}>
								<Icon
									className='Mic'
									name='microphone'
									size='big'
									style={{ margin: '0' }}
									onClick={() => console.log('test')}
								/>
							</MenuItem>
						}
					/>
				)}

				{/* ide에 사용할 버튼 */}
				{$mode === 1 && (
					<MenuItem name='RunButton' className='RunButton' position='right'>
						<Button size='large' className='RunButtonColor'>
							코드 실행
						</Button>
						<Button size='large' className='RunButtonColor'>
							코드 제출하기
						</Button>
					</MenuItem>
				)}

				{/* 회고에 사용할 자리 - 빈칸 용도 */}
				{$mode === 2 && (
					<MenuItem name='RunButton' className='RunButton' position='right'>
						<Button size='large' className='ReviewButtonColor'>
							GIT 업로드
						</Button>
						<Button size='large' className='ReviewButtonColor'>
							회고 저장하기
						</Button>
					</MenuItem>
				)}
			</Menu>
		</>
	);
};

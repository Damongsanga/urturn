import { Button, Header, Menu, MenuItem, MenuMenu } from 'semantic-ui-react';
import { useNavigate } from 'react-router-dom';
import { useLogout } from '../../utils/logout.ts';
import './HeaderBar.css';
import { useAuthStore } from "../../stores/useAuthStore.ts";
import { useRoomStore } from '../../stores/room.ts';
import { useEffect, useState } from 'react';

interface HeaderProp {
	$main?: boolean;
	$myPage?: boolean;
	$ide?: boolean;
	$review? : boolean;
	$mode?: number;
}
/* 
헤더 규칙 prop
main : 메인용 헤더일시 true
myPage : 마이페이지 헤더일시 true
ide : ide용 헤더일시 true
review : review용 헤더일시 true
mode : 0이면 문제 확인 모드, 1이면 스위칭 모드 / 2이면 페어 프로그래밍 모드

main과 ide는 상태가 달라야함
*/
export const HeaderBar = ({ $main, $myPage, $ide, $review, $mode }: HeaderProp) => {
	const logout = useLogout();
	const navigate = useNavigate();
	const auth = useAuthStore();
	const roomStore = useRoomStore();

	const goToMyPage = () => {
		navigate('/myPage');
	};

	const goToMain = () => {
		navigate('/main');
	};

	const [min, setMin] = useState('00');
	const [sec, setSec] = useState('00');
	useEffect(() => {
		if(roomStore.sec < 60){
			setMin('00');
			setSec(roomStore.sec.toString().padStart(2, '0'));
		}
		else{
			setMin(Math.floor(roomStore.sec / 60).toString().padStart(2, '0'));
			setSec((roomStore.sec % 60).toString().padStart(2, '0'));
		}
	}, [roomStore.sec])

	// uri
	// const gitRepo = () => {
	// 	location.href = `https://github.com/login/oauth/authorize?client_id=a82095fde8aa68bb396d&scope=repo&redirect_uri=http://localhost:5173/auth/github/upload`;
	// };
	//const gitRepo = () => {
	//     location.href = `https://github.com/login/oauth/authorize?client_id=a82095fde8aa68bb396d&scope=repo&redirect_uri=http://localhost:5173/auth/github/upload`;
	// };
	return (
		<>
			{/* 메인, 마이 	페이지 헤더 */}
			{($main || $myPage) && (
				<div className='HeaderBar Main'>
					<Menu className='HeaderBar Main'>
						<MenuItem
							className='HeaderAlign'
							onClick={() => {
								goToMain();
							}}
						>
							<Header as='h2' textAlign='center' className='FontColor'>
								URTurn
							</Header>
						</MenuItem>
						<MenuMenu position='right' className='ProfileAlign'>
							<MenuItem
								name='Profile'
								onClick={() => {
									goToMyPage();
								}}
							>
								<img alt='profile' src={auth.profileImage} style ={{ width: 'auto', height: '55px'}}/>
							</MenuItem>
							<MenuItem name='Welcome'>
								<Header as='h3' textAlign='center' className='FontColor'>
									{auth.nickname}
									{'님 환영합니다.'}
								</Header>
							</MenuItem>
							<MenuItem name='QuitButton' className='QuitButton'>
								<Button size='large' className='ButtonColor' onClick={logout}>
									로그아웃
								</Button>
								{/*<Button size='large' className='ButtonColor' onClick={gitRepo}>*/}
								{/*	repo test*/}
								{/*</Button>*/}
							</MenuItem>
						</MenuMenu>
					</Menu>
				</div>
			)}

			{/* Ide 헤더 */}
			{$ide && (
				<Menu className='HeaderBar Ide' borderless>
					<MenuItem className='HeaderAlign'

						style={
							$mode === 0? { marginLeft: '0vw' } :
							$mode === 1?{marginLeft: '46vw' }:
							$mode === 2?{marginLeft: '44vw' }:
							{marginLeft: '0vw'}
						}
					>
						<Header as='h2' textAlign='center' className='FontColor'>
							{
								$mode === 0? '문제 확인' :
								$mode === 1? '스위칭 모드' :
								$mode === 2? '페어 프로그래밍 모드' :
								'IDE'
							}
						</Header>
					</MenuItem>
					{
						$mode !== 0 &&
						<MenuMenu position='right'>
							<MenuItem name='Rounds'>
								<Header as='h3' textAlign='center' className='FontColor'>
									{min} : {sec}
								</Header>
							</MenuItem>
							<MenuItem name='Rounds'>
								<Header as='h3' textAlign='center' className='FontColor'>
									{'라운드 '}
									{roomStore.round}
								</Header>
							</MenuItem>
							<MenuItem name='QuitButton' className='QuitButton'>
								<Button size='large' className='ButtonColor'>
									포기하기
								</Button>
							</MenuItem>
						</MenuMenu>
					}
				</Menu>
			)}

			{/* 리뷰 헤더 */}
			{$review && (
				<Menu className='HeaderBar Ide' borderless>
					<MenuItem className='HeaderAlign'
						style={{ marginLeft: '45%' }}
					>
						<Header as='h2' textAlign='center' className='FontColor'>
							회고
						</Header>
					</MenuItem>
					<MenuMenu position='right'>
						<MenuItem name='QuitButton' className='QuitButton'>
							<Button size='large' className='ButtonColor'>
								나가기
							</Button>
						</MenuItem>
					</MenuMenu>
				</Menu>
			)}
		</>
	);
};

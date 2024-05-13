import { Button, Header, Menu, MenuItem, MenuMenu } from 'semantic-ui-react';
import { useNavigate } from 'react-router-dom';
import { useLogout } from '../../utils/logout.ts';
import './HeaderBar.css';
import { useAuthStore } from '../../stores/useAuthStore.ts';
import { useRoomStore } from '../../stores/room.ts';

interface HeaderProp {
	$main?: boolean;
	$myPage?: boolean;
	$ide?: boolean;
	$review?: boolean;
	$mode?: number;
}
/* 
헤더 규칙 prop
main : 메인용 헤더일시 true
myPage : 마이페이지 헤더일시 true
ide : ide용 헤더일시 true
review : review용 헤더일시 true
mode : 1이면 스위칭 모드 / 2이면 페어 프로그래밍 모드

main과 ide는 상태가 달라야함
*/
export const HeaderBar = ({ $main, $myPage, $ide, $review }: HeaderProp) => {
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

	// uri
	// const gitRepo = () => {
	// 	location.href = `https://github.com/login/oauth/authorize?client_id=a82095fde8aa68bb396d&scope=repo&redirect_uri=http://localhost:5173/auth/github/upload`;
	// };
	//const gitRepo = () => {
	//     location.href = `https://github.com/login/oauth/authorize?client_id=a82095fde8aa68bb396d&scope=repo&redirect_uri=http://localhost:5173/auth/github/upload`;
	// };
	return (
		<>
			{/* 메인 헤더 */}
			{$main && (
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
								<img alt='profile' src={auth.profileImage} />
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

			{/* 마이페이지 헤더 */}
			{$myPage && (
				<div className='HeaderBar Main'>
					<Menu className='HeaderBar Main'>
						<MenuItem
							style={{ marginLeft: '6vw' }}
							onClick={() => {
								goToMain();
							}}
						>
							<Header as='h2' textAlign='center' className='FontColor'>
								URTurn
							</Header>
						</MenuItem>
						<MenuMenu position='right' style={{ marginRight: '6vw' }}>
							<MenuItem
								name='Profile'
								onClick={() => {
									goToMyPage();
								}}
							>
								<img alt='profile' src={auth.profileImage} />
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
							</MenuItem>
						</MenuMenu>
					</Menu>
				</div>
			)}

			{/* Ide 헤더 */}
			{$ide && (
				<Menu className='HeaderBar Ide' borderless>
					<MenuItem className='HeaderAlign'>
						{/* <Header as='h2' textAlign='center' className='FontColor'>
							URTurn
						</Header> */}
					</MenuItem>
					<MenuMenu position='right'>
						<MenuItem name='Rounds'>
							<Header as='h3' textAlign='center' className='FontColor'>
								{roomStore.sec}
							</Header>
						</MenuItem>
						<MenuItem name='Rounds'>
							<Header as='h3' textAlign='center' className='FontColor'>
							{roomStore.round}<span style={{ marginLeft: '10px' }}></span>{'ROUND'}
							</Header>
						</MenuItem>
						<MenuItem name='QuitButton' className='QuitButton'>
							<Button size='large' className='ButtonColor'>
								포기하기
							</Button>
						</MenuItem>
					</MenuMenu>
				</Menu>
			)}

			{/* 리뷰 헤더 */}
			{$review && (
				<Menu className='HeaderBar Ide' borderless>
					<MenuItem className='HeaderAlign'>
						{/* <Header as='h2' textAlign='center' className='FontColor'>
							URTurn
						</Header> */}
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

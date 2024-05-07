import { Button, Header, Menu, MenuItem, MenuMenu } from 'semantic-ui-react';
import {useNavigate} from "react-router-dom";
import { useLogout } from '../../utils/logout.ts';
import './HeaderBar.css';
import { useAuthStore } from "../../stores/useAuthStore.ts";

interface HeaderProp {
	$main?: boolean;
	$ide?: boolean;
	$mode?: number;
}
/* 
헤더 규칙 prop
main : 메인용 헤더일시 true
ide : ide용 헤더일시 true
mode : 1이면 스위칭 모드 / 2이면 페어 프로그래밍 모드

main과 ide는 상태가 달라야함
*/
export const HeaderBar = ({ $main, $ide }: HeaderProp) => {
	const logout = useLogout();
	const navigate = useNavigate();
	const auth = useAuthStore();
	const goToMyPage = () => {
		navigate('/myPage');
	}

	const goToMain = () => {
		navigate('/main');
	}

	return (
		<>
			{/* 메인 헤더 */}
			{$main && (
				<div className='HeaderBar Main'>
					<Menu className='HeaderBar Main'>
						<MenuItem className='HeaderAlign' onClick={() => {goToMain()}}>
							<Header as='h2' textAlign='center' className='FontColor'>
								URTurn
							</Header>
						</MenuItem>
						<MenuMenu position='right'>
							<MenuItem name='Profile' onClick={() => {goToMyPage()}}>
								<img alt='profile' src='' />
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
								{'라운드'}
								{' 1 '}
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
		</>
	);
};

import { Button, Header } from 'semantic-ui-react';

import './HeaderBar.css';

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
export const HeaderBar = ({ $main, $ide, $mode }: HeaderProp) => {
	return (
		<>
		{/* 메인 헤더 */}
			{$main && (
				<div className='HeaderBar Main'>
					<div className='HeaderAlign'>
						<Header as='h2' textAlign='center' className='FontColor'>
							URTurn
						</Header>
					</div>
					<div className='HeaderAlign QuitButton'>
						<Button size='large' className='ButtonColor'>로그아웃</Button>
					</div>
				</div>
			)}

			{/* Ide 헤더 */}
			{$ide && (
				<div className='HeaderBar Ide'>
					<div className='HeaderAlign'>
						{/* 모드별 헤더 문구 변경 */}
						{$mode === 1 && (
							<Header as='h2' textAlign='center' className='FontColor'>
								스위칭 모드
							</Header>
						)}
						{$mode === 2 && (
							<Header as='h2' textAlign='center' className='FontColor'>
								페어 프로그래밍 모드
							</Header>
						)}
					</div>
					<div className='HeaderAlign QuitButton'>
						<Button size='large' className='ButtonColor'>포기하기</Button>
					</div>
				</div>
			)}
		</>
	);
};

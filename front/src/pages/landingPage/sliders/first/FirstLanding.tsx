import { MenuItem, Menu, Header, Icon, Button } from 'semantic-ui-react';
import logo from '../../../../assets/images/logo.svg';
import scroll from '../../../../assets/images/scrolldown.png';
import { TestLogin } from '../../TestLogin';

import './FirstLanding.css';

export const FirstLandingPage = () => {
	const githubAuth = () => {
		location.href = `https://github.com/login/oauth/authorize?client_id=${
			import.meta.env.VITE_GITHUB_CLIENT_ID
		}&redirect_uri=${import.meta.env.VITE_GITHUB_REDIRECT_URI}`;
	};
	//&response_type=code

	return (
		<>
			<div className='Entire'>
				<div className='LandingPage'>
					<div className='Content'>
						{/* 헤더 */}
						<div className='Navigation'>
							<Menu secondary size='large'>
								<MenuItem>
									<img alt='URTurn' src={logo} style={{ width: '125px' }} />
								</MenuItem>
								<MenuItem name='Waiting Room' position='right'>
									<Button circular className='ButtonColor' onClick={githubAuth}>
										<Icon name='github' size='big' /> 로그인
									</Button>
								</MenuItem>
							</Menu>
						</div>

						{/* 동영상 영역 */}
						<div className='Video'>
							<iframe
								width='560'
								height='315'
								src='https://www.youtube.com/embed/rub7vg6YG7k?si=MRm32a7ITC80DZWu'
								title='YouTube video player'
								frameBorder='0'
								allow='accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share'
								referrerPolicy='strict-origin-when-cross-origin'
								allowFullScreen
							></iframe>
						</div>

						{/* CTA 문구 & 버튼 영역 */}
						<div className='CTA'>
							<Header as='h3' textAlign='left' style={{ marginBottom: '-1vh' }}>
								URTurn으로 코드를 넘어서
							</Header>
							<Header as='h3' textAlign='left' style={{ marginBottom: '-1vh' }}>
								당신의 개발 역량을 한 단계 업그레이드하세요.
							</Header>
							<Header as='h3' textAlign='left' style={{ marginBottom: '2vh' }}>
								지금 바로 시작해보세요!
							</Header>
							<Button circular className='ButtonColor' onClick={githubAuth}>
								<Icon name='github' size='huge' /> <span className='FontSize'>Github로 시작하기</span>
							</Button>
						</div>
						<div className='ScrollDown'>
							<img alt='scroll' src={scroll} style={{ width: '80px' }} />
							<TestLogin />
						</div>
					</div>
				</div>
			</div>
		</>
	);
};
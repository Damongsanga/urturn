import { MenuItem, Menu, Header, Icon, Button, Image } from 'semantic-ui-react';
import { TestLogin } from '../../TestLogin';
import logo from '../../../../assets/images/logo.svg';
import scroll from '../../../../assets/images/scrolldown.png';
import pair from '../../../../assets/images/pair.png';

import './FirstLanding.css';

export const FirstLandingPage = () => {
	const githubAuth = () => {
		location.href = `https://github.com/login/oauth/authorize?client_id=a82095fde8aa68bb396d&scope=user:email&redirect_uri=${
			import.meta.env.VITE_GITHUB_REDIRECT_URI
		}`;
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
									<Button circular className='ButtonColor GitButton' onClick={githubAuth}>
										<div className='InnerButton'>
											<Icon name='github' size='big' />
											<span className='FontSize'>로그인</span>
										</div>
									</Button>
								</MenuItem>
							</Menu>
						</div>

						<div className='Information'>
							{/* CTA 문구 & 버튼 영역 */}
							<div className='CTA'>
								<Header as='h1' textAlign='left' style={{ marginBottom: '5vh' }}>
									Unlock Potential, Code Awaits <br />
									Now is Your Turn
								</Header>
								<p style={{ marginBottom: '5vh', textAlign: 'left' }}>
									URTurn이
									당신의 개발 역량을 업그레이드 할 수 있도록 도와드립니다.
								</p>
								<Button circular className='ButtonColor GitButton' onClick={githubAuth}>
									<div className='InnerButton'>
										<Icon name='github' size='huge' />
										<span className='FontSize'>Github로 시작하기</span>
									</div>
								</Button>
							</div>
							<div>
								<Image src={pair} size='big'></Image>
							</div>
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

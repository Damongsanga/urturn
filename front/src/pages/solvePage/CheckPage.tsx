import { Allotment } from 'allotment';
//import CodeEditor from '../../components/solve/CodeEditor';
import Markdown from 'markdown-to-jsx';
import { Menu, MenuItem, Table, TableBody, TableCell, TableHeader, TableHeaderCell, TableRow } from 'semantic-ui-react';

import 'allotment/dist/style.css';

import { HeaderBar } from '../../components/header/HeaderBar';

import { useRoomStore } from '../../stores/room';

import './SolvePage.css';
import { FooterBar } from '../../components/footer/FooterBar';
import { useEffect, useState } from 'react';
import { QuestionSideBar } from '../../components/questionSideBar/QuestionSideBar';
import { useEmojiStore } from '../../stores/emoji';

export default function CheckPage() {
	const roomStore = useRoomStore();
	const emojiStore = useEmojiStore();

	const [activeQuestion, setActiveQuestion] = useState(1);

	useEffect(() => {
		setActiveQuestion(1);
		handleResize();
		window.addEventListener("resize", handleResize, false);
	}, []);
	const handleResize = () => {
		const top = document.getElementById('mdSection')?.getBoundingClientRect().top;
		const left = document.getElementById('mdSection')?.getBoundingClientRect().left;
		const width = document.getElementById('mdSection')?.getBoundingClientRect().width;
		const height = document.getElementById('mdSection')?.getBoundingClientRect().height;
		if(top && left && width && height)
			emojiStore.setMdInContainerInfo({ top, left, width, height });
	}

	return (
		<div className='Page'>
			<HeaderBar $ide={true} $mode={0} />
			<div>
				<div style={{ display: 'flex', height: 'calc(100vh - 140px)', width: '100vw' }}>
					{/* 문제 사이드바 */}
					<div className='QuestionSideBars'>
						<Menu
							secondary
							icon
							vertical
							borderless
							style={{ marginTop: '10vh', backgroundColor: 'transparent' }}
						>
							<MenuItem
								className='QuestionButton'
								name='1'
								active={activeQuestion === 1}
								onClick={() => {
									setActiveQuestion(1);
								}}
								style={{ marginBottom: '5vh' }}
							></MenuItem>

							<MenuItem
								className='QuestionButton'
								name='2'
								onClick={() => {
									setActiveQuestion(2);
								}}
								active={activeQuestion === 2}
							></MenuItem>
						</Menu>
						{/* 마이크, 참여자 프로필 컴포넌트 */}
						<QuestionSideBar></QuestionSideBar>
					</div>
					<Allotment>
						<Allotment.Pane minSize={350}>
							<div className='HeaderBar' style={{ height: '70px' }}>
								<div
									style={{
										width: '98%',
										textAlign: 'left',
										fontSize: '20px',
										fontWeight: 'bold',
										color: 'white',
									}}
								>
									{roomStore.questionInfos &&
										activeQuestion > 0 &&
										roomStore.questionInfos[activeQuestion - 1].algoQuestionTitle}
								</div>
							</div>
							<div id='mdSection' style={{ height: '100%', overflowY: 'auto', padding: '12px', fontSize: '1.3rem' }}>
								{roomStore.questionInfos && activeQuestion > 0 && (
									<Markdown>
										{roomStore.questionInfos[activeQuestion - 1].algoQuestionContent}
									</Markdown>
								)}
								<hr />
								<div className='testcase'>
									<Table>
										<TableHeader>
											<TableRow>
												{/* <TableHeaderCell /> */}
												<TableHeaderCell>입력값</TableHeaderCell>
												<TableHeaderCell>출력값</TableHeaderCell>
											</TableRow>
										</TableHeader>

										<TableBody>
											{roomStore.questionInfos &&
												activeQuestion > 0 &&
												roomStore.questionInfos[activeQuestion - 1].testcases.map(
													(testcase, i) => (
														<TableRow key={i}>
															{/* <TableCell>{i + 1} 번 테스트 케이스</TableCell> */}
															<TableCell>{testcase.stdin}</TableCell>
															<TableCell style={{ verticalAlign: 'top' }}>
																{testcase.expectedOutput}
															</TableCell>
														</TableRow>
													),
												)}
										</TableBody>
									</Table>
								</div>
								<br />
								<br />
								<br />
								<br />
							</div>
						</Allotment.Pane>
						<Allotment.Pane minSize={350}>
							<div className='HeaderBar' style={{ height: '70px' }}>
								{/* <div
									style={{
										width: '98%',
										textAlign: 'left',
										fontSize: '20px',
										fontWeight: 'bold',
										color: 'white',
									}}
								>
								</div> */}
							</div>
							<Allotment vertical>
								<Allotment.Pane minSize={325}>
									<div style={{ height: '100%', width: '100%' }}>
										<div
											style={{
												padding: '12px',
												fontSize: '1.3rem',
												overflowY: 'scroll',
												height: '100%',
											}}
										>
											<p className='CheckText'>현재 페이지는 문제 읽기 페이지입니다!</p>
											<p className='regularText CheckText'>두 문제를 충분히 읽고 어떻게 문제를 풀지 고민해보세요.</p>
											<p className='regularText CheckText'>준비가 완료되었으면 준비완료 버튼을 눌러주세요!</p>
											<br />
											<p className='CheckText'>1. 릴레이 모드 (음성 불가, 이모티콘 가능)</p>
											<p className='regularText CheckText'>
												릴레이 모드에서는 라운드가 지날 때마다 주어진 문제와 코드가 서로
												뒤바뀌게 됩니다.
											</p>
											<p className='regularText CheckText'>페어에게 이어 받은 코드를 해석하여 문제를 풀어보세요.</p>
											<br />
											<p className='CheckText'>2. 페어 프로그래밍 모드 (음성, 이모티콘 가능)</p>
											<p className='regularText CheckText'>한 문제를 완료한다면 페어 프로그래밍 모드로 전환됩니다.</p>
											<p className='regularText CheckText'>번갈아가며 코드를 작성하며 남은 문제를 함께 완수하세요.</p>
											<p className='regularText CheckText'>해당 모드에서는 음성 소통할 수 있습니다.</p>
											<br />
											<p className='CheckText'>3. 회고</p>
											<p className='regularText CheckText'>회고를 진행한 후 깃허브 레포지토리에 편하게 업로드하세요!</p>

											<br />
											<p className='CheckText'>TIP!</p>
											<p className='regularText CheckText'>음성 채팅과 이모티콘 기능을 활용하여 소통하세요.</p>
											<p className='regularText CheckText'>페어가 쉽게 이해할 수 있도록 가독성 좋은 코드를 작성해보세요.</p>
										</div>
										{/* <CodeEditor/> */}
									</div>
								</Allotment.Pane>
								<Allotment.Pane minSize={125}>
									<div className='ReviewBar' style={{ height: '50px', backgroundColor: '#000034' }}>
										<div
											style={{
												width: '98%',
												textAlign: 'left',
												fontSize: '20px',
												fontWeight: 'bold',
												color: 'white',
											}}
										></div>
									</div>
									<div
										style={{
											padding: '12px',
											fontSize: '1.3rem',
											overflowY: 'scroll',
											height: '100%',
										}}
									>
										<p>두 문제를 모두 확인했으면, 준비하기 버튼을 눌러주세요.</p>
									</div>
								</Allotment.Pane>
							</Allotment>
						</Allotment.Pane>
					</Allotment>
				</div>
			</div>
			{/* footer */}
			<FooterBar $mode={0} />
		</div>
	);
}

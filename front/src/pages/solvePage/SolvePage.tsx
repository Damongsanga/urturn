import { Allotment } from 'allotment';
import CodeEditor from '../../components/solve/CodeEditor';
import Markdown from 'markdown-to-jsx';
import { Dropdown, Table, TableBody, TableHeader, TableHeaderCell, TableRow } from 'semantic-ui-react';

import 'allotment/dist/style.css';

import { HeaderBar } from '../../components/header/HeaderBar';

import { useRoomStore } from '../../stores/room';

import './SolvePage.css';
import { QuestionSideBar } from '../../components/questionSideBar/QuestionSideBar';
import { FooterBar } from '../../components/footer/FooterBar';
import { langOptions } from '../../types/lagnOptions';

export default function SolvePage() {
	const roomStore = useRoomStore();

	return (
		<div className='Page'>
			<HeaderBar $ide={true} $mode={1} />
			<div>
				<div style={{ display: 'flex', height: 'calc(100vh - 140px)', width: '100vw' }}>
					{/* 문제 사이드바 */}
					<div className='QuestionSideBars'>
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
									{roomStore.questionIdx == -1 ? (
										<div></div>
									) : (
										<Markdown>
											{roomStore.questionInfos![roomStore.questionIdx].algoQuestionTitle}
										</Markdown>
									)}
								</div>
							</div>
							<div style={{ height: '100%', overflowY: 'auto', padding: '12px', fontSize:'1.3rem' }}>
								{roomStore.questionIdx == -1 ? (
									<div></div>
								) : (
									<Markdown>
										{roomStore.questionInfos![roomStore.questionIdx].algoQuestionContent}
									</Markdown>
								)}
								<Table definition>
									<TableHeader>
										<TableRow>
											<TableHeaderCell />
											<TableHeaderCell>입력값</TableHeaderCell>
											<TableHeaderCell>출력값</TableHeaderCell>
										</TableRow>
									</TableHeader>

									<TableBody>
										{/* {
											roomStore.questionInfos &&
												roomStore.questionInfos[roomStore.questionIdx].testcases.map((testcase, i) => (
													<TableRow key={i}>
														<TableCell>{i + 1} 번 테스트 케이스</TableCell>
														<TableCell>{testcase.stdin}</TableCell>
														<TableCell>{testcase.expectedOutput}</TableCell>
													</TableRow>
												))
										} */}
									</TableBody>
								</Table>
							</div>
						</Allotment.Pane>
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
									<Dropdown
										search
										value={roomStore.lang}
										searchInput={{ type: 'string' }}
										options={langOptions}
										onChange={(_e, { value }) => {
											roomStore.setLang(value as string);
										}}
									/>
								</div>
							</div>
							<Allotment vertical>
								<Allotment.Pane minSize={325}>
									<div style={{ height: '100%', width: '100%' }}>
										<CodeEditor />
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
									<div style={{ padding: '12px' }}>
										<p style={{ whiteSpace: 'pre-wrap' }}>{roomStore.console}</p>
									</div>
								</Allotment.Pane>
							</Allotment>
						</Allotment.Pane>
					</Allotment>
				</div>
			</div>
			{/* footer */}
			<FooterBar $mode={1} $pairMode={false} />
		</div>
	);
}

// <div>
//       <Dropdown
//           search
//           defaultValue={langOptions[0].value}
//           searchInput={{ type: 'string' }}
//           selection
//           options={langOptions}
//           style={{ backgroundColor: 'green' }}
//         />
//       </div>

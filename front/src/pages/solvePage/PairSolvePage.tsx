import { Allotment } from 'allotment';
import CodeEditor from '../../components/solve/CodeEditor';
import Markdown from 'markdown-to-jsx';
import { Dropdown, Table, TableBody, TableCell, TableHeader, TableHeaderCell, TableRow } from 'semantic-ui-react';

import 'allotment/dist/style.css';

import { HeaderBar } from '../../components/header/HeaderBar';

import { useRoomStore } from '../../stores/room';

import './SolvePage.css';
import { QuestionSideBar } from '../../components/questionSideBar/QuestionSideBar';
import { FooterBar } from '../../components/footer/FooterBar';
import { useEffect } from 'react';

const langOptions = [
	{ key: 'C++', text: 'C++', value: 'c++' },
	{ key: 'Java', text: 'Java', value: 'java' },
	{ key: 'Python', text: 'Python', value: 'python' },
	{ key: 'JavaScript', text: 'JavaScript', value: 'javascript' },
];

export default function PairSolvePage() {
	const roomStore = useRoomStore();

	useEffect(() => {
		roomStore.setLang('c++');
	}, []);

	return (
		<div className='Page'>
			<HeaderBar $ide={true} $mode={2} />
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
							<div style={{ height: '100%', overflowY: 'auto', padding: '12px' }}>
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
										<TableRow>
											<TableCell width={4}>테스트 케이스 1</TableCell>
											<TableCell>입격값1</TableCell>
											<TableCell>출력값1</TableCell>
										</TableRow>
										<TableRow>
											<TableCell width={4}>테스트 케이스 2</TableCell>
											<TableCell>입력값2</TableCell>
											<TableCell>출력값2</TableCell>
										</TableRow>
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
										defaultValue={langOptions[0].value}
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
			<FooterBar $mode={1} />
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

import { Allotment } from "allotment";
import CodeEditor from '../../components/solve/CodeEditor';
import Markdown from 'markdown-to-jsx'
import { Dropdown, Menu, MenuItem } from 'semantic-ui-react'

import "allotment/dist/style.css";

import { HeaderBar } from '../../components/header/HeaderBar';

import { useRoomStore } from '../../stores/room';

import './SolvePage.css'
import { FooterBar } from "../../components/footer/FooterBar";
import { useEffect, useState } from "react";
import { QuestionSideBar } from "../../components/questionSideBar/QuestionSideBar";

const langOptions = [
  { key: 'C++', text: 'C++', value: 'C++' },
  { key: 'Java', text: 'Java', value: 'Java' },
  { key: 'Python', text: 'Python', value: 'Python' },
  { key: 'JavaScript', text: 'JavaScript', value: 'JavaScript' },
]

export default function CheckPage() {
    const roomStore = useRoomStore();

    const [activeQuestion, setActiveQuestion] = useState(1);

	useEffect(() => {
		setActiveQuestion(1);
	}, [])

    return (
      <div className='Page'>
			<HeaderBar $ide={true} $mode={1} />
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
								onClick={() => {setActiveQuestion(1); }}
								style={{ marginBottom: '5vh' }}
							></MenuItem>

							<MenuItem
								className='QuestionButton'
								name='2'
								onClick={() => {setActiveQuestion(2);}}
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
									{roomStore.questionInfos && activeQuestion > 0 && roomStore.questionInfos[activeQuestion - 1].algoQuestionTitle}
								</div>
							</div>
							<div style={{ height: '100%', overflowY: 'auto' }}>
              {
                roomStore.questionInfos && activeQuestion > 0 &&
                <Markdown>{roomStore.questionInfos[activeQuestion - 1].algoQuestionContent}</Markdown>
              }
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
									/>
								</div>
							</div>
							<Allotment vertical>
								<Allotment.Pane minSize={325}>
									<div style={{ height: '100%', width: '100%' }}>
										<CodeEditor/>
									</div>
								</Allotment.Pane>
								<Allotment.Pane minSize={125}>
									<div className='ReviewBar' style={{ height: '50px', backgroundColor: '#F2CAB3' }}>
										<div
											style={{
												width: '98%',
												textAlign: 'left',
												fontSize: '20px',
												fontWeight: 'bold',
												color: 'white',
											}}
										>
										</div>
									</div>
									<div>
                        <p>
                          두 문제를 모두 확인했으면, 시작하기 버튼을 눌러주세요.
                        </p>
									</div>
								</Allotment.Pane>
							</Allotment>
						</Allotment.Pane>
					</Allotment>
				</div>
			</div>
			{/* footer */}
			<FooterBar $mode={0}/>
		</div>
    );
  }


//import { useEffect, useState } from 'react';

import { Allotment } from "allotment";
import CodeEditor from '../../components/solve/CodeEditor';
import Markdown from 'markdown-to-jsx'
import { Button, Dropdown } from 'semantic-ui-react'

import "allotment/dist/style.css";

import { HeaderBar } from '../../components/header/HeaderBar';
import { SolveSubHeader } from '../../components/solve/sloveSubHeader';

import { useRoomStore } from '../../stores/room';

import './SolvePage.css'

const langOptions = [
  { key: 'C++', text: 'C++', value: 'C++' },
  { key: 'Java', text: 'Java', value: 'Java' },
  { key: 'Python', text: 'Python', value: 'Python' },
  { key: 'JavaScript', text: 'JavaScript', value: 'JavaScript' },
]

export default function CheckPage() {
    const roomStore = useRoomStore();

    const readyToSolve = () =>{
      const idx = roomStore.roomInfo?.host ? 0 : 1
      console.log(roomStore.roomInfo?.roomId)
      console.log(roomStore.questionInfos?.[idx]?.algoQuestionId);
      console.log(roomStore.roomInfo?.host)

      roomStore.client?.publish({
        destination: '/app/readyToSolve',
        body: JSON.stringify({
          roomId: roomStore.roomInfo?.roomId,
          algoQuestionId: roomStore.questionInfos?.[idx]?.algoQuestionId,
          isHost: roomStore.roomInfo?.host
        })

      })
    }


    return (
      <div className='Page'>

          <HeaderBar $ide={true} $mode={1}/>
          
          <SolveSubHeader $mode={1}/>
        
        <div style={{height: '80vh' }}>
        <Allotment>
          <Allotment.Pane minSize={350}>
            <div className='HeaderBar Ide' style={{ height: '70px'}}>
              <div style={{width: '98%', textAlign: 'left', fontSize: '20px', fontWeight: 'bold', color: 'white'}}>
              양과 늑대
              </div>

            </div>
            <div style={{ height: '100%', overflowY: 'auto'  }}>
              <Markdown>{roomStore.questionInfos![0].algoQuestionContent}</Markdown>
            </div>
          </Allotment.Pane>
          <Allotment.Pane minSize={350}>
            <div className='HeaderBar Ide' style={{ height: '70px'}}>
              <div style={{width: '98%', textAlign: 'left', fontSize: '20px', fontWeight: 'bold', color: 'white'}}>
              <Dropdown
                search
                defaultValue={langOptions[0].value}
                searchInput={{ type: 'string' }}
                options={langOptions}
              />
              </div> 
            </div>
            <Allotment vertical>
              <Allotment.Pane minSize={350}>
                <div style={{ height: '100%', width: '100%' }}>
                  <CodeEditor lang="javascript" />
                </div>
              </Allotment.Pane>
              <Allotment.Pane minSize={100}>
                <div>CONSOLE</div>
              </Allotment.Pane>
            </Allotment>
          </Allotment.Pane>
        </Allotment>
        </div>

        <Button onClick={readyToSolve}>
          시작하기
        </Button>
  
      </div>
    );
  }


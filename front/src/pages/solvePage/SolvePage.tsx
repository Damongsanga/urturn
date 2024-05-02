import { useEffect, useState } from 'react';

import { Allotment } from "allotment";
import CodeEditor from '../../components/solve/CodeEditor';
import Markdown from 'markdown-to-jsx'
import { Dropdown } from 'semantic-ui-react'

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

export default function SolvePage() {
    const roomStore = useRoomStore();

    const [fileContent, setFileContent] = useState('');

    const [nowIdxState, setNowIdxState] = useState(-1);
    //const nowIdxRef = useRef(-1);

    useEffect(() => {
        if(roomStore.roomInfo?.host==true){
          setNowIdxState(0);
          //nowIdxRef.current = 0;
        }
        else{
          setNowIdxState(1);
          //nowIdxRef.current = 1;
        }
        console.log("페이지가 바뀌엇어오")
        if(roomStore.questionInfos){
          console.log("퀘스천 인포: " + roomStore.questionInfos[0].algoQuestionUrl);
        }
        console.log("인덱스:" + nowIdxState);
    }, [roomStore.questionInfos]);

    useEffect(() => {
      const loadMarkdown = async () => {
        if(roomStore.questionInfos){
          const txt = await loadMarkdownFromCDN(roomStore.questionInfos[nowIdxState].algoQuestionUrl);
          console.log("변환결과?!!?!???????????: ", txt);
          setFileContent(txt);
        }
      };

      loadMarkdown();
    }, [nowIdxState]);

    async function loadMarkdownFromCDN(url: string): Promise<string> {
      try {
        const response = await fetch(url);
        if (!response.ok) {
          throw new Error(`Error fetching Markdown file: ${response.statusText}`);
        }
        const markdown = await response.text();
        return markdown;
      } catch (error) {
        console.error(error);
        return '';
      }
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
              <Markdown>{fileContent}</Markdown>
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
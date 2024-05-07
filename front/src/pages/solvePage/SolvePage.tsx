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

    //const [nowQIdx, setnowQIdx] = useState(-1);

    // useEffect(() => {
    //     if(roomStore.roomInfo?.host==true){
    //       setnowQIdx(0);
    //     }
    //     else{
    //       setnowQIdx(1);
    //     }
    // }, []);

    //use


    return (
      <div className='Page'>
          <p>{roomStore.sec}</p>

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
              {
                roomStore.questionIdx == -1 ? <div></div> :
                <Markdown>{roomStore.questionInfos![roomStore.questionIdx].algoQuestionContent}</Markdown>
              }
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
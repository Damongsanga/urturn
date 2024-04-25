import { useEffect, useRef, useState } from 'react';

import { Allotment } from "allotment";
import "allotment/dist/style.css";
import CodeEditor from '../components/solve/CodeEditor';
import Markdown from 'markdown-to-jsx'
import { Dropdown } from 'semantic-ui-react'

import 'semantic-ui-css/semantic.min.css'

const langOptions = [
  { key: 100, text: '100', value: 100 },
  { key: 200, text: '200', value: 200 },
  { key: 300, text: '300', value: 300 },
  { key: 400, text: '400', value: 400 },
]

export default function SolvePage() {
    const [fileContent, setFileContent] = useState('');
  
    useEffect(() => {
      setFileContent("# 테스트 문제");
    }, []);
  
    return (
      <div style={{ width: '100%', height: '100%' }}>
        <div>
        <Dropdown
            search
            defaultValue={langOptions[0].value}
            searchInput={{ type: 'string' }}
            selection
            options={langOptions}
            style={{ backgroundColor: 'green' }}
          />
        </div>
        <Allotment>
          <Allotment.Pane minSize={350}>
          <div style={{ height: '100%', overflowY: 'auto'  }}>
            <Markdown>{fileContent}</Markdown>
          </div>
          </Allotment.Pane>
          <Allotment.Pane minSize={350}>
            <Allotment vertical>
              <Allotment.Pane minSize={350}>
                <div style={{ height: '100%', width: '100%' }}>
                  <CodeEditor/>
                </div>
              </Allotment.Pane>
              <Allotment.Pane minSize={100}>
                <div>CONSOLE</div>
              </Allotment.Pane>
            </Allotment>
          </Allotment.Pane>
        </Allotment>
      </div>
    );
  }
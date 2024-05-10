import Editor, { loader } from '@monaco-editor/react';

import * as monaco from 'monaco-editor';
import editorWorker from 'monaco-editor/esm/vs/editor/editor.worker?worker';
import jsonWorker from 'monaco-editor/esm/vs/language/json/json.worker?worker';
import cssWorker from 'monaco-editor/esm/vs/language/css/css.worker?worker';
import htmlWorker from 'monaco-editor/esm/vs/language/html/html.worker?worker';
import tsWorker from 'monaco-editor/esm/vs/language/typescript/ts.worker?worker';
import { useRoomStore } from '../../stores/room';
import { useEffect } from 'react';

self.MonacoEnvironment = {
  getWorker(_, label) {
    if (label === 'json') {
      return new jsonWorker();
    }
    if (label === 'css' || label === 'scss' || label === 'less') {
      return new cssWorker();
    }
    if (label === 'html' || label === 'handlebars' || label === 'razor') {
      return new htmlWorker();
    }
    if (label === 'typescript' || label === 'javascript') {
      return new tsWorker();
    }
    return new editorWorker();
  },
};

loader.config({ monaco });

loader.init().then(/* ... */);

export default function CodeEditor( ) {
    const roomStore = useRoomStore();

    function handleEditorDidMount(editor: monaco.editor.IStandaloneCodeEditor) {
      const code = roomStore.getCode();
      if (code !== null) {
        editor.setValue(code);
      }
      editor.onDidChangeModelContent(_e => {
        roomStore.setCode(editor.getValue());
      });

      roomStore.setEditor(editor);
    }

    // function showValue() {
    //   if(roomStore.editor === null) return
    //   alert(roomStore.editor.getValue());
    // }

    return (
        <div style={{ height: '100%' }}>
            {/* <button onClick={showValue}>Show value</button> */}
            <Editor
                height="100%"
                defaultLanguage={'cpp'}
                language={roomStore.getLang()}
                defaultValue="// 코드를 적어주세요"
                onMount={handleEditorDidMount}
                options={{
                  wordWrap: 'on', // 이 줄을 추가
                  automaticLayout: true, // 자동 레이아웃 조정 활성화
                }}
            />
        </div>
    );
}
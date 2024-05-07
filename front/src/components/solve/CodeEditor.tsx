import Editor, { loader } from '@monaco-editor/react';

import * as monaco from 'monaco-editor';
import editorWorker from 'monaco-editor/esm/vs/editor/editor.worker?worker';
import jsonWorker from 'monaco-editor/esm/vs/language/json/json.worker?worker';
import cssWorker from 'monaco-editor/esm/vs/language/css/css.worker?worker';
import htmlWorker from 'monaco-editor/esm/vs/language/html/html.worker?worker';
import tsWorker from 'monaco-editor/esm/vs/language/typescript/ts.worker?worker';
import { useRoomStore } from '../../stores/room';

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

export default function CodeEditor({lang}: {lang: string}) {
    const roomStore = useRoomStore();


    function handleEditorDidMount(editor: monaco.editor.IStandaloneCodeEditor) {
      try{
        roomStore.setEditor(editor);
      }catch(e){
        console.log(e)
      }
    }

    function showValue() {
      if(roomStore.editor === null) return
      alert(roomStore.editor.getValue());
    }

    return (
        <div style={{ height: '100%' }}>
            <button onClick={showValue}>Show value</button>
            <Editor
                height="100%"
                defaultLanguage={lang}
                defaultValue="// some comment"
                onMount={handleEditorDidMount}
                options={{
                  wordWrap: 'on', // 이 줄을 추가
                  automaticLayout: true, // 자동 레이아웃 조정 활성화
                }}
            />
        </div>
    );
}
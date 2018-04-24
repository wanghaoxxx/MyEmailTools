package com.wh;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * Created by wanghao on 2017/8/29.
 */
    public class MSWordManager {
        // word???
        private Dispatch doc;

        // word??????????
        private ActiveXComponent word;

        // ????word???????
        private Dispatch documents;

        // ?????????????
        private Dispatch selection;

        private boolean saveOnExit = true;

        /** *//**
         *
         * @param visible ?true???word??ó?????
         */
        public MSWordManager(boolean visible) {
            if (word == null) {
                word = new ActiveXComponent("Word.Application");
                word.setProperty("Visible", new Variant(visible));
            }
            if (documents == null)
                documents = word.getProperty("Documents").toDispatch();
        }

        /** *//**
         * ????????????
         *
         * @param saveOnExit boolean true-?????????????false-?????????????
         */
        public void setSaveOnExit(boolean saveOnExit) {
            this.saveOnExit = saveOnExit;
        }

        /** *//**
         * ????????µ?word???
         *
         */
        public void createNewDocument() {
            doc = Dispatch.call(documents, "Add").toDispatch();
            selection = Dispatch.get(word, "Selection").toDispatch();
        }

        /** *//**
         * ??????????????
         *
         * @param docPath
         */
        public void openDocument(String docPath) {
            closeDocument();
            doc = Dispatch.call(documents, "Open", docPath).toDispatch();
            selection = Dispatch.get(word, "Selection").toDispatch();
        }

        /** *//**
         * ???????????????????????
         *
         * @param pos ????????
         */
        public void moveUp(int pos) {
            if (selection == null)
                selection = Dispatch.get(word, "Selection").toDispatch();
            for (int i = 0; i < pos; i++)
            Dispatch.call(selection, "MoveUp");

        }

        /** *//**
         * ?????????????????????????
         *
         * @param pos ????????
         */
        public void moveDown(int pos) {
            if (selection == null)
                selection = Dispatch.get(word, "Selection").toDispatch();
            for (int i = 0; i < pos; i++)
            Dispatch.call(selection, "MoveDown");
        }

        /** *//**
         * ?????????????????????????
         *
         * @param pos ????????
         */
        public void moveLeft(int pos) {
            if (selection == null)
                selection = Dispatch.get(word, "Selection").toDispatch();
            for (int i = 0; i < pos; i++) {
                Dispatch.call(selection, "MoveLeft");
            }
        }

        /** *//**
         * ?????????????????????????
         *
         * @param pos ????????
         */
        public void moveRight(int pos) {
            if (selection == null)
                selection = Dispatch.get(word, "Selection").toDispatch();
            for (int i = 0; i < pos; i++)
            Dispatch.call(selection, "MoveRight");
        }

        /** *//**
         * ???????????????????
         *
         */
        public void moveStart() {
            if (selection == null)
                selection = Dispatch.get(word, "Selection").toDispatch();
            Dispatch.call(selection, "HomeKey", new Variant(6));
        }

        public void moveEnd() {
            if (selection == null)
                selection = Dispatch.get(word, "Selection").toDispatch();
            Dispatch.call(selection, "EndKey", new Variant(6));
        }

        /** *//**
         * ??????????????????????
         *
         * @param toFindText ?????????
         * @return boolean true-????????????????false-?????????
         */
        public boolean find(String toFindText) {
            if (toFindText == null || toFindText.equals(""))
                return false;
            // ??selection??????ÿ?????
            Dispatch find = word.call(selection, "Find").toDispatch();
            // ??????????????
            Dispatch.put(find, "Text", toFindText);
            // ???????
            Dispatch.put(find, "Forward", "True");
            // ???ø??
            Dispatch.put(find, "Format", "True");
            // ???????
            Dispatch.put(find, "MatchCase", "True");
            // ??????
            Dispatch.put(find, "MatchWholeWord", "True");
            // ????????
            return Dispatch.call(find, "Execute").getBoolean();
        }

        /** *//**
         * ????????????????????
         *
         * @param toFindText ?????????
         * @param newText ?????????
         * @return
         */
        public boolean replaceText(String toFindText, String newText) {
            if (!find(toFindText))
                return false;
            Dispatch.put(selection, "Text", newText);
            return true;
        }

        /** *//**
         * ????????
         *
         * @param toFindText ?????????
         * @param newText ?????????
         */
        public void replaceAllText(String toFindText, String newText) {
            while (find(toFindText)) {
                Dispatch.put(selection, "Text", newText);
                Dispatch.call(selection, "MoveRight");
            }
        }

        /** *//**
         * ?????????????????
         *
         * @param newText ?????????????
         */
        public void insertText(String newText) {
            Dispatch.put(selection, "Text", newText);
        }


        /** *//**
         * ???????????????
         *
         * @param toCopyText
         */
        public void copy(String toCopyText) {
            moveStart();
            if (this.find(toCopyText)) {
                Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
                Dispatch.call(textRange, "Copy");
            }
        }

        /** *//**
         * ????????????????????
         *
         * @param pos
         */
        public void paste(String pos) {
            moveStart();
            if (this.find(pos)) {
                Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
                Dispatch.call(textRange, "Paste");
            }
        }

        /** *//**
         * ???????????????????????????????
         *
         * @param anotherDocPath ?????????????·??
         * @param paragraphIndex ???????????????????????????(??1???)
         */
        public void copyParagraphFromAnotherDoc(String anotherDocPath,
                                                int paragraphIndex) {
            Dispatch wordContent = Dispatch.get(doc, "Content").toDispatch(); // ??õ???????????
            Dispatch.call(wordContent, "InsertAfter", "$selection$");// ?????????????????
            copyParagraphFromAnotherDoc(anotherDocPath, paragraphIndex,
                    "$selection$");
        }

        /** *//**
         * ??????????????ÿ?????????????????????
         *
         * @param anotherDocPath ?????????????·??
         * @param paragraphIndex ???????????????????????????(??1???)
         * @param pos ??????????????
         */
        public void copyParagraphFromAnotherDoc(String anotherDocPath,
                                                int paragraphIndex, String pos) {
            Dispatch doc2 = null;
            try {
                doc2 = Dispatch.call(documents, "Open", anotherDocPath)
                        .toDispatch();
                Dispatch paragraphs = Dispatch.get(doc2, "Paragraphs").toDispatch();

                Dispatch paragraph = Dispatch.call(paragraphs, "Item",
                        new Variant(paragraphIndex)).toDispatch();
                Dispatch range = Dispatch.get(paragraph, "Range").toDispatch();
                Dispatch.call(range, "Copy");
                if (this.find(pos)) {
                    Dispatch textRange = Dispatch.get(selection, "Range")
                            .toDispatch();
                    Dispatch.call(textRange, "Paste");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (doc2 != null) {
                    Dispatch.call(doc2, "Close", new Variant(saveOnExit));
                    doc2 = null;
                }
            }
        }

        /** *//**
         * ????????????
         *
         * @param savePath ?????????·??
         */
        public void save(String savePath) {
            Dispatch.call(
                    Dispatch.call(word, "WordBasic").getDispatch(),
                    "FileSaveAs", savePath);
        }

        /** *//**
         * ?????word???
         *
         */
        public void closeDocument() {
            if (doc != null) {
                Dispatch.call(doc, "Save");
                Dispatch.call(doc, "Close", new Variant(saveOnExit));
                doc = null;
            }
        }

        /** *//**
         * ?????????
         *
         */
        public void close() {
            closeDocument();
            if (word != null) {
                Dispatch.call(word, "Quit");
                word = null;
            }
            selection = null;
            documents = null;
        }
}

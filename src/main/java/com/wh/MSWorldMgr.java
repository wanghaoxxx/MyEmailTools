package com.wh;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class MSWorldMgr {

    private Dispatch doc;

    private ActiveXComponent word;

    private Dispatch documents;

    private Dispatch selection;

    private boolean saveOnExit = true;

    private static final int DOC_FMT = 0;
    private static final int DOCX_FMT = 12;

    public MSWorldMgr(boolean visible) {
        if (word == null) {
            word = new ActiveXComponent("Word.Application");
            word.setProperty("Visible", new Variant(visible));
        }
        if (documents == null)
            documents = word.getProperty("Documents").toDispatch();
    }

    public void setSaveOnExit(boolean saveOnExit) {
        this.saveOnExit = saveOnExit;
    }

    public void createNewDocument() {
        doc = Dispatch.call(documents, "Add").toDispatch();
        selection = Dispatch.get(word, "Selection").toDispatch();
    }

    public void openDocument(String docPath) {
        closeDocument();
        doc = Dispatch.call(documents, "Open", docPath).toDispatch();
        selection = Dispatch.get(word, "Selection").toDispatch();
    }

    public void moveUp(int pos) {
        if (selection == null)
            selection = Dispatch.get(word, "Selection").toDispatch();
        for (int i = 0; i < pos; i++)
            Dispatch.call(selection, "MoveUp");

    }

    public void moveDown(int pos) {
        if (selection == null)
            selection = Dispatch.get(word, "Selection").toDispatch();
        for (int i = 0; i < pos; i++)
            Dispatch.call(selection, "MoveDown");
    }

    public void moveLeft(int pos) {
        if (selection == null)
            selection = Dispatch.get(word, "Selection").toDispatch();
        for (int i = 0; i < pos; i++) {
            Dispatch.call(selection, "MoveLeft");
        }
    }

    public void moveRight(int pos) {
        if (selection == null)
            selection = Dispatch.get(word, "Selection").toDispatch();
        for (int i = 0; i < pos; i++)
            Dispatch.call(selection, "MoveRight");
    }
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

    public boolean find(String toFindText) {
        if (toFindText == null || toFindText.equals(""))
            return false;
        Dispatch find = word.call(selection, "Find").toDispatch();
        Dispatch.put(find, "Text", toFindText);
        Dispatch.put(find, "Forward", "True");
        Dispatch.put(find, "Format", "True");
        Dispatch.put(find, "MatchCase", "True");
        Dispatch.put(find, "MatchWholeWord", "True");
        return Dispatch.call(find, "Execute").getBoolean();
    }

    public boolean replaceText(String toFindText, String newText) {
        if (!find(toFindText))
            return false;
        Dispatch.put(selection, "Text", newText);
        return true;
    }

    public void replaceAllText(String toFindText, String newText) {
        while (find(toFindText)) {
            Dispatch.put(selection, "Text", newText);
            Dispatch.call(selection, "MoveRight");
        }
    }

    public void insertText(String newText) {
        Dispatch.put(selection, "Text", newText);
    }


    public void copy(String toCopyText) {
        moveStart();
        if (this.find(toCopyText)) {
            Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
            Dispatch.call(textRange, "Copy");
        }
    }

    public void paste(String pos) {
        moveStart();
        if (this.find(pos)) {
            Dispatch textRange = Dispatch.get(selection, "Range").toDispatch();
            Dispatch.call(textRange, "Paste");
        }
    }

    public void copyParagraphFromAnotherDoc(String anotherDocPath,
                                            int paragraphIndex) {
        Dispatch wordContent = Dispatch.get(doc, "Content").toDispatch(); // ??????????????
        Dispatch.call(wordContent, "InsertAfter", "$selection$");// ?????????????????
        copyParagraphFromAnotherDoc(anotherDocPath, paragraphIndex,
                "$selection$");
    }

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

    public void save(String savePath) {
        Dispatch.call(
                Dispatch.call(word, "WordBasic").getDispatch(),
                "FileSaveAs", savePath);
    }

    public void saveDocx(String savePath) {
        Dispatch.invoke(doc, "SaveAs",
                Dispatch.Method,
                new Object[]{savePath, new Variant(DOCX_FMT)},
                new int[1]);
    }

    public void closeDocument() {
        if (doc != null) {
            Dispatch.call(doc, "Save");
            Dispatch.call(doc, "Close", new Variant(saveOnExit));
            doc = null;
        }
    }

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

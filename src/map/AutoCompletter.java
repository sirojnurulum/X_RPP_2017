/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 *
 * @author ABC
 */
public class AutoCompletter implements DocumentListener {

    private static enum Mode {
        INSERT, COMPLETION
    };
    private JTextField textField;
    private final List<String> keywords;
    private Mode mode = Mode.INSERT;

    public AutoCompletter(JTextField textField, List<String> keywords) {
        this.textField = textField;
        this.keywords = keywords;
        Collections.sort(keywords);
    }

    @Override
    public void insertUpdate(DocumentEvent de) {
        if (de.getLength() != 1) {
            return;
        }
        int pos = de.getOffset();
        String content = null;
        try {
            content = textField.getText(0, pos + 1);
        } catch (BadLocationException e) {
        }
        //find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
            if (!Character.isLetter(content.charAt(w))) {
                break;
            }
        }
        //too few chars
        if (pos - w < 2) {
            return;
        }

        String prefix = content.substring(w + 1).toLowerCase();
        int n = Collections.binarySearch(keywords, prefix);
        if (n < 0 && -n <= keywords.size()) {
            String match = keywords.get(-n - 1);
            if (match.startsWith(prefix)) {
                //a completion is found
                String completion = match.substring(pos - w);
                //cannot modify Document from within notification,
                //so submit a tack that does change later
                SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
            }
        } else {
            //nothing found
            mode = Mode.INSERT;
        }
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changedUpdate(DocumentEvent de) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public class CommitAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (mode == Mode.COMPLETION) {
                int pos = textField.getSelectionEnd();
                StringBuilder sb = new StringBuilder(textField.getText());
                sb.insert(pos, " ");
                textField.setText(sb.toString());
                textField.setCaretPosition(pos + 1);
                mode = Mode.INSERT;
            } else {
                textField.replaceSelection("\t");
            }
        }

    }

    private class CompletionTask implements Runnable {

        private String completion;
        private int position;

        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        @Override
        public void run() {
            StringBuilder sb = new StringBuilder(textField.getText());
            sb.insert(position, completion);
            textField.setText(sb.toString());
            textField.setCaretPosition(position + completion.length());
            textField.moveCaretPosition(position);
            mode = Mode.COMPLETION;
        }
    }

}

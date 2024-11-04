package org.jhotdraw.draw.tool;


import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.text.FloatingTextArea;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TextAreaCreationToolTest {

    private TextHolderFigure prototype;
    private TextAreaCreationTool textAreaCreationTool;
    private FloatingTextArea mockTextArea;
    private DrawingEditor mockEditor;
    private DrawingView mockDrawingView;
    private MouseEvent mockMouseEvent;

    @BeforeEach
    void setUp() {
        prototype = mock(TextHolderFigure.class);
        textAreaCreationTool = new TextAreaCreationTool(prototype);
        mockTextArea = mock(FloatingTextArea.class);
        mockEditor = mock(DrawingEditor.class);
        mockDrawingView = mock(DrawingView.class);
        mockMouseEvent = mock(MouseEvent.class);

        when(mockEditor.getActiveView()).thenReturn(mockDrawingView);
        textAreaCreationTool.setDrawingEditor(mockEditor);
    }

    @Test
    void testRubberbandColor() {
        assertNull(textAreaCreationTool.getRubberbandColor());

        Color color = Color.RED;
        textAreaCreationTool.setRubberbandColor(color);
        assertEquals(color, textAreaCreationTool.getRubberbandColor());
    }

    @Test
    void testTextArea() {
        assertNull(textAreaCreationTool.getTextArea());

        textAreaCreationTool.setTextArea(mockTextArea);
        assertEquals(mockTextArea, textAreaCreationTool.getTextArea());
    }

    @Test
    void testSetAndGetTypingTarget() {
        assertNull(textAreaCreationTool.getTypingTarget());

        TextHolderFigure figure = mock(TextHolderFigure.class);
        textAreaCreationTool.setTypingTarget(figure);
        assertEquals(figure, textAreaCreationTool.getTypingTarget());
    }


    @Test void testActionPerformed() {
        ActionEvent event = new ActionEvent(mockTextArea, ActionEvent.ACTION_PERFORMED, "testCommand");
        textAreaCreationTool.actionPerformed(event);
        
    }




}

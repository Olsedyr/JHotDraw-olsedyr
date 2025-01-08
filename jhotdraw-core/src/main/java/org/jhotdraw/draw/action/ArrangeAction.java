/*
 * @(#)SendToBackAction.java
 *
 * Copyright (c) 2003-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw.action;

import org.jhotdraw.draw.figure.Figure;
import java.util.*;
import javax.swing.undo.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.util.ResourceBundleUtil;

import static org.jhotdraw.draw.action.ArrangeActionType.BRING_TO_FRONT;
import static org.jhotdraw.draw.action.ArrangeActionType.SEND_TO_BACK;


public class ArrangeAction extends AbstractSelectedAction {



    private static final long serialVersionUID = 1L;
    public static final String RESOURCE_BUNDLE_BASENAME = "org.jhotdraw.draw.Labels";
    private final ArrangeActionType orderType;

    /**
     * Creates a new instance.
     */
    public ArrangeAction(DrawingEditor editor, ArrangeActionType type) {
        super(editor);
        this.orderType = type;
        ResourceBundleUtil
                .getBundle(RESOURCE_BUNDLE_BASENAME)
                .configureAction(this, getActionId(type));
        updateEnabledState();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        final DrawingView view = getView();
        final LinkedList<Figure> figures = new LinkedList<>(view.getSelectedFigures());
        arrangeFigures(view, figures, orderType);

        fireUndoableEditHappened(new AbstractUndoableEdit() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getPresentationName() {
                ResourceBundleUtil labels = ResourceBundleUtil.getBundle(RESOURCE_BUNDLE_BASENAME);
                return labels.getTextProperty(getActionId(orderType));
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                arrangeFigures(view, figures, orderType);
            }

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                undoArrangeFigures(view, figures, orderType);
            }
        });
    }


    public static void arrange(DrawingView view, Collection<Figure> figures, ArrangeActionType type) {
        Drawing drawing = view.getDrawing();
        if (type.equals(BRING_TO_FRONT)) {
            for (Figure figure : drawing.sort(figures)) {
                drawing.arrange(figure,BRING_TO_FRONT);
            }
        } else if (type.equals(SEND_TO_BACK)) {
            for (Figure figure : drawing.sort(figures)) {
                drawing.arrange(figure,SEND_TO_BACK);
            }
        }
    }

    private void arrangeFigures(DrawingView view, LinkedList<Figure> figures, ArrangeActionType orderType) {
        if (orderType.equals(BRING_TO_FRONT)) {
            arrange(view, figures, BRING_TO_FRONT);
        } else if (orderType.equals(SEND_TO_BACK)) {
            arrange(view, figures, SEND_TO_BACK);
        }
    }
    private void undoArrangeFigures(DrawingView view, LinkedList<Figure> figures, ArrangeActionType orderType) {
        if (orderType.equals(BRING_TO_FRONT)) {
            arrange(view, figures, SEND_TO_BACK);
        } else if (orderType.equals(SEND_TO_BACK)) {
            arrange(view, figures, BRING_TO_FRONT);
        }
    }

    // Maps ArrangeActionType to the corresponding action ID string.
    public static String getActionId(ArrangeActionType type) {
        switch (type) {
            case BRING_TO_FRONT:
                return "edit.bringToFront";
            case SEND_TO_BACK:
                return "edit.sendToBack";
            default:
                throw new IllegalArgumentException("Unknown ArrangeActionType: " + type);
        }
    }
}
